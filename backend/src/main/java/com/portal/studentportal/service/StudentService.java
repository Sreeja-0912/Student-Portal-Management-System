package com.portal.studentportal.service;

import com.portal.studentportal.dto.student.StudentRequest;
import com.portal.studentportal.dto.student.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentService {
    Page<StudentResponse> search(String keyword, Pageable pageable);
    StudentResponse create(StudentRequest request);
    StudentResponse get(Long id);
    StudentResponse update(Long id, StudentRequest request);
    void delete(Long id);
    StudentResponse me(String username);
}
