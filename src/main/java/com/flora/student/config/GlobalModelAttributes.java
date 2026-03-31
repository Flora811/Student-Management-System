package com.flora.student.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {
    @ModelAttribute
    public void addActivePage(Model model, HttpServletRequest request){
        String uri = request.getRequestURI();


        if (uri.startsWith("/course")) {
            model.addAttribute("activePage", "courses");
        } else if (uri.startsWith("/student")) {
            model.addAttribute("activePage", "students");
        } else if (uri.startsWith("/enrollments/showEnroll")) {
            model.addAttribute("activePage", "enroll");
        } else if (uri.startsWith("/enrollments/enrollmentList")) {
            model.addAttribute("activePage", "enrolledStudents");
        } else if (uri.startsWith("/dashboard")) {
            model.addAttribute("activePage", "dashboard");
        }
    }
}
