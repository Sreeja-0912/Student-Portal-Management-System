package com.portal.studentportal.service.impl;

import com.portal.studentportal.dto.auth.LoginRequest;
import com.portal.studentportal.dto.auth.LoginResponse;
import com.portal.studentportal.dto.auth.RegisterRequest;
import com.portal.studentportal.entity.Role;
import com.portal.studentportal.entity.RoleName;
import com.portal.studentportal.entity.User;
import com.portal.studentportal.exception.ConflictException;
import com.portal.studentportal.exception.ResourceNotFoundException;
import com.portal.studentportal.repository.RoleRepository;
import com.portal.studentportal.repository.UserRepository;
import com.portal.studentportal.security.JwtService;
import com.portal.studentportal.service.AuditLogService;
import com.portal.studentportal.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuditLogService auditLogService;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        User user = userRepository.findByUsernameAndDeletedFalse(request.username())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        UserDetails details = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole().getName().name())
                .build();
        String role = user.getRole().getName().name();
        auditLogService.log("LOGIN", user.getUsername());
        return new LoginResponse(jwtService.generateToken(details, role), user.getUsername(), user.getEmail(), role);
    }

    @Override
    @Transactional
    public LoginResponse register(RegisterRequest request) {
        if (userRepository.existsByUsernameAndDeletedFalse(request.username())) {
            throw new ConflictException("Username already exists");
        }
        if (userRepository.existsByEmailAndDeletedFalse(request.email())) {
            throw new ConflictException("Email already exists");
        }
        RoleName roleName = request.role() == null || request.role().isBlank()
                ? RoleName.STUDENT
                : RoleName.valueOf(request.role().toUpperCase());
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));

        User user = userRepository.save(User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(role)
                .active(true)
                .build());
        auditLogService.log("REGISTER USER", user.getUsername());

        UserDetails details = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("ROLE_" + roleName.name())
                .build();
        return new LoginResponse(jwtService.generateToken(details, roleName.name()), user.getUsername(), user.getEmail(), roleName.name());
    }
}
