package com.portal.studentportal.repository;

import com.portal.studentportal.entity.Marks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface MarksRepository extends JpaRepository<Marks, Long> {
    long countByDeletedFalse();
    long countByScoreGreaterThanEqualAndDeletedFalse(BigDecimal score);
    Optional<Marks> findByIdAndDeletedFalse(Long id);
    Optional<Marks> findByStudentIdAndCourseIdAndDeletedFalse(Long studentId, Long courseId);
    List<Marks> findByStudentIdAndDeletedFalse(Long studentId);

    @Query(value = """
           select m.student.id, concat(m.student.firstName, ' ', coalesce(m.student.lastName, '')), m.student.rollNumber,
                  m.student.department, avg(m.score)
           from Marks m
           where m.deleted = false
           group by m.student.id, m.student.firstName, m.student.lastName, m.student.rollNumber, m.student.department
           order by avg(m.score) desc
           """,
           countQuery = "select count(distinct m.student.id) from Marks m where m.deleted = false")
    Page<Object[]> studentRankings(Pageable pageable);

    @Query("""
           select m.course.id, m.course.courseCode, m.course.courseName, avg(m.score), max(m.score), min(m.score)
           from Marks m
           where m.deleted = false
           group by m.course.id, m.course.courseCode, m.course.courseName
           order by m.course.courseCode
           """)
    List<Object[]> coursePerformance();

    @Query("""
           select m.student.department, avg(m.score)
           from Marks m
           where m.deleted = false
           group by m.student.department
           order by avg(m.score) desc
           """)
    List<Object[]> departmentPerformance();
}
