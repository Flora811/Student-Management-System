package com.flora.student.service.impl;

import com.flora.student.dto.CourseDTO;
import com.flora.student.dto.EnrollmentDTO;
import com.flora.student.dto.EnrollmentSummaryDTO;
import com.flora.student.dto.StudentDTO;
import com.flora.student.model.Courses;
import com.flora.student.model.Enrollment;
import com.flora.student.model.Students;
import com.flora.student.repository.CourseRepository;
import com.flora.student.repository.EnrollmentRepository;
import com.flora.student.repository.StudentRepository;
import com.flora.student.service.EnrollmentService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final ModelMapper mapper;

    private static final Logger log = LoggerFactory.getLogger(EnrollmentService.class);

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository, CourseRepository courseRepository, StudentRepository studentRepository, ModelMapper mapper) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.mapper = mapper;
    }


    @Override
    public void enrollStudentToCourse(EnrollmentDTO enrollmentDTO) {
        log.info("Enrolling student with id {} to course(s)", enrollmentDTO.getStudentId());

        Students student = studentRepository.findById(enrollmentDTO.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not Found"));

        for(Long courseId : enrollmentDTO.getCourseIds()){
            Courses course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Invalid Course Id"));
            if(enrollmentRepository.existsByStudentIdAndCourseId(enrollmentDTO.getStudentId(), courseId)){
                continue;
            }

            Enrollment enrollment = new Enrollment();
            enrollment.setStudent(student);
            enrollment.setCourse(course);

            student.getEnrollments().add(enrollment);
            course.getEnrollments().add(enrollment);

            enrollmentRepository.save(enrollment);
        }

    }

    @Override
    public Page<EnrollmentSummaryDTO> getEnrolledStudents(int page, int size) {
        log.info("EnrollmentService: get enrolled students in Page<StudentsDTO>");
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return studentRepository.findEnrolledStudents(pageRequest)
                .map(student -> {
                    EnrollmentSummaryDTO dto = new EnrollmentSummaryDTO();
                    dto.setStudentId(student.getId());
                    dto.setStudentName(student.getFirstName() + " " + student.getLastName());
                    dto.setEmail(student.getEmail());
                    dto.setCourseCount(student.getEnrollments().size());
                    BigDecimal totalFees = student.getEnrollments().stream()
                            .map(enrollment -> enrollment.getCourse().getCourseFee())
                            .filter( fee -> fee != null)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    dto.setTotalFees(totalFees);

                    return dto;
                });
    }

    @Override
    public EnrollmentSummaryDTO findEnrolledStudentCourseDetails(Long studentId) {

        log.info("EnrollmentService: get enrolled students course details");

        return studentRepository.findEnrolledStudentCourseDetails(studentId)
                .map(student -> {
                    EnrollmentSummaryDTO dto = new EnrollmentSummaryDTO();
                    dto.setStudentId(student.getId());
                    dto.setStudentName(student.getFirstName() + " " + student.getLastName());
                    dto.setEmail(student.getEmail());
                    dto.setCourseCount(student.getEnrollments().size());
                    BigDecimal totalFees = student.getEnrollments().stream()
                            .map(enrollment -> enrollment.getCourse().getCourseFee())
                            .filter( fee -> fee != null)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    dto.setTotalFees(totalFees);

                    List<CourseDTO> courseList = student.getEnrollments().stream()
                            .map(enrollment -> enrollment.getCourse())
                            .map(course -> mapper.map(course, CourseDTO.class))
                            .collect(Collectors.toList());

                    dto.setCourseList(courseList);

                    return dto;
                })
                .orElseThrow(() -> new RuntimeException("Student not Found"));
    }

    @Override
    public List<EnrollmentSummaryDTO> getRecentlyEnrolledStudents() {
        log.info("EnrollmentService: get recently enrolled students in Page<StudentsDTO>");
        //5 recent students
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id"));
        return studentRepository.findEnrolledStudents(pageRequest)
                .map(student -> {
                    EnrollmentSummaryDTO dto = new EnrollmentSummaryDTO();
                    dto.setStudentId(student.getId());
                    dto.setStudentName(student.getFirstName() + " " + student.getLastName());
                    dto.setEmail(student.getEmail());
                    dto.setCourseCount(student.getEnrollments().size());
                    BigDecimal totalFees = student.getEnrollments().stream()
                            .map(enrollment -> enrollment.getCourse().getCourseFee())
                            .filter( fee -> fee != null)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    dto.setTotalFees(totalFees);

                    return dto;
                }).getContent();
    }
}
