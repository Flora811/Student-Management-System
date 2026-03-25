package com.flora.student.service;

import com.flora.student.dto.EnrollmentSummaryDTO;
import com.flora.student.dto.StudentDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StudentService {

    StudentDTO createStudent(StudentDTO studentDTO);

    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    Page<StudentDTO> getStudents(int page, int size);

    StudentDTO getStudentById(Long id);

    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);

    StudentDTO updateStudentById(Long id, StudentDTO studentDTO);

    List<StudentDTO> getALlStudents();

}
