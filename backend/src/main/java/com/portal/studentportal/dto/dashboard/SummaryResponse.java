package com.portal.studentportal.dto.dashboard;

import java.math.BigDecimal;

public record SummaryResponse(
        long totalStudents,
        long totalCourses,
        BigDecimal averageAttendance,
        BigDecimal passPercentage
) {}
