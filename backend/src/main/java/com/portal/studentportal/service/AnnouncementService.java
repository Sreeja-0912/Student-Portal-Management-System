package com.portal.studentportal.service;

import com.portal.studentportal.dto.announcement.AnnouncementRequest;
import com.portal.studentportal.dto.announcement.AnnouncementResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnnouncementService {
    Page<AnnouncementResponse> list(Pageable pageable);
    AnnouncementResponse create(AnnouncementRequest request, String username);
    AnnouncementResponse get(Long id);
    AnnouncementResponse update(Long id, AnnouncementRequest request);
    void delete(Long id);
}
