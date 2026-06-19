package com.portal.studentportal.service;

import com.portal.studentportal.dto.attendance.AttendanceRequest;
import com.portal.studentportal.entity.Attendance;
import com.portal.studentportal.exception.ConflictException;
import com.portal.studentportal.repository.AttendanceRepository;
import com.portal.studentportal.repository.CourseRepository;
import com.portal.studentportal.repository.StudentRepository;
import com.portal.studentportal.service.impl.AttendanceServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {
    @Mock AttendanceRepository attendanceRepository;
    @Mock StudentRepository studentRepository;
    @Mock CourseRepository courseRepository;
    @Mock AuditLogService auditLogService;

    @Test
    void duplicateAttendanceThrowsConflict() {
        AttendanceServiceImpl service = new AttendanceServiceImpl(attendanceRepository, studentRepository, courseRepository, auditLogService);
        when(attendanceRepository.findByStudentIdAndCourseIdAndDeletedFalse(1L, 1L)).thenReturn(Optional.of(new Attendance()));
        assertThrows(ConflictException.class, () -> service.record(new AttendanceRequest(1L, 1L, BigDecimal.valueOf(80))));
    }
}
