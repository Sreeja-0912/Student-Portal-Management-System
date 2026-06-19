package com.portal.studentportal.dto.marks;

import java.math.BigDecimal;

public record MarksResponse(
        Long id,
        Long studentId,
        String studentName,
        Long courseId,
        String courseCode,
        String courseName,
        BigDecimal score,
        String grade,
        String result
) {}
