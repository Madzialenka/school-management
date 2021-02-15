package com.madzialenka.schoolmanagement.service;

import com.madzialenka.schoolmanagement.api.dto.StudentDataRequestDTO;
import com.madzialenka.schoolmanagement.api.dto.StudentResponseDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface StudentService {
    StudentResponseDTO createStudent(StudentDataRequestDTO requestDTO);

    List<StudentResponseDTO> getStudents(String sortBy, Sort.Direction direction);

    StudentResponseDTO updateStudent(Long id, StudentDataRequestDTO requestDTO);

    void deleteStudent(Long id);
}
