package com.portal.studentportal.dto.dashboard;

import java.math.BigDecimal;

public record AtRiskStudentResponse(
        Long studentId,
        String studentName,
        String rollNumber,
        BigDecimal averageAttendance,
        BigDecimal averageScore,
        String reason
) {}
