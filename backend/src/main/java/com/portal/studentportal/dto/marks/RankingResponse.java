package com.portal.studentportal.dto.marks;

import java.math.BigDecimal;

public record RankingResponse(
        int rank,
        Long studentId,
        String studentName,
        String rollNumber,
        String department,
        BigDecimal averageScore,
        String grade
) {}
