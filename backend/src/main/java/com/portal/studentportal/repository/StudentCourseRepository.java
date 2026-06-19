package com.portal.studentportal.repository;

import com.portal.studentportal.entity.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
    boolean existsByStudentIdAndCourseIdAndDeletedFalse(Long studentId, Long courseId);
    Optional<StudentCourse> findByStudentIdAndCourseIdAndDeletedFalse(Long studentId, Long courseId);
    List<StudentCourse> findByStudentIdAndDeletedFalse(Long studentId);
    List<StudentCourse> findByCourseIdAndDeletedFalse(Long courseId);

    @Query("""
           select month(sc.createdDate), count(sc) from StudentCourse sc
           where sc.deleted = false and year(sc.createdDate) = :year
           group by month(sc.createdDate)
           order by month(sc.createdDate)
           """)
    List<Object[]> monthlyEnrollment(@Param("year") int year);
}
