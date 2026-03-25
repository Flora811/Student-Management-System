package com.flora.student.service.impl;

import com.flora.student.dto.DashboardInfoDTO;
import com.flora.student.repository.CourseRepository;
import com.flora.student.repository.EnrollmentRepository;
import com.flora.student.repository.StudentRepository;
import com.flora.student.service.DashboardService;
import com.flora.student.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    StudentRepository studentRepository;
    CourseRepository courseRepository;
    EnrollmentRepository enrollmentRepository;
    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    DashboardServiceImpl(StudentRepository studentRepository, CourseRepository courseRepository, EnrollmentRepository enrollmentRepository){
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public DashboardInfoDTO getDashboardStats() {
        long totalStudents = studentRepository.count();
        long totalCourses = courseRepository.count();
        String topPerformingCourse = getTopPerformingCourse();
        YearMonth currMonth = YearMonth.now();
        LocalDateTime startMonth = currMonth.atDay(1).atStartOfDay();
        LocalDateTime endMonth = currMonth.atEndOfMonth().atTime(LocalTime.MAX);
        long studentsEnrolledThisMonth = enrollmentRepository.countDistinctStudentByEnrollDateBetween(startMonth, endMonth);

        DashboardInfoDTO dashboardInfoDTO = new DashboardInfoDTO();
        dashboardInfoDTO.setTotalStudents(totalStudents);
        dashboardInfoDTO.setTotalCourses(totalCourses);
        dashboardInfoDTO.setTopPerformingCourse(topPerformingCourse);
        dashboardInfoDTO.setStudentsEnrolledThisMonth(studentsEnrolledThisMonth);
        return dashboardInfoDTO;
    }

    private String getTopPerformingCourse(){
        return enrollmentRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(e -> e.getCourse().getCourseCode(), Collectors.counting()))
                .entrySet()
                .stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse("N/A");
    }
}
