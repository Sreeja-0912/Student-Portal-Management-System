package com.portal.studentportal.dto.student;

import java.time.LocalDateTime;

public record StudentResponse(
        Long id,
        String rollNumber,
        String firstName,
        String lastName,
        String email,
        String phone,
        String department,
        Integer semester,
        Long userId,
        LocalDateTime createdDate,
        LocalDateTime updatedDate
) {}
