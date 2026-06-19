package com.portal.studentportal.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(min = 3, max = 100) String username,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password,
        @Pattern(regexp = "ADMIN|FACULTY|STUDENT", message = "role must be ADMIN, FACULTY or STUDENT") String role
) {}
