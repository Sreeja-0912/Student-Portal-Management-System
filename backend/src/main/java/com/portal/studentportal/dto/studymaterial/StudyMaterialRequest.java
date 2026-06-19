package com.portal.studentportal.dto.studymaterial;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StudyMaterialRequest(
        @NotNull Long courseId,
        @NotBlank @Size(max = 200) String title,
        @NotBlank @Size(max = 500) String fileUrl
) {}
