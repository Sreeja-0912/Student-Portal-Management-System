package com.portal.studentportal.dto.attendance;

import java.math.BigDecimal;

public record AttendanceResponse(
        Long id,
        Long studentId,
        String studentName,
        Long courseId,
        String courseCode,
        String courseName,
        BigDecimal attendancePercentage
) {}
