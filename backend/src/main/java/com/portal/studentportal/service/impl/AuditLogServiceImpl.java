package com.portal.studentportal.service.impl;

import com.portal.studentportal.dto.audit.AuditLogResponse;
import com.portal.studentportal.entity.AuditLog;
import com.portal.studentportal.repository.AuditLogRepository;
import com.portal.studentportal.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {
    private final AuditLogRepository auditLogRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(String action, String username) {
        auditLogRepository.save(AuditLog.builder().action(action).username(username).build());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogResponse> list(Pageable pageable) {
        return auditLogRepository.findByDeletedFalse(pageable)
                .map(log -> new AuditLogResponse(log.getId(), log.getAction(), log.getUsername(), log.getCreatedDate()));
    }
}
