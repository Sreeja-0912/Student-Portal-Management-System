package com.portal.studentportal.dto.course;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CourseRequest(
        @NotBlank @Size(max = 30) String courseCode,
        @NotBlank @Size(max = 150) String courseName,
        @NotNull @Min(1) @Max(8) Integer credits,
        @Size(max = 100) String facultyName
) {}
