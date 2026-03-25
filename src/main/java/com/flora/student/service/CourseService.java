package com.flora.student.service;

import com.flora.student.dto.CourseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CourseService {

    CourseDTO createCourse(CourseDTO courseDTO);

    boolean existsByCourseCode(String code);

    Page<CourseDTO> getCourses(int page, int size);

    CourseDTO getCourseById(Long id);

    CourseDTO updateCourseById(Long id, CourseDTO courseDTO);

    boolean existsByCourseCodeAndIdNot(String code, Long id);

    List<CourseDTO> getALlCourses();
}
