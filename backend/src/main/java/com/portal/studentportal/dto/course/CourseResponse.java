package com.portal.studentportal.dto.course;

import java.time.LocalDateTime;

public record CourseResponse(
        Long id,
        String courseCode,
        String courseName,
        Integer credits,
        String facultyName,
        LocalDateTime createdDate,
        LocalDateTime updatedDate
) {}
