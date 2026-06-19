package com.portal.studentportal.config;

import com.portal.studentportal.entity.Course;
import com.portal.studentportal.entity.Role;
import com.portal.studentportal.entity.RoleName;
import com.portal.studentportal.entity.User;
import com.portal.studentportal.repository.CourseRepository;
import com.portal.studentportal.repository.RoleRepository;
import com.portal.studentportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seedData() {
        return args -> {
            for (RoleName roleName : RoleName.values()) {
                roleRepository.findByName(roleName).orElseGet(() -> roleRepository.save(Role.builder().name(roleName).build()));
            }

            Role adminRole = roleRepository.findByName(RoleName.ADMIN).orElseThrow();
            if (!userRepository.existsByUsernameAndDeletedFalse("admin")) {
                userRepository.save(User.builder()
                        .username("admin")
                        .email("admin@studentportal.local")
                        .password(passwordEncoder.encode("Admin@123"))
                        .role(adminRole)
                        .active(true)
                        .build());
            }

            if (courseRepository.countByDeletedFalse() == 0) {
                courseRepository.save(Course.builder().courseCode("CS101").courseName("Programming Fundamentals").credits(4).facultyName("Dr. Sharma").build());
                courseRepository.save(Course.builder().courseCode("MA101").courseName("Engineering Mathematics").credits(3).facultyName("Prof. Rao").build());
            }
        };
    }
}
