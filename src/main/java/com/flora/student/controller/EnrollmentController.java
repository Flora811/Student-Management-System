package com.flora.student.controller;

import com.flora.student.dto.EnrollmentDTO;
import com.flora.student.dto.EnrollmentSummaryDTO;
import com.flora.student.dto.StudentDTO;
import com.flora.student.service.CourseService;
import com.flora.student.service.EnrollmentService;
import com.flora.student.service.StudentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    private final CourseService courseService;

    private final StudentService studentService;

    private static final Logger log = LoggerFactory.getLogger(CourseController.class);

    public EnrollmentController(EnrollmentService enrollmentService,
                                CourseService courseService,
                                StudentService studentService) {
        this.enrollmentService = enrollmentService;
        this.courseService = courseService;
        this.studentService = studentService;
    }

    @GetMapping("/showEnroll")
    public String showEnroll(Model model){
        log.info("GET /enrollments/showEnroll - showing enrollment page");
        model.addAttribute("enrollmentDto", new EnrollmentDTO());
        model.addAttribute("courseList", courseService.getALlCourses());
        model.addAttribute("studentList", studentService.getALlStudents());
        return "enroll-course";
    }

    @PostMapping("/enrollCourse")
    public String enrollStudentToCourses(@Valid @ModelAttribute("enrollmentDto") EnrollmentDTO enrollmentDTO,
                                         BindingResult bindingResult,
                                         Model model,
                                         RedirectAttributes redirectAttributes){
        log.info("POST /enrollments/enrollCourse - Enroll Student to Course Request Received");

        if(bindingResult.hasErrors()){
            log.error("POST /enrollments/enrollCourse - Page returned due to validation error.");
            model.addAttribute("courseList", courseService.getALlCourses());
            model.addAttribute("studentList", studentService.getALlStudents());
            return "enroll-course";
        }

        enrollmentService.enrollStudentToCourse(enrollmentDTO);
        redirectAttributes.addFlashAttribute("message", "Student is enrolled Successfully !!");
        log.info("POST /enrollments/enrollCourse - Student is Enrolled Successfully.");

        return"redirect:/enrollments/enrollmentList";
    }

    @GetMapping("/enrollmentList")
    public String enrollmentList(@RequestParam(defaultValue = "0")int page,
                                 @RequestParam(defaultValue = "3") int size,
                                 Model model){
        log.info("GET /enrollments/enrollmentList - showing enrollment List");

        Page<EnrollmentSummaryDTO> student = enrollmentService.getEnrolledStudents(page, size);
        model.addAttribute("students", student);

        return "enrolled-students";
    }

    @GetMapping("/enrollmentDetails/{id}")
    public String enrollmentDetails( @PathVariable Long id,
                                    Model model,
                                     @RequestParam(defaultValue = "enrollments") String source){
        EnrollmentSummaryDTO details = enrollmentService.findEnrolledStudentCourseDetails(id);
        model.addAttribute("details", details);
        model.addAttribute("source", source);
        return "enrollment-details";
    }
}
