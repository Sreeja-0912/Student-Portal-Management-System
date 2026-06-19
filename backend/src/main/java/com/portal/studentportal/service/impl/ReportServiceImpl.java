package com.portal.studentportal.service.impl;

import com.portal.studentportal.dto.attendance.AttendanceResponse;
import com.portal.studentportal.dto.dashboard.BandStatisticResponse;
import com.portal.studentportal.dto.marks.RankingResponse;
import com.portal.studentportal.dto.report.CoursePerformanceResponse;
import com.portal.studentportal.dto.report.DepartmentPerformanceResponse;
import com.portal.studentportal.dto.report.MonthlyEnrollmentResponse;
import com.portal.studentportal.dto.report.PassFailStatisticsResponse;
import com.portal.studentportal.repository.MarksRepository;
import com.portal.studentportal.repository.StudentCourseRepository;
import com.portal.studentportal.service.AttendanceService;
import com.portal.studentportal.service.DashboardService;
import com.portal.studentportal.service.MarksService;
import com.portal.studentportal.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final MarksService marksService;
    private final AttendanceService attendanceService;
    private final DashboardService dashboardService;
    private final MarksRepository marksRepository;
    private final StudentCourseRepository enrollmentRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<RankingResponse> studentRankings(Pageable pageable) {
        return marksService.rankings(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AttendanceResponse> attendanceDefaulters(Pageable pageable) {
        return attendanceService.defaulters(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoursePerformanceResponse> coursePerformance() {
        return marksRepository.coursePerformance().stream()
                .map(row -> new CoursePerformanceResponse(
                        ((Number) row[0]).longValue(),
                        String.valueOf(row[1]),
                        String.valueOf(row[2]),
                        bd(row[3]),
                        bd(row[4]),
                        bd(row[5])
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentPerformanceResponse> departmentPerformance() {
        return marksRepository.departmentPerformance().stream()
                .map(row -> new DepartmentPerformanceResponse(row[0] == null ? "N/A" : String.valueOf(row[0]), bd(row[1])))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PassFailStatisticsResponse passFailStatistics() {
        long total = marksRepository.countByDeletedFalse();
        long pass = marksRepository.countByScoreGreaterThanEqualAndDeletedFalse(BigDecimal.valueOf(40));
        long fail = total - pass;
        return new PassFailStatisticsResponse(pass, fail, percentage(pass, total), percentage(fail, total));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BandStatisticResponse> attendanceStatistics() {
        return dashboardService.attendanceStatistics();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonthlyEnrollmentResponse> monthlyEnrollment(int year) {
        return enrollmentRepository.monthlyEnrollment(year).stream()
                .map(row -> new MonthlyEnrollmentResponse(((Number) row[0]).intValue(), ((Number) row[1]).longValue()))
                .toList();
    }

    private BigDecimal percentage(long part, long total) {
        return total == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(part).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal bd(Object value) {
        if (value == null) return BigDecimal.ZERO;
        if (value instanceof BigDecimal b) return b.setScale(2, RoundingMode.HALF_UP);
        if (value instanceof Number n) return BigDecimal.valueOf(n.doubleValue()).setScale(2, RoundingMode.HALF_UP);
        return new BigDecimal(value.toString()).setScale(2, RoundingMode.HALF_UP);
    }
}
