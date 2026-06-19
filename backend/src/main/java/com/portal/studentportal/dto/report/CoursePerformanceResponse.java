package com.portal.studentportal.dto.report;

import java.math.BigDecimal;

public record CoursePerformanceResponse(
        Long courseId,
        String courseCode,
        String courseName,
        BigDecimal averageScore,
        BigDecimal highestScore,
        BigDecimal lowestScore
) {}
