package com.portal.studentportal.service.impl;

import com.portal.studentportal.dto.attendance.AttendanceRequest;
import com.portal.studentportal.dto.attendance.AttendanceResponse;
import com.portal.studentportal.entity.Attendance;
import com.portal.studentportal.entity.Course;
import com.portal.studentportal.entity.Student;
import com.portal.studentportal.exception.ConflictException;
import com.portal.studentportal.exception.ResourceNotFoundException;
import com.portal.studentportal.repository.AttendanceRepository;
import com.portal.studentportal.repository.CourseRepository;
import com.portal.studentportal.repository.StudentRepository;
import com.portal.studentportal.service.AttendanceService;
import com.portal.studentportal.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
    private static final BigDecimal DEFAULTER_THRESHOLD = BigDecimal.valueOf(75);

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final AuditLogService auditLogService;

    @Override
    @Transactional
    public AttendanceResponse record(AttendanceRequest request) {
        if (attendanceRepository.findByStudentIdAndCourseIdAndDeletedFalse(request.studentId(), request.courseId()).isPresent()) {
            throw new ConflictException("Attendance already recorded for this student and course");
        }
        Attendance attendance = Attendance.builder()
                .student(student(request.studentId()))
                .course(course(request.courseId()))
                .attendancePercentage(request.attendancePercentage())
                .build();
        Attendance saved = attendanceRepository.save(attendance);
        auditLogService.log("RECORD ATTENDANCE " + saved.getId(), null);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public AttendanceResponse update(Long id, AttendanceRequest request) {
        Attendance attendance = find(id);
        Student student = student(request.studentId());
        Course course = course(request.courseId());
        attendanceRepository.findByStudentIdAndCourseIdAndDeletedFalse(request.studentId(), request.courseId())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> { throw new ConflictException("Another attendance record already exists for this student and course"); });
        attendance.setStudent(student);
        attendance.setCourse(course);
        attendance.setAttendancePercentage(request.attendancePercentage());
        Attendance saved = attendanceRepository.save(attendance);
        auditLogService.log("UPDATE ATTENDANCE " + saved.getId(), null);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Attendance attendance = find(id);
        attendance.setDeleted(true);
        attendanceRepository.save(attendance);
        auditLogService.log("DELETE ATTENDANCE " + id, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponse> byStudent(Long studentId) {
        student(studentId);
        return attendanceRepository.findByStudentIdAndDeletedFalse(studentId).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AttendanceResponse> defaulters(Pageable pageable) {
        return attendanceRepository.findByAttendancePercentageLessThanAndDeletedFalse(DEFAULTER_THRESHOLD, pageable).map(this::toResponse);
    }

    private Attendance find(Long id) {
        return attendanceRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with id " + id));
    }

    private Student student(Long id) {
        return studentRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
    }

    private Course course(Long id) {
        return courseRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + id));
    }

    private AttendanceResponse toResponse(Attendance attendance) {
        Student s = attendance.getStudent();
        Course c = attendance.getCourse();
        String name = (s.getFirstName() + " " + (s.getLastName() == null ? "" : s.getLastName())).trim();
        return new AttendanceResponse(attendance.getId(), s.getId(), name, c.getId(), c.getCourseCode(), c.getCourseName(), attendance.getAttendancePercentage());
    }
}
