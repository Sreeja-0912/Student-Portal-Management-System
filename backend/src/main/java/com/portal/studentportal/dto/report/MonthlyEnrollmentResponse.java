package com.portal.studentportal.dto.report;

public record MonthlyEnrollmentResponse(
        int month,
        long count
) {}
