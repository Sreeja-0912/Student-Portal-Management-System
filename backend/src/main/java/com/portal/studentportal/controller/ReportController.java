package com.portal.studentportal.controller;

import com.portal.studentportal.dto.attendance.AttendanceResponse;
import com.portal.studentportal.dto.dashboard.BandStatisticResponse;
import com.portal.studentportal.dto.marks.RankingResponse;
import com.portal.studentportal.dto.report.CoursePerformanceResponse;
import com.portal.studentportal.dto.report.DepartmentPerformanceResponse;
import com.portal.studentportal.dto.report.MonthlyEnrollmentResponse;
import com.portal.studentportal.dto.report.PassFailStatisticsResponse;
import com.portal.studentportal.service.ReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Year;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
@Tag(name = "Reports")
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/student-rankings")
    public Page<RankingResponse> studentRankings(@PageableDefault(size = 10) Pageable pageable) {
        return reportService.studentRankings(pageable);
    }

    @GetMapping("/attendance-defaulters")
    public Page<AttendanceResponse> attendanceDefaulters(@PageableDefault(size = 10) Pageable pageable) {
        return reportService.attendanceDefaulters(pageable);
    }

    @GetMapping("/course-performance")
    public List<CoursePerformanceResponse> coursePerformance() {
        return reportService.coursePerformance();
    }

    @GetMapping("/department-performance")
    public List<DepartmentPerformanceResponse> departmentPerformance() {
        return reportService.departmentPerformance();
    }

    @GetMapping("/pass-fail-statistics")
    public PassFailStatisticsResponse passFailStatistics() {
        return reportService.passFailStatistics();
    }

    @GetMapping("/attendance-statistics")
    public List<BandStatisticResponse> attendanceStatistics() {
        return reportService.attendanceStatistics();
    }

    @GetMapping("/monthly-enrollment")
    public List<MonthlyEnrollmentResponse> monthlyEnrollment(@RequestParam(defaultValue = "0") int year) {
        return reportService.monthlyEnrollment(year == 0 ? Year.now().getValue() : year);
    }
}
