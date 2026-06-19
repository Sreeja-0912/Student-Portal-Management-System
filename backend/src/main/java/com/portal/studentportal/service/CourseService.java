package com.portal.studentportal.service;

import com.portal.studentportal.dto.course.CourseRequest;
import com.portal.studentportal.dto.course.CourseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseService {
    Page<CourseResponse> search(String keyword, Pageable pageable);
    CourseResponse create(CourseRequest request);
    CourseResponse get(Long id);
    CourseResponse update(Long id, CourseRequest request);
    void delete(Long id);
}
