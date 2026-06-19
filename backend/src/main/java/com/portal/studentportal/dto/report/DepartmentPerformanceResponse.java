package com.portal.studentportal.dto.report;

import java.math.BigDecimal;

public record DepartmentPerformanceResponse(
        String department,
        BigDecimal averageScore
) {}
