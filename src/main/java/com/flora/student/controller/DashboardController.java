package com.flora.student.controller;

import com.flora.student.service.CourseService;
import com.flora.student.service.DashboardService;
import com.flora.student.service.EnrollmentService;
import com.flora.student.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    private final EnrollmentService enrollmentService;
    private final CourseService courseService;
    private final StudentService studentService;
    private final DashboardService dashboardService;

    private static final Logger log = LoggerFactory.getLogger(StudentController.class);

    public DashboardController (EnrollmentService enrollmentService, CourseService courseService, StudentService studentService, DashboardService dashboardService){
        this.enrollmentService = enrollmentService;
        this.courseService = courseService;
        this.studentService = studentService;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model){
        model.addAttribute("dashboardStats", dashboardService.getDashboardStats());
        model.addAttribute("students", enrollmentService.getRecentlyEnrolledStudents());
        return "dashboard";
    }
}
