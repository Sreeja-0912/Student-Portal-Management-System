package com.portal.studentportal.service.impl;

import com.portal.studentportal.dto.enrollment.EnrollmentRequest;
import com.portal.studentportal.dto.enrollment.EnrollmentResponse;
import com.portal.studentportal.entity.Course;
import com.portal.studentportal.entity.Student;
import com.portal.studentportal.entity.StudentCourse;
import com.portal.studentportal.exception.ConflictException;
import com.portal.studentportal.exception.ResourceNotFoundException;
import com.portal.studentportal.repository.CourseRepository;
import com.portal.studentportal.repository.StudentCourseRepository;
import com.portal.studentportal.repository.StudentRepository;
import com.portal.studentportal.service.AuditLogService;
import com.portal.studentportal.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private final StudentCourseRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final AuditLogService auditLogService;

    @Override
    @Transactional
    public EnrollmentResponse enroll(EnrollmentRequest request) {
        if (enrollmentRepository.existsByStudentIdAndCourseIdAndDeletedFalse(request.studentId(), request.courseId())) {
            throw new ConflictException("Student is already enrolled in this course");
        }
        Student student = student(request.studentId());
        Course course = course(request.courseId());
        StudentCourse saved = enrollmentRepository.save(StudentCourse.builder().student(student).course(course).build());
        auditLogService.log("ENROLL STUDENT " + student.getRollNumber() + " IN " + course.getCourseCode(), null);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> byStudent(Long studentId) {
        student(studentId);
        return enrollmentRepository.findByStudentIdAndDeletedFalse(studentId).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> byCourse(Long courseId) {
        course(courseId);
        return enrollmentRepository.findByCourseIdAndDeletedFalse(courseId).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public void unenroll(Long studentId, Long courseId) {
        StudentCourse enrollment = enrollmentRepository.findByStudentIdAndCourseIdAndDeletedFalse(studentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
        enrollment.setDeleted(true);
        enrollmentRepository.save(enrollment);
        auditLogService.log("UNENROLL STUDENT " + studentId + " FROM COURSE " + courseId, null);
    }

    private Student student(Long id) {
        return studentRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
    }

    private Course course(Long id) {
        return courseRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + id));
    }

    private EnrollmentResponse toResponse(StudentCourse enrollment) {
        Student s = enrollment.getStudent();
        Course c = enrollment.getCourse();
        String name = (s.getFirstName() + " " + (s.getLastName() == null ? "" : s.getLastName())).trim();
        return new EnrollmentResponse(enrollment.getId(), s.getId(), name, c.getId(), c.getCourseCode(), c.getCourseName());
    }
}
