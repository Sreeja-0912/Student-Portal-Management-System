package com.portal.studentportal.dto.studymaterial;

public record StudyMaterialResponse(
        Long id,
        Long courseId,
        String courseCode,
        String courseName,
        String title,
        String fileUrl
) {}
