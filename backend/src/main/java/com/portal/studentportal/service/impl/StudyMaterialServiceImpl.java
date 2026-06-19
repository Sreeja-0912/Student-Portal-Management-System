package com.portal.studentportal.service.impl;

import com.portal.studentportal.dto.studymaterial.StudyMaterialRequest;
import com.portal.studentportal.dto.studymaterial.StudyMaterialResponse;
import com.portal.studentportal.entity.Course;
import com.portal.studentportal.entity.StudyMaterial;
import com.portal.studentportal.exception.ResourceNotFoundException;
import com.portal.studentportal.mapper.StudyMaterialMapper;
import com.portal.studentportal.repository.CourseRepository;
import com.portal.studentportal.repository.StudyMaterialRepository;
import com.portal.studentportal.service.AuditLogService;
import com.portal.studentportal.service.StudyMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyMaterialServiceImpl implements StudyMaterialService {
    private final StudyMaterialRepository studyMaterialRepository;
    private final CourseRepository courseRepository;
    private final StudyMaterialMapper studyMaterialMapper;
    private final AuditLogService auditLogService;

    @Override
    @Transactional(readOnly = true)
    public Page<StudyMaterialResponse> list(Pageable pageable) {
        return studyMaterialRepository.findByDeletedFalse(pageable).map(studyMaterialMapper::toResponse);
    }

    @Override
    @Transactional
    public StudyMaterialResponse create(StudyMaterialRequest request) {
        StudyMaterial material = StudyMaterial.builder()
                .course(course(request.courseId()))
                .title(request.title())
                .fileUrl(request.fileUrl())
                .build();
        StudyMaterial saved = studyMaterialRepository.save(material);
        auditLogService.log("CREATE STUDY MATERIAL " + saved.getId(), null);
        return studyMaterialMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudyMaterialResponse> byCourse(Long courseId) {
        course(courseId);
        return studyMaterialRepository.findByCourseIdAndDeletedFalse(courseId).stream().map(studyMaterialMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public StudyMaterialResponse update(Long id, StudyMaterialRequest request) {
        StudyMaterial material = find(id);
        material.setCourse(course(request.courseId()));
        material.setTitle(request.title());
        material.setFileUrl(request.fileUrl());
        StudyMaterial saved = studyMaterialRepository.save(material);
        auditLogService.log("UPDATE STUDY MATERIAL " + saved.getId(), null);
        return studyMaterialMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        StudyMaterial material = find(id);
        material.setDeleted(true);
        studyMaterialRepository.save(material);
        auditLogService.log("DELETE STUDY MATERIAL " + id, null);
    }

    private StudyMaterial find(Long id) {
        return studyMaterialRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Study material not found with id " + id));
    }

    private Course course(Long id) {
        return courseRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + id));
    }
}
