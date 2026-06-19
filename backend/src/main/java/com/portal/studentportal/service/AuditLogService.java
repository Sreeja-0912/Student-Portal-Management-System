package com.portal.studentportal.service;

import com.portal.studentportal.dto.audit.AuditLogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditLogService {
    void log(String action, String username);
    Page<AuditLogResponse> list(Pageable pageable);
}
