package com.madzialenka.schoolmanagement.service.impl;

import com.madzialenka.schoolmanagement.api.dto.SchoolSimpleResponseDTO;
import com.madzialenka.schoolmanagement.api.dto.StudentDataRequestDTO;
import com.madzialenka.schoolmanagement.api.dto.StudentResponseDTO;
import com.madzialenka.schoolmanagement.db.entity.School;
import com.madzialenka.schoolmanagement.db.entity.Student;
import com.madzialenka.schoolmanagement.db.repository.SchoolRepository;
import com.madzialenka.schoolmanagement.db.repository.StudentRepository;
import com.madzialenka.schoolmanagement.exception.StudentAlreadyExistsException;
import com.madzialenka.schoolmanagement.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;

    @Override
    public StudentResponseDTO createStudent(StudentDataRequestDTO requestDTO) {
        validateStudentUniqueness(requestDTO);
        Student student = new Student();
        updateStudent(requestDTO, student);
        Student savedStudent = studentRepository.save(student);
        return createStudentResponseDTO(savedStudent);
    }

    private void validateStudentUniqueness(StudentDataRequestDTO requestDTO) {
        String pesel = requestDTO.getPesel();
        String email = requestDTO.getEmail();
        if (!studentRepository.findByPeselOrEmail(pesel, email).isEmpty()) {
            throw new StudentAlreadyExistsException(pesel, email);
        }
    }

    private StudentResponseDTO createStudentResponseDTO(Student savedStudent) {
        StudentResponseDTO responseDTO = new StudentResponseDTO();
        responseDTO.setId(savedStudent.getId());
        responseDTO.setSurname(savedStudent.getSurname());
        responseDTO.setName(savedStudent.getName());
        responseDTO.setEmail(savedStudent.getEmail());
        responseDTO.setPesel(savedStudent.getPesel());
        responseDTO.setGender(savedStudent.getGender());
        List<SchoolSimpleResponseDTO> schools = savedStudent.getSchools().stream()
                .map(this::createSchoolSimpleResponseDTO)
                .collect(Collectors.toList());
        responseDTO.setSchools(schools);
        return responseDTO;
    }

    private SchoolSimpleResponseDTO createSchoolSimpleResponseDTO(School school) {
        SchoolSimpleResponseDTO schoolResponseDTO = new SchoolSimpleResponseDTO();
        schoolResponseDTO.setId(school.getId());
        schoolResponseDTO.setTown(school.getTown());
        schoolResponseDTO.setSchoolNumber(school.getSchoolNumber());
        return schoolResponseDTO;
    }

    private void updateStudent(StudentDataRequestDTO requestDTO, Student student) {
        student.setSurname(requestDTO.getSurname());
        student.setName(requestDTO.getName());
        student.setEmail(requestDTO.getEmail());
        student.setPesel(requestDTO.getPesel());
        student.setGender(requestDTO.getGender());
        List<School> schools = schoolRepository.findAllById(requestDTO.getSchoolIds());
        student.setSchools(schools);
    }
}
