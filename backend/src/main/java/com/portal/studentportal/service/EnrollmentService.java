package com.portal.studentportal.service;

import com.portal.studentportal.dto.enrollment.EnrollmentRequest;
import com.portal.studentportal.dto.enrollment.EnrollmentResponse;

import java.util.List;

public interface EnrollmentService {
    EnrollmentResponse enroll(EnrollmentRequest request);
    List<EnrollmentResponse> byStudent(Long studentId);
    List<EnrollmentResponse> byCourse(Long courseId);
    void unenroll(Long studentId, Long courseId);
}
