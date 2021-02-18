package com.madzialenka.schoolmanagement.service.impl;

import com.madzialenka.schoolmanagement.api.dto.SchoolSubjectDataRequestDTO;
import com.madzialenka.schoolmanagement.api.dto.SchoolSubjectResponseDTO;
import com.madzialenka.schoolmanagement.db.entity.School;
import com.madzialenka.schoolmanagement.db.entity.SchoolSubject;
import com.madzialenka.schoolmanagement.db.repository.SchoolRepository;
import com.madzialenka.schoolmanagement.db.repository.SchoolSubjectRepository;
import com.madzialenka.schoolmanagement.exception.SchoolNotFoundException;
import com.madzialenka.schoolmanagement.service.SchoolSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SchoolSubjectServiceImpl implements SchoolSubjectService {
    private final SchoolRepository schoolRepository;
    private final SchoolSubjectRepository schoolSubjectRepository;

    @Override
    public SchoolSubjectResponseDTO createSchoolSubject(Long schoolId, SchoolSubjectDataRequestDTO requestDTO) {
        SchoolSubject schoolSubject = new SchoolSubject();
        updateSchoolSubject(schoolSubject, schoolId, requestDTO);
        SchoolSubject savedSchoolSubject = schoolSubjectRepository.save(schoolSubject);
        return createSchoolSubjectResponseDTO(savedSchoolSubject);
    }

    private SchoolSubjectResponseDTO createSchoolSubjectResponseDTO(SchoolSubject savedSchoolSubject) {
        SchoolSubjectResponseDTO responseDTO = new SchoolSubjectResponseDTO();
        responseDTO.setId(savedSchoolSubject.getId());
        responseDTO.setName(savedSchoolSubject.getName());
        responseDTO.setTeacherName(savedSchoolSubject.getTeacherName());
        return responseDTO;
    }

    private void updateSchoolSubject(SchoolSubject schoolSubject, Long schoolId, SchoolSubjectDataRequestDTO requestDTO) {
        School school = getSchoolById(schoolId);
        schoolSubject.setName(requestDTO.getName());
        schoolSubject.setTeacherName(requestDTO.getTeacherName());
        schoolSubject.setSchool(school);
    }

    private School getSchoolById(Long schoolId) {
        return schoolRepository.findById(schoolId).orElseThrow(() -> new SchoolNotFoundException(schoolId));
    }
}
