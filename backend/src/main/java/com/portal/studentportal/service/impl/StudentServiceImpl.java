package com.portal.studentportal.service.impl;

import com.portal.studentportal.dto.student.StudentRequest;
import com.portal.studentportal.dto.student.StudentResponse;
import com.portal.studentportal.entity.Student;
import com.portal.studentportal.entity.User;
import com.portal.studentportal.exception.ConflictException;
import com.portal.studentportal.exception.ResourceNotFoundException;
import com.portal.studentportal.mapper.StudentMapper;
import com.portal.studentportal.repository.StudentRepository;
import com.portal.studentportal.repository.UserRepository;
import com.portal.studentportal.service.AuditLogService;
import com.portal.studentportal.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final StudentMapper studentMapper;
    private final AuditLogService auditLogService;

    @Override
    @Transactional(readOnly = true)
    public Page<StudentResponse> search(String keyword, Pageable pageable) {
        return studentRepository.search(keyword, pageable).map(studentMapper::toResponse);
    }

    @Override
    @Transactional
    public StudentResponse create(StudentRequest request) {
        if (studentRepository.existsByRollNumberAndDeletedFalse(request.rollNumber())) {
            throw new ConflictException("Roll number already exists");
        }
        if (studentRepository.existsByEmailAndDeletedFalse(request.email())) {
            throw new ConflictException("Student email already exists");
        }
        Student student = studentMapper.toEntity(request);
        attachUser(request.userId(), student);
        Student saved = studentRepository.save(student);
        auditLogService.log("CREATE STUDENT " + saved.getRollNumber(), null);
        return studentMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse get(Long id) {
        return studentMapper.toResponse(findStudent(id));
    }

    @Override
    @Transactional
    public StudentResponse update(Long id, StudentRequest request) {
        Student student = findStudent(id);
        studentMapper.update(request, student);
        attachUser(request.userId(), student);
        Student saved = studentRepository.save(student);
        auditLogService.log("UPDATE STUDENT " + saved.getRollNumber(), null);
        return studentMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Student student = findStudent(id);
        student.setDeleted(true);
        studentRepository.save(student);
        auditLogService.log("DELETE STUDENT " + student.getRollNumber(), null);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse me(String username) {
        Student student = studentRepository.findByUserUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new ResourceNotFoundException("No student profile linked to user: " + username));
        return studentMapper.toResponse(student);
    }

    private Student findStudent(Long id) {
        return studentRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
    }

    private void attachUser(Long userId, Student student) {
        if (userId == null) {
            student.setUser(null);
            return;
        }
        User user = userRepository.findById(userId)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        student.setUser(user);
    }
}
