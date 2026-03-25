package com.flora.student.controller;

import com.flora.student.dto.CourseDTO;
import com.flora.student.dto.StudentDTO;
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
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private static final Logger log = LoggerFactory.getLogger(StudentController.class);

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/list")
    public String listStudent(@RequestParam(defaultValue = "0")int page,
                              @RequestParam(defaultValue = "3") int size,
                              Model model){
        Page<StudentDTO> students = studentService.getStudents(page, size);
        model.addAttribute("students", students);
        return "students";
    }

    @GetMapping("/new")
    public String showCreateStudent(Model model){
        log.info("GET /student/new - showing create student page");
        model.addAttribute("studentDto", new StudentDTO());
        return "add-student";
    }

    @PostMapping
    public String createStudent(@Valid @ModelAttribute("studentDto")StudentDTO studentDTO,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes){
        log.info("POST /student - Create Student Request Received.");

        if(bindingResult.hasErrors()){
            log.error("POST /student - Page returned due to validation error.");
            return "add-student";
        }

        if(studentService.existsByEmail(studentDTO.getEmail())){
            log.error("POST /student - email must be unique.");
            bindingResult.rejectValue("email", "email.unique", "Email must be unique");
            return "add-student";
        }

        if(studentService.existsByPhoneNumber(studentDTO.getPhoneNumber())){
            log.error("POST /student - Phone Number must be unique.");
            bindingResult.rejectValue("phoneNumber", "phoneNumber.unique", "Phone Number must be unique");
            return "add-student";
        }

        studentService.createStudent(studentDTO);
        redirectAttributes.addFlashAttribute("message", "Student is created Successfully !!");
        log.info("POST /student - Student is Created Successfully.");

        return"redirect:/student/list";
    }

    @GetMapping("/{id}")
    public String getStudentById(@PathVariable Long id,
                                 Model model){
        StudentDTO student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        return "view-student";
    }

    @GetMapping("/{id}/edit")
    public String editStudent(@PathVariable Long id,
                             Model model){
        StudentDTO studentDTO = studentService.getStudentById(id);
        model.addAttribute("studentDto", studentDTO);
        return "edit-student";
    }

    @PostMapping("/{id}/update")
    public String updateStudent(@PathVariable Long id,
                                @Valid @ModelAttribute("studentDto")StudentDTO studentDTO,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes){

        log.info("POST /student - Update Student Request Received.");

        if(bindingResult.hasErrors()){
            log.error("POST /student/{}/update - Page returned due to validation error.", id);
            return "edit-student";
        }

        if(studentService.existsByEmailAndIdNot(studentDTO.getEmail(), id)){
            log.error("POST /student/{}/update - email must be unique.", id);
            bindingResult.rejectValue("email", "email.unique", "Email must be unique");
            return "edit-student";
        }

        if(studentService.existsByPhoneNumberAndIdNot(studentDTO.getPhoneNumber(), id)){
            log.error("POST /student/{}/update - Phone Number must be unique.", id);
            bindingResult.rejectValue("phoneNumber", "phoneNumber.unique", "Phone Number must be unique");
            return "edit-student";
        }

        studentService.updateStudentById(id, studentDTO);
        redirectAttributes.addFlashAttribute("message", "Student is Updated Successfully !!");
        log.info("POST /student/{}/update - Student is Updated Successfully.", id);

        return"redirect:/student/list";
    }

}
