package com.portal.studentportal.dto.audit;

import java.time.LocalDateTime;

public record AuditLogResponse(
        Long id,
        String action,
        String username,
        LocalDateTime createdDate
) {}
