package com.portal.studentportal.dto.announcement;

import java.time.LocalDateTime;

public record AnnouncementResponse(
        Long id,
        String title,
        String description,
        String createdByName,
        LocalDateTime createdDate,
        LocalDateTime updatedDate
) {}
