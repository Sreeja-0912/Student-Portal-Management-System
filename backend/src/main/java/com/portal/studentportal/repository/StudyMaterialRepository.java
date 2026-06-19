package com.portal.studentportal.repository;

import com.portal.studentportal.entity.StudyMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyMaterialRepository extends JpaRepository<StudyMaterial, Long> {
    Optional<StudyMaterial> findByIdAndDeletedFalse(Long id);
    Page<StudyMaterial> findByDeletedFalse(Pageable pageable);
    List<StudyMaterial> findByCourseIdAndDeletedFalse(Long courseId);
}
