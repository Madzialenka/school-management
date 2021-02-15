package com.madzialenka.schoolmanagement.api.controller;

import com.madzialenka.schoolmanagement.api.dto.StudentDataRequestDTO;
import com.madzialenka.schoolmanagement.api.dto.StudentResponseDTO;
import com.madzialenka.schoolmanagement.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("students")
@RestController
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    public StudentResponseDTO createStudent(@RequestBody StudentDataRequestDTO requestDTO) {
        return studentService.createStudent(requestDTO);
    }

    @GetMapping
    public List<StudentResponseDTO> getStudents(@RequestParam(value = "sortBy", required = false) String sortBy,
                                                @RequestParam(value = "sortDirection", required = false) Sort.Direction direction) {
        return studentService.getStudents(sortBy, direction);
    }

    @PutMapping("{id}")
    public StudentResponseDTO updateStudent(@PathVariable("id") Long id, @RequestBody StudentDataRequestDTO requestDTO) {
        return studentService.updateStudent(id, requestDTO);
    }

    @DeleteMapping("{id}")
    public void deleteStudent (@PathVariable("id") Long id) {
        studentService.deleteStudent(id);
    }
}
