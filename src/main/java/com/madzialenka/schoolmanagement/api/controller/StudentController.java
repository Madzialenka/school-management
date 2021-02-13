package com.madzialenka.schoolmanagement.api.controller;

import com.madzialenka.schoolmanagement.api.dto.StudentDataRequestDTO;
import com.madzialenka.schoolmanagement.api.dto.StudentResponseDTO;
import com.madzialenka.schoolmanagement.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("students")
@RestController
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    public StudentResponseDTO createStudent(@RequestBody StudentDataRequestDTO requestDTO) {
        return studentService.createStudent(requestDTO);
    }
}
