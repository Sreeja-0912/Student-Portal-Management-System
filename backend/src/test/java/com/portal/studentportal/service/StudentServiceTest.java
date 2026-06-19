package com.portal.studentportal.service;

import com.portal.studentportal.dto.student.StudentRequest;
import com.portal.studentportal.exception.ConflictException;
import com.portal.studentportal.mapper.StudentMapper;
import com.portal.studentportal.repository.StudentRepository;
import com.portal.studentportal.repository.UserRepository;
import com.portal.studentportal.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock StudentRepository studentRepository;
    @Mock UserRepository userRepository;
    @Mock StudentMapper studentMapper;
    @Mock AuditLogService auditLogService;

    @Test
    void duplicateRollNumberThrowsConflict() {
        StudentServiceImpl service = new StudentServiceImpl(studentRepository, userRepository, studentMapper, auditLogService);
        StudentRequest request = new StudentRequest("R001", "Asha", "K", "asha@example.com", "9999999999", "CSE", 3, null);
        when(studentRepository.existsByRollNumberAndDeletedFalse("R001")).thenReturn(true);
        assertThrows(ConflictException.class, () -> service.create(request));
    }
}
