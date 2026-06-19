package com.portal.studentportal.repository;

import com.portal.studentportal.entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByIdAndDeletedFalse(Long id);
    Optional<Attendance> findByStudentIdAndCourseIdAndDeletedFalse(Long studentId, Long courseId);
    List<Attendance> findByStudentIdAndDeletedFalse(Long studentId);
    Page<Attendance> findByAttendancePercentageLessThanAndDeletedFalse(BigDecimal threshold, Pageable pageable);

    @Query("select coalesce(avg(a.attendancePercentage), 0) from Attendance a where a.deleted = false")
    Double averageAttendance();

    @Query("select count(a) from Attendance a where a.deleted = false and a.attendancePercentage >= 90")
    long countExcellent();

    @Query("select count(a) from Attendance a where a.deleted = false and a.attendancePercentage >= 75 and a.attendancePercentage < 90")
    long countGood();

    @Query("select count(a) from Attendance a where a.deleted = false and a.attendancePercentage >= 50 and a.attendancePercentage < 75")
    long countAverage();

    @Query("select count(a) from Attendance a where a.deleted = false and a.attendancePercentage < 50")
    long countPoor();
}
