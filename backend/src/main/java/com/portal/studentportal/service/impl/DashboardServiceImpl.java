package com.portal.studentportal.service.impl;

import com.portal.studentportal.dto.dashboard.AtRiskStudentResponse;
import com.portal.studentportal.dto.dashboard.BandStatisticResponse;
import com.portal.studentportal.dto.dashboard.SummaryResponse;
import com.portal.studentportal.dto.marks.RankingResponse;
import com.portal.studentportal.entity.Attendance;
import com.portal.studentportal.entity.Marks;
import com.portal.studentportal.entity.Student;
import com.portal.studentportal.repository.AttendanceRepository;
import com.portal.studentportal.repository.CourseRepository;
import com.portal.studentportal.repository.MarksRepository;
import com.portal.studentportal.repository.StudentRepository;
import com.portal.studentportal.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final AttendanceRepository attendanceRepository;
    private final MarksRepository marksRepository;

    @Override
    @Transactional(readOnly = true)
    public SummaryResponse summary() {
        long totalMarks = marksRepository.countByDeletedFalse();
        long pass = marksRepository.countByScoreGreaterThanEqualAndDeletedFalse(BigDecimal.valueOf(40));
        BigDecimal passPercentage = percentage(pass, totalMarks);
        return new SummaryResponse(
                studentRepository.countByDeletedFalse(),
                courseRepository.countByDeletedFalse(),
                BigDecimal.valueOf(attendanceRepository.averageAttendance()).setScale(2, RoundingMode.HALF_UP),
                passPercentage
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<RankingResponse> topStudents(int limit) {
        List<RankingResponse> rows = new ArrayList<>();
        var page = marksRepository.studentRankings(PageRequest.of(0, Math.max(1, limit)));
        int rank = 1;
        for (Object[] row : page.getContent()) {
            BigDecimal avg = bd(row[4]);
            rows.add(new RankingResponse(rank++, ((Number) row[0]).longValue(), String.valueOf(row[1]).trim(), String.valueOf(row[2]), row[3] == null ? "N/A" : String.valueOf(row[3]), avg, grade(avg)));
        }
        return rows;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BandStatisticResponse> attendanceStatistics() {
        return List.of(
                new BandStatisticResponse("Excellent >= 90%", attendanceRepository.countExcellent()),
                new BandStatisticResponse("Good 75% to 89%", attendanceRepository.countGood()),
                new BandStatisticResponse("Average 50% to 74%", attendanceRepository.countAverage()),
                new BandStatisticResponse("Poor < 50%", attendanceRepository.countPoor())
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtRiskStudentResponse> atRiskStudents() {
        List<AtRiskStudentResponse> result = new ArrayList<>();
        for (Student student : studentRepository.search("", PageRequest.of(0, 1000)).getContent()) {
            BigDecimal attendanceAvg = avgAttendance(student.getId());
            BigDecimal scoreAvg = avgMarks(student.getId());
            boolean lowAttendance = attendanceAvg.compareTo(BigDecimal.valueOf(75)) < 0;
            boolean lowMarks = scoreAvg.compareTo(BigDecimal.valueOf(40)) < 0;
            if (lowAttendance || lowMarks) {
                String reason = lowAttendance && lowMarks ? "Low attendance and low marks" : lowAttendance ? "Low attendance" : "Low marks";
                String name = (student.getFirstName() + " " + (student.getLastName() == null ? "" : student.getLastName())).trim();
                result.add(new AtRiskStudentResponse(student.getId(), name, student.getRollNumber(), attendanceAvg, scoreAvg, reason));
            }
        }
        return result;
    }

    private BigDecimal avgAttendance(Long studentId) {
        List<Attendance> rows = attendanceRepository.findByStudentIdAndDeletedFalse(studentId);
        if (rows.isEmpty()) return BigDecimal.ZERO;
        BigDecimal total = rows.stream().map(Attendance::getAttendancePercentage).reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.divide(BigDecimal.valueOf(rows.size()), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal avgMarks(Long studentId) {
        List<Marks> rows = marksRepository.findByStudentIdAndDeletedFalse(studentId);
        if (rows.isEmpty()) return BigDecimal.ZERO;
        BigDecimal total = rows.stream().map(Marks::getScore).reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.divide(BigDecimal.valueOf(rows.size()), 2, RoundingMode.HALF_UP);
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

    private String grade(BigDecimal score) {
        if (score.compareTo(BigDecimal.valueOf(90)) >= 0) return "A+";
        if (score.compareTo(BigDecimal.valueOf(80)) >= 0) return "A";
        if (score.compareTo(BigDecimal.valueOf(70)) >= 0) return "B+";
        if (score.compareTo(BigDecimal.valueOf(60)) >= 0) return "B";
        if (score.compareTo(BigDecimal.valueOf(50)) >= 0) return "C";
        if (score.compareTo(BigDecimal.valueOf(40)) >= 0) return "D";
        return "F";
    }
}
