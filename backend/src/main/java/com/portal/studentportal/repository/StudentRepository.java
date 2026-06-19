package com.portal.studentportal.repository;

import com.portal.studentportal.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    long countByDeletedFalse();
    Optional<Student> findByIdAndDeletedFalse(Long id);
    Optional<Student> findByUserUsernameAndDeletedFalse(String username);
    boolean existsByRollNumberAndDeletedFalse(String rollNumber);
    boolean existsByEmailAndDeletedFalse(String email);

    @Query("""
            select s from Student s where s.deleted = false and
            (:keyword is null or :keyword = '' or
             lower(s.rollNumber) like lower(concat('%', :keyword, '%')) or
             lower(s.firstName) like lower(concat('%', :keyword, '%')) or
             lower(coalesce(s.lastName, '')) like lower(concat('%', :keyword, '%')) or
             lower(s.email) like lower(concat('%', :keyword, '%')) or
             lower(coalesce(s.department, '')) like lower(concat('%', :keyword, '%')))
            """)
    Page<Student> search(@Param("keyword") String keyword, Pageable pageable);
}
