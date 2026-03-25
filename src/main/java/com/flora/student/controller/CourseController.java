package com.flora.student.controller;

import com.flora.student.dto.CourseDTO;
import com.flora.student.service.CourseService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;
    private static final Logger log = LoggerFactory.getLogger(CourseController.class);

    CourseController(CourseService courseService){
        this.courseService = courseService;
    }

    @GetMapping("/new")
    public String showCreateCourse(Model model){
        log.info("GET /course/new - showing create course page");
        model.addAttribute("courseDto", new CourseDTO());
        return "add-course";
    }

    @GetMapping("/list")
    public String listCourse(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "3") int size,
                             Model model){
        Page<CourseDTO> courses = courseService.getCourses(page, size);
        model.addAttribute("courses", courses);
        return "courses";
    }

    @PostMapping
    public String createCourse(@Valid @ModelAttribute("courseDto") CourseDTO courseDTO,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes){
        log.info("POST /course - Create Course Request Received.");
        if(bindingResult.hasErrors()){
            log.error("POST /course - Page returned due to validation error.");
            return "add-course";
        }
        if(courseService.existsByCourseCode(courseDTO.getCourseCode())){
            log.error("POST /course - Code must be unique.");
            bindingResult.rejectValue("courseCode", "courseCode.unique", "Course Code must be unique");
            return "add-course";
        }

        courseService.createCourse(courseDTO);
        redirectAttributes.addFlashAttribute("message", "Course is created Successfully !!");
        log.info("POST /course - Course Created Successfully.");

        return"redirect:/course/list";
    }

    @GetMapping("/{id}")
    public String getCourseById(@PathVariable Long id,
                                 Model model){
        CourseDTO course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        return "view-course";
    }

    @GetMapping("/{id}/edit")
    public String editCourse(@PathVariable Long id,
                                Model model){
        CourseDTO course = courseService.getCourseById(id);
        model.addAttribute("courseDto", course);
        return "edit-course";
    }

    @PostMapping("/{id}/update")
    public String updateCourse(@PathVariable Long id,
                               @Valid @ModelAttribute("courseDto") CourseDTO courseDTO,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes){
        log.info(String.format("POST /course/%d/update - Update course request received",id));

        if(bindingResult.hasErrors()){
            log.error(String.format("POST /course/%d/update - Page returned due to validation error.",id));
            return "edit-course";
        }

        if(courseService.existsByCourseCodeAndIdNot(courseDTO.getCourseCode(), id)){
            log.error(String.format("POST /course/%d/update - Code must be unique.",id));
            bindingResult.rejectValue("courseCode", "courseCode.unique", "Course Code must be unique");
            return "edit-course";
        }

        courseService.updateCourseById(id, courseDTO);
        redirectAttributes.addFlashAttribute("message", "Course is Updated Successfully!! ");
        log.info(String.format("POST /course/%d/update - Course Updated Successfully!! ",id));

        return"redirect:/course/list";
    }


}
