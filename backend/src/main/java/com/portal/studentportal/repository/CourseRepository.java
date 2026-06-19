package com.portal.studentportal.repository;

import com.portal.studentportal.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    long countByDeletedFalse();
    Optional<Course> findByIdAndDeletedFalse(Long id);
    boolean existsByCourseCodeAndDeletedFalse(String courseCode);

    @Query("""
            select c from Course c where c.deleted = false and
            (:keyword is null or :keyword = '' or
             lower(c.courseCode) like lower(concat('%', :keyword, '%')) or
             lower(c.courseName) like lower(concat('%', :keyword, '%')) or
             lower(coalesce(c.facultyName, '')) like lower(concat('%', :keyword, '%')))
            """)
    Page<Course> search(@Param("keyword") String keyword, Pageable pageable);
}
