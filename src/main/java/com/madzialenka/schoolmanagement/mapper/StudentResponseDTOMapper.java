package com.madzialenka.schoolmanagement.mapper;

import com.madzialenka.schoolmanagement.api.dto.SchoolSimpleResponseDTO;
import com.madzialenka.schoolmanagement.api.dto.StudentResponseDTO;
import com.madzialenka.schoolmanagement.db.entity.School;
import com.madzialenka.schoolmanagement.db.entity.Student;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StudentResponseDTOMapper {

    public StudentResponseDTO map(Student student) {
        StudentResponseDTO responseDTO = new StudentResponseDTO();
        responseDTO.setId(student.getId());
        responseDTO.setSurname(student.getSurname());
        responseDTO.setName(student.getName());
        responseDTO.setEmail(student.getEmail());
        responseDTO.setPesel(student.getPesel());
        responseDTO.setGender(student.getGender());
        List<SchoolSimpleResponseDTO> schools = student.getSchools().stream()
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
}
