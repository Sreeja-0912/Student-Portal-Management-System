package com.portal.studentportal.dto.marks;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MarksRequest(
        @NotNull Long studentId,
        @NotNull Long courseId,
        @NotNull @DecimalMin("0.00") @DecimalMax("100.00") BigDecimal score
) {}
