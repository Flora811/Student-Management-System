package com.flora.student.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/corn")
public class CronController {

    @GetMapping("/ping")
    public ResponseEntity<String> ping(@RequestParam String token) {
        if (!"corntoken".equals(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        }

        return ResponseEntity.ok("Task Successful");
    }
}
