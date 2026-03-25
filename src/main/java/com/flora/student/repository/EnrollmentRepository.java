package com.flora.student.repository;

import com.flora.student.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByStudentIdAndCourseId(Long studentId, Long CourseId);

    @Query("""
        SELECT COUNT (DISTINCT e.student.id)
        FROM Enrollment e
        WHERE e.enrolledDate BETWEEN :startDate AND :endDate
""")
    long countDistinctStudentByEnrollDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate")LocalDateTime endDate);

}
