package com.portal.studentportal.service;

import com.portal.studentportal.dto.attendance.AttendanceResponse;
import com.portal.studentportal.dto.dashboard.BandStatisticResponse;
import com.portal.studentportal.dto.marks.RankingResponse;
import com.portal.studentportal.dto.report.CoursePerformanceResponse;
import com.portal.studentportal.dto.report.DepartmentPerformanceResponse;
import com.portal.studentportal.dto.report.MonthlyEnrollmentResponse;
import com.portal.studentportal.dto.report.PassFailStatisticsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReportService {
    Page<RankingResponse> studentRankings(Pageable pageable);
    Page<AttendanceResponse> attendanceDefaulters(Pageable pageable);
    List<CoursePerformanceResponse> coursePerformance();
    List<DepartmentPerformanceResponse> departmentPerformance();
    PassFailStatisticsResponse passFailStatistics();
    List<BandStatisticResponse> attendanceStatistics();
    List<MonthlyEnrollmentResponse> monthlyEnrollment(int year);
}
