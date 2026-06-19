package com.portal.studentportal.controller;

import com.portal.studentportal.dto.enrollment.EnrollmentRequest;
import com.portal.studentportal.dto.enrollment.EnrollmentResponse;
import com.portal.studentportal.service.EnrollmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student-courses")
@RequiredArgsConstructor
@Tag(name = "Student Course Enrollment")
public class StudentCourseController {
    private final EnrollmentService enrollmentService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EnrollmentResponse> enroll(@Valid @RequestBody EnrollmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollmentService.enroll(request));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
    public List<EnrollmentResponse> byStudent(@PathVariable Long studentId) {
        return enrollmentService.byStudent(studentId);
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    public List<EnrollmentResponse> byCourse(@PathVariable Long courseId) {
        return enrollmentService.byCourse(courseId);
    }

    @DeleteMapping("/student/{studentId}/course/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> unenroll(@PathVariable Long studentId, @PathVariable Long courseId) {
        enrollmentService.unenroll(studentId, courseId);
        return ResponseEntity.noContent().build();
    }
}
