package com.portal.studentportal.dto.enrollment;

public record EnrollmentResponse(
        Long id,
        Long studentId,
        String studentName,
        Long courseId,
        String courseCode,
        String courseName
) {}
