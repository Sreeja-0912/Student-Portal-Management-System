package com.portal.studentportal.repository;

import com.portal.studentportal.entity.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    Optional<Announcement> findByIdAndDeletedFalse(Long id);
    Page<Announcement> findByDeletedFalse(Pageable pageable);
}
