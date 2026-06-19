package com.portal.studentportal.controller;

import com.portal.studentportal.dto.studymaterial.StudyMaterialRequest;
import com.portal.studentportal.dto.studymaterial.StudyMaterialResponse;
import com.portal.studentportal.service.StudyMaterialService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import java.util.List;

@RestController
@RequestMapping("/api/study-materials")
@RequiredArgsConstructor
@Tag(name = "Study Materials")
public class StudyMaterialController {
    private final StudyMaterialService studyMaterialService;

    @GetMapping
    public Page<StudyMaterialResponse> list(@PageableDefault(size = 10) Pageable pageable) {
        return studyMaterialService.list(pageable);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudyMaterialResponse> create(@Valid @RequestBody StudyMaterialRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studyMaterialService.create(request));
    }

    @GetMapping("/course/{id}")
    public List<StudyMaterialResponse> byCourse(@PathVariable Long id) {
        return studyMaterialService.byCourse(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public StudyMaterialResponse update(@PathVariable Long id, @Valid @RequestBody StudyMaterialRequest request) {
        return studyMaterialService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studyMaterialService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
