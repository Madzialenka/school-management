package com.madzialenka.schoolmanagement.service;

import com.madzialenka.schoolmanagement.api.dto.StudentDataRequestDTO;
import com.madzialenka.schoolmanagement.api.dto.StudentResponseDTO;

public interface StudentService {
    StudentResponseDTO createStudent(StudentDataRequestDTO requestDTO);
}
