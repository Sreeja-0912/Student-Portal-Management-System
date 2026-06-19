package com.portal.studentportal.dto.enrollment;

import jakarta.validation.constraints.NotNull;

public record EnrollmentRequest(
        @NotNull Long studentId,
        @NotNull Long courseId
) {}
