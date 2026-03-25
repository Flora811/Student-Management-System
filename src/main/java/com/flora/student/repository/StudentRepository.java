package com.flora.student.repository;

import com.flora.student.model.Students;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Students, Long> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);

    Page<Students> findByisActiveTrue(Pageable pageable);

    List<Students> findByisActiveTrue();

    @EntityGraph(attributePaths = {"enrollments", "enrollments.course"})
    @Query( value = """
        SELECT DISTINCT s
        FROM Students s
        JOIN s.enrollments e""",
            countQuery = """
                    SELECT count(DISTINCT s)
                    FROM Students s
                    JOIN s.enrollments e
                    """)
    Page<Students> findEnrolledStudents(Pageable pageable);

    @Query("""
        SELECT s
        FROM Students s
        JOIN FETCH s.enrollments e
        JOIN FETCH e.course c
        WHERE s.id = :id
        """)
    Optional<Students> findEnrolledStudentCourseDetails(@Param("id") Long id);

}
