package com.flora.student.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class StudentDTO {

    private Long id;

    @NotBlank(message = "First Name is required.")
    @Size(max = 150, message = "Max 150 characters allowed")
    private String firstName;

    @NotBlank(message = "Last Name is required.")
    @Size(max = 150, message = "Max 150 characters allowed")
    private String lastName;

    @NotBlank(message = "Phone Number is required.")
    @Size(min = 10, max = 13, message = "Phone number must be between 10 and 13 digits")
    @Pattern(
            regexp = "^\\+?[0-9]{10,13}$",
            message = "Phone number must contain only digits and optional leading +"
    )
    private String phoneNumber;

    @Email
    @NotBlank(message = "Email is required.")
    private String email;

    @NotBlank(message = "Address is required")
    @Size(max = 500, message = "Max 500 characters allowed")
    private String address;

    private boolean isActive = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFormattedId() {
        if (id < 100) {
            return String.format("#STU%03d", id);
        } else {
            return "#STU" + id;
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
