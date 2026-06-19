package com.portal.studentportal.service;

import com.portal.studentportal.dto.attendance.AttendanceRequest;
import com.portal.studentportal.dto.attendance.AttendanceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AttendanceService {
    AttendanceResponse record(AttendanceRequest request);
    AttendanceResponse update(Long id, AttendanceRequest request);
    void delete(Long id);
    List<AttendanceResponse> byStudent(Long studentId);
    Page<AttendanceResponse> defaulters(Pageable pageable);
}
