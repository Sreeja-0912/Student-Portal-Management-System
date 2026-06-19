package com.portal.studentportal.service.impl;

import com.portal.studentportal.dto.announcement.AnnouncementRequest;
import com.portal.studentportal.dto.announcement.AnnouncementResponse;
import com.portal.studentportal.entity.Announcement;
import com.portal.studentportal.exception.ResourceNotFoundException;
import com.portal.studentportal.mapper.AnnouncementMapper;
import com.portal.studentportal.repository.AnnouncementRepository;
import com.portal.studentportal.service.AnnouncementService;
import com.portal.studentportal.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private final AnnouncementMapper announcementMapper;
    private final AuditLogService auditLogService;

    @Override
    @Transactional(readOnly = true)
    public Page<AnnouncementResponse> list(Pageable pageable) {
        return announcementRepository.findByDeletedFalse(pageable).map(announcementMapper::toResponse);
    }

    @Override
    @Transactional
    public AnnouncementResponse create(AnnouncementRequest request, String username) {
        Announcement announcement = announcementMapper.toEntity(request);
        announcement.setCreatedByName(username);
        Announcement saved = announcementRepository.save(announcement);
        auditLogService.log("CREATE ANNOUNCEMENT " + saved.getId(), username);
        return announcementMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AnnouncementResponse get(Long id) {
        return announcementMapper.toResponse(find(id));
    }

    @Override
    @Transactional
    public AnnouncementResponse update(Long id, AnnouncementRequest request) {
        Announcement announcement = find(id);
        announcementMapper.update(request, announcement);
        Announcement saved = announcementRepository.save(announcement);
        auditLogService.log("UPDATE ANNOUNCEMENT " + id, null);
        return announcementMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Announcement announcement = find(id);
        announcement.setDeleted(true);
        announcementRepository.save(announcement);
        auditLogService.log("DELETE ANNOUNCEMENT " + id, null);
    }

    private Announcement find(Long id) {
        return announcementRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Announcement not found with id " + id));
    }
}
