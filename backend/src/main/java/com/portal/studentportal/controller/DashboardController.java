package com.portal.studentportal.controller;

import com.portal.studentportal.dto.dashboard.AtRiskStudentResponse;
import com.portal.studentportal.dto.attendance.AttendanceResponse;
import com.portal.studentportal.dto.dashboard.SummaryResponse;
import com.portal.studentportal.dto.marks.RankingResponse;
import com.portal.studentportal.service.AttendanceService;
import com.portal.studentportal.service.DashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
@Tag(name = "Dashboard")
public class DashboardController {
    private final DashboardService dashboardService;
    private final AttendanceService attendanceService;

    @GetMapping("/summary")
    public SummaryResponse summary() {
        return dashboardService.summary();
    }

    @GetMapping("/top-students")
    public List<RankingResponse> topStudents(@RequestParam(defaultValue = "5") int limit) {
        return dashboardService.topStudents(limit);
    }

    @GetMapping("/low-attendance")
    public List<AttendanceResponse> lowAttendance() {
        return attendanceService.defaulters(org.springframework.data.domain.PageRequest.of(0, 20)).getContent();
    }

    @GetMapping("/at-risk-students")
    public List<AtRiskStudentResponse> atRiskStudents() {
        return dashboardService.atRiskStudents();
    }
}
