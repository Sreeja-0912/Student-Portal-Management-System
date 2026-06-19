package com.portal.studentportal.service.impl;

import com.portal.studentportal.dto.marks.MarksRequest;
import com.portal.studentportal.dto.marks.MarksResponse;
import com.portal.studentportal.dto.marks.RankingResponse;
import com.portal.studentportal.entity.Course;
import com.portal.studentportal.entity.Marks;
import com.portal.studentportal.entity.Student;
import com.portal.studentportal.exception.ConflictException;
import com.portal.studentportal.exception.ResourceNotFoundException;
import com.portal.studentportal.repository.CourseRepository;
import com.portal.studentportal.repository.MarksRepository;
import com.portal.studentportal.repository.StudentRepository;
import com.portal.studentportal.service.AuditLogService;
import com.portal.studentportal.service.MarksService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class MarksServiceImpl implements MarksService {
    private static final BigDecimal PASS_SCORE = BigDecimal.valueOf(40);

    private final MarksRepository marksRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final AuditLogService auditLogService;

    @Override
    @Transactional
    public MarksResponse record(MarksRequest request) {
        if (marksRepository.findByStudentIdAndCourseIdAndDeletedFalse(request.studentId(), request.courseId()).isPresent()) {
            throw new ConflictException("Marks already recorded for this student and course");
        }
        Marks marks = Marks.builder()
                .student(student(request.studentId()))
                .course(course(request.courseId()))
                .score(request.score())
                .build();
        Marks saved = marksRepository.save(marks);
        auditLogService.log("RECORD MARKS " + saved.getId(), null);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public MarksResponse update(Long id, MarksRequest request) {
        Marks marks = find(id);
        Student student = student(request.studentId());
        Course course = course(request.courseId());
        marksRepository.findByStudentIdAndCourseIdAndDeletedFalse(request.studentId(), request.courseId())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> { throw new ConflictException("Another marks record already exists for this student and course"); });
        marks.setStudent(student);
        marks.setCourse(course);
        marks.setScore(request.score());
        Marks saved = marksRepository.save(marks);
        auditLogService.log("UPDATE MARKS " + saved.getId(), null);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Marks marks = find(id);
        marks.setDeleted(true);
        marksRepository.save(marks);
        auditLogService.log("DELETE MARKS " + id, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarksResponse> byStudent(Long studentId) {
        student(studentId);
        return marksRepository.findByStudentIdAndDeletedFalse(studentId).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RankingResponse> rankings(Pageable pageable) {
        Page<Object[]> page = marksRepository.studentRankings(pageable);
        AtomicInteger rank = new AtomicInteger(page.getNumber() * page.getSize() + 1);
        return page.map(row -> new RankingResponse(
                rank.getAndIncrement(),
                ((Number) row[0]).longValue(),
                String.valueOf(row[1]).trim(),
                String.valueOf(row[2]),
                row[3] == null ? "N/A" : String.valueOf(row[3]),
                bd(row[4]),
                grade(bd(row[4]))
        ));
    }

    public String grade(BigDecimal score) {
        if (score.compareTo(BigDecimal.valueOf(90)) >= 0) return "A+";
        if (score.compareTo(BigDecimal.valueOf(80)) >= 0) return "A";
        if (score.compareTo(BigDecimal.valueOf(70)) >= 0) return "B+";
        if (score.compareTo(BigDecimal.valueOf(60)) >= 0) return "B";
        if (score.compareTo(BigDecimal.valueOf(50)) >= 0) return "C";
        if (score.compareTo(BigDecimal.valueOf(40)) >= 0) return "D";
        return "F";
    }

    private Marks find(Long id) {
        return marksRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marks not found with id " + id));
    }

    private Student student(Long id) {
        return studentRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
    }

    private Course course(Long id) {
        return courseRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + id));
    }

    private MarksResponse toResponse(Marks marks) {
        Student s = marks.getStudent();
        Course c = marks.getCourse();
        String name = (s.getFirstName() + " " + (s.getLastName() == null ? "" : s.getLastName())).trim();
        return new MarksResponse(marks.getId(), s.getId(), name, c.getId(), c.getCourseCode(), c.getCourseName(), marks.getScore(), grade(marks.getScore()), marks.getScore().compareTo(PASS_SCORE) >= 0 ? "PASS" : "FAIL");
    }

    private BigDecimal bd(Object value) {
        if (value == null) return BigDecimal.ZERO;
        if (value instanceof BigDecimal b) return b.setScale(2, RoundingMode.HALF_UP);
        if (value instanceof Number n) return BigDecimal.valueOf(n.doubleValue()).setScale(2, RoundingMode.HALF_UP);
        return new BigDecimal(value.toString()).setScale(2, RoundingMode.HALF_UP);
    }
}
