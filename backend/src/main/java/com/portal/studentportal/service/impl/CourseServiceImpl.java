package com.portal.studentportal.service.impl;

import com.portal.studentportal.dto.course.CourseRequest;
import com.portal.studentportal.dto.course.CourseResponse;
import com.portal.studentportal.entity.Course;
import com.portal.studentportal.exception.ConflictException;
import com.portal.studentportal.exception.ResourceNotFoundException;
import com.portal.studentportal.mapper.CourseMapper;
import com.portal.studentportal.repository.CourseRepository;
import com.portal.studentportal.service.AuditLogService;
import com.portal.studentportal.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final AuditLogService auditLogService;

    @Override
    @Transactional(readOnly = true)
    public Page<CourseResponse> search(String keyword, Pageable pageable) {
        return courseRepository.search(keyword, pageable).map(courseMapper::toResponse);
    }

    @Override
    @Transactional
    public CourseResponse create(CourseRequest request) {
        if (courseRepository.existsByCourseCodeAndDeletedFalse(request.courseCode())) {
            throw new ConflictException("Course code already exists");
        }
        Course saved = courseRepository.save(courseMapper.toEntity(request));
        auditLogService.log("CREATE COURSE " + saved.getCourseCode(), null);
        return courseMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse get(Long id) {
        return courseMapper.toResponse(findCourse(id));
    }

    @Override
    @Transactional
    public CourseResponse update(Long id, CourseRequest request) {
        Course course = findCourse(id);
        courseMapper.update(request, course);
        Course saved = courseRepository.save(course);
        auditLogService.log("UPDATE COURSE " + saved.getCourseCode(), null);
        return courseMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Course course = findCourse(id);
        course.setDeleted(true);
        courseRepository.save(course);
        auditLogService.log("DELETE COURSE " + course.getCourseCode(), null);
    }

    private Course findCourse(Long id) {
        return courseRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + id));
    }
}
