package com.madzialenka.schoolmanagement.service.impl;

import com.madzialenka.schoolmanagement.api.dto.SchoolDataRequestDTO;
import com.madzialenka.schoolmanagement.api.dto.SchoolResponseDTO;
import com.madzialenka.schoolmanagement.api.dto.SchoolSubjectDataRequestDTO;
import com.madzialenka.schoolmanagement.api.dto.SchoolSubjectResponseDTO;
import com.madzialenka.schoolmanagement.db.entity.School;
import com.madzialenka.schoolmanagement.db.entity.SchoolSubject;
import com.madzialenka.schoolmanagement.db.repository.SchoolRepository;
import com.madzialenka.schoolmanagement.db.repository.SchoolSubjectRepository;
import com.madzialenka.schoolmanagement.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SchoolServiceImpl implements SchoolService {
    private final SchoolRepository schoolRepository;
    private final SchoolSubjectRepository schoolSubjectRepository;

    @Override
    public SchoolResponseDTO createSchool(SchoolDataRequestDTO requestDTO) {
        School school = new School();
        School savedSchool = updateAndSaveSchoolData(requestDTO, school);
        List<SchoolSubject> schoolSubjects = requestDTO.getSubjects().stream()
                .map(subjectRequestDTO -> createSchoolSubject(savedSchool, subjectRequestDTO))
                .collect(Collectors.toList());
        List<SchoolSubject> savedSubjects = schoolSubjectRepository.saveAll(schoolSubjects);
        return createSchoolResponseDTO(savedSchool, savedSubjects);
    }

    private SchoolResponseDTO createSchoolResponseDTO(School savedSchool, List<SchoolSubject> savedSubjects) {
        SchoolResponseDTO responseDTO = new SchoolResponseDTO();
        responseDTO.setId(savedSchool.getId());
        responseDTO.setTown(savedSchool.getTown());
        responseDTO.setSchoolNumber(savedSchool.getSchoolNumber());
        List<SchoolSubjectResponseDTO> subjectResponseDTOs = savedSubjects.stream()
                .map(this::createSchoolSubjectResponseDTO)
                .collect(Collectors.toList());
        responseDTO.setSubjects(subjectResponseDTOs);
        return responseDTO;
    }

    private SchoolSubjectResponseDTO createSchoolSubjectResponseDTO(SchoolSubject schoolSubject) {
        SchoolSubjectResponseDTO subjectResponseDTO = new SchoolSubjectResponseDTO();
        subjectResponseDTO.setId(schoolSubject.getId());
        subjectResponseDTO.setName(schoolSubject.getName());
        subjectResponseDTO.setTeacherName(schoolSubject.getTeacherName());
        return subjectResponseDTO;
    }

    private SchoolSubject createSchoolSubject(School savedSchool, SchoolSubjectDataRequestDTO subjectRequestDTO) {
        SchoolSubject subject = new SchoolSubject();
        subject.setName(subjectRequestDTO.getName());
        subject.setTeacherName(subjectRequestDTO.getTeacherName());
        subject.setSchool(savedSchool);
        return subject;
    }

    private School updateAndSaveSchoolData(SchoolDataRequestDTO requestDTO, School school) {
        school.setTown(requestDTO.getTown());
        school.setSchoolNumber(requestDTO.getSchoolNumber());
        return schoolRepository.save(school);
    }
}
