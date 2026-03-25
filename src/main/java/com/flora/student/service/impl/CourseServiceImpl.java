package com.flora.student.service.impl;

import com.flora.student.dto.CourseDTO;
import com.flora.student.model.Courses;
import com.flora.student.repository.CourseRepository;
import com.flora.student.service.CourseService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private static final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);
    private final ModelMapper mapper;

    public CourseServiceImpl(CourseRepository courseRepository, ModelMapper mapper) {
        this.courseRepository = courseRepository;
        this.mapper = mapper;
    }

    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {
        log.info("Creating course with code: {}", courseDTO.getCourseCode());
        Courses courses = mapper.map(courseDTO, Courses.class);
        courseRepository.save(courses);
        return mapper.map(courses, CourseDTO.class);
    }

    @Override
    public boolean existsByCourseCode(String code) {
        log.info("Checking if the code exists: {}", code);
        return courseRepository.existsByCourseCodeIgnoreCase(code);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<CourseDTO> getCourses(int page, int size) {
        log.info("Getting list of courses from page: {}", page);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return courseRepository.findByActiveTrue(pageRequest)
                .map(course -> mapper.map(course, CourseDTO.class));
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public CourseDTO getCourseById(Long id) {
        Courses course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException( String.format("No course with ID: %d found", id) ) );
               return mapper.map(course, CourseDTO.class);
    }

    @Override
    public CourseDTO updateCourseById(Long id, CourseDTO courseDTO) {
        // first of all check if this course is present or not if the id is not unique hibernate will update the data
        // otherwise hibernate will create new data. Hence, we check first
        Courses course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException( String.format("No course with ID: %d found", id) ) );

        //new data is in courseDTO and existing data is in course - so we will map courseDTO values (source) to course(destination) so that
        // it will keep the existing data that we are not providing from courseDTO.
        mapper.map(courseDTO, course);

        Courses updated = courseRepository.save(course);

        return mapper.map(updated, CourseDTO.class);
    }

    @Override
    public boolean existsByCourseCodeAndIdNot(String code, Long id) {
        log.info("Checking if the code ({}) exists where id is not equal to the given/current id: {}", code, id);
        return courseRepository.existsByCourseCodeIgnoreCaseAndIdNot(code, id);
    }

    @Override
    public List<CourseDTO> getALlCourses() {
        return courseRepository.findByActiveTrue(Sort.by("courseName"))
                .stream()
                .map(courses -> mapper.map(courses, CourseDTO.class))
                .collect(Collectors.toList());
    }
}
