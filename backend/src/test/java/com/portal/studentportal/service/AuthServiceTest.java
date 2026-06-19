package com.portal.studentportal.service;

import com.portal.studentportal.dto.auth.RegisterRequest;
import com.portal.studentportal.exception.ConflictException;
import com.portal.studentportal.repository.RoleRepository;
import com.portal.studentportal.repository.UserRepository;
import com.portal.studentportal.security.JwtService;
import com.portal.studentportal.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock AuthenticationManager authenticationManager;
    @Mock UserRepository userRepository;
    @Mock RoleRepository roleRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtService jwtService;
    @Mock AuditLogService auditLogService;

    @Test
    void duplicateUsernameThrowsConflictOnRegister() {
        AuthServiceImpl service = new AuthServiceImpl(authenticationManager, userRepository, roleRepository, passwordEncoder, jwtService, auditLogService);
        RegisterRequest request = new RegisterRequest("admin", "new@example.com", "Password@123", "STUDENT");
        when(userRepository.existsByUsernameAndDeletedFalse("admin")).thenReturn(true);
        assertThrows(ConflictException.class, () -> service.register(request));
    }
}
