package com.portal.studentportal.dto.report;

import java.math.BigDecimal;

public record PassFailStatisticsResponse(
        long passCount,
        long failCount,
        BigDecimal passPercentage,
        BigDecimal failPercentage
) {}
