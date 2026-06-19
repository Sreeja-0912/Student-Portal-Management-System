package com.portal.studentportal.controller;

import com.portal.studentportal.dto.announcement.AnnouncementRequest;
import com.portal.studentportal.dto.announcement.AnnouncementResponse;
import com.portal.studentportal.service.AnnouncementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
@Tag(name = "Announcements")
public class AnnouncementController {
    private final AnnouncementService announcementService;

    @GetMapping
    public Page<AnnouncementResponse> list(@PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return announcementService.list(pageable);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    public ResponseEntity<AnnouncementResponse> create(@Valid @RequestBody AnnouncementRequest request, Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(announcementService.create(request, principal.getName()));
    }

    @GetMapping("/{id}")
    public AnnouncementResponse get(@PathVariable Long id) {
        return announcementService.get(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public AnnouncementResponse update(@PathVariable Long id, @Valid @RequestBody AnnouncementRequest request) {
        return announcementService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        announcementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
