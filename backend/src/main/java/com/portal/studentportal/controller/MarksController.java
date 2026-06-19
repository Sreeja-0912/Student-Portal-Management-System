package com.portal.studentportal.controller;

import com.portal.studentportal.dto.marks.MarksRequest;
import com.portal.studentportal.dto.marks.MarksResponse;
import com.portal.studentportal.dto.marks.RankingResponse;
import com.portal.studentportal.service.MarksService;
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
@RequestMapping("/api/marks")
@RequiredArgsConstructor
@Tag(name = "Marks")
public class MarksController {
    private final MarksService marksService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    public ResponseEntity<MarksResponse> record(@Valid @RequestBody MarksRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(marksService.record(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    public MarksResponse update(@PathVariable Long id, @Valid @RequestBody MarksRequest request) {
        return marksService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        marksService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/student/{id}")
    public List<MarksResponse> byStudent(@PathVariable Long id) {
        return marksService.byStudent(id);
    }

    @GetMapping("/rankings")
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    public Page<RankingResponse> rankings(@PageableDefault(size = 10) Pageable pageable) {
        return marksService.rankings(pageable);
    }
}
