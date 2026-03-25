package com.flora.student.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class CourseDTO {
    private Long id;

    @NotBlank(message = "Course Name is required.")
    @Size(max = 150, message = "Max 150 characters allowed.")
    private String courseName;

    @NotBlank(message = "Course Code is required.")
    private String courseCode;

    @NotBlank(message = "Course Duration is required.")
    private String duration;

    @NotNull(message = "Course fee is required")
    private BigDecimal courseFee;

    @Size(max = 500, message = "Max 500 characters allowed.")
    private String description;

    private boolean active = true;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public BigDecimal getCourseFee() {
        return courseFee;
    }

    public void setCourseFee(BigDecimal courseFee) {
        this.courseFee = courseFee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
