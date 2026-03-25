package com.flora.student.service.impl;

import com.flora.student.dto.EnrollmentSummaryDTO;
import com.flora.student.dto.StudentDTO;
import com.flora.student.model.Students;
import com.flora.student.repository.StudentRepository;
import com.flora.student.service.StudentService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);
    private final ModelMapper mapper;

    StudentServiceImpl(StudentRepository studentRepository, ModelMapper mapper){
        this.studentRepository = studentRepository;
        this.mapper = mapper;
    }

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        log.info("Creating student with id: {}", studentDTO.getId());
        Students student = mapper.map(studentDTO, Students.class);
        studentRepository.save(student);
        return mapper.map(student, StudentDTO.class);
    }

    @Override
    public boolean existsByEmail(String email) {
        log.info("Checking if the email ({}) exists", email);
        return studentRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        log.info("Checking if the phone number ({}) exists", phoneNumber);
        return studentRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public Page<StudentDTO> getStudents(int page, int size) {
        log.info("Service: get students in Page<StudentsDTO>");
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return studentRepository.findByisActiveTrue(pageRequest)
                .map(student -> mapper.map(student, StudentDTO.class));
    }

    @Override
    public StudentDTO getStudentById(Long id) {
        log.info("Service: get student by ID ({})", id);
        Students student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException( String.format("No Student with ID: %d found", id) ) );
        return mapper.map(student, StudentDTO.class);
    }

    @Override
    public boolean existsByEmailAndIdNot(String email, Long id) {
        log.info("Checking if the email ({}) exists in Id other than {}", email, id);
        return studentRepository.existsByEmailIgnoreCaseAndIdNot(email, id);
    }

    @Override
    public boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id) {
        log.info("Checking if the phone number ({}) exists in Id other than {}", phoneNumber, id);
        return studentRepository.existsByPhoneNumberAndIdNot(phoneNumber, id);
    }

    @Override
    public StudentDTO updateStudentById(Long id, StudentDTO studentDTO) {

        log.info("Update Student by Id {}", id);

        Students student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException( String.format("No Student with ID: %d found", id) ) );

        mapper.map(studentDTO, student);

        Students updated = studentRepository.save(student);

        return mapper.map(updated, StudentDTO.class);
    }

    @Override
    public List<StudentDTO> getALlStudents() {
        return studentRepository.findByisActiveTrue()
                .stream()
                .map(students -> mapper.map(students, StudentDTO.class))
                .collect(Collectors.toList());
    }

}
