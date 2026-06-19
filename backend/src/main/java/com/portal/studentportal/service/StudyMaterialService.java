package com.portal.studentportal.service;

import com.portal.studentportal.dto.studymaterial.StudyMaterialRequest;
import com.portal.studentportal.dto.studymaterial.StudyMaterialResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudyMaterialService {
    Page<StudyMaterialResponse> list(Pageable pageable);
    StudyMaterialResponse create(StudyMaterialRequest request);
    List<StudyMaterialResponse> byCourse(Long courseId);
    StudyMaterialResponse update(Long id, StudyMaterialRequest request);
    void delete(Long id);
}
