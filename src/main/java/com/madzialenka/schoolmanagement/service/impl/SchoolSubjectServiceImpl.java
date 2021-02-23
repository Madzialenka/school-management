package com.madzialenka.schoolmanagement.service.impl;

import com.madzialenka.schoolmanagement.api.dto.GradesMeanResponseDTO;
import com.madzialenka.schoolmanagement.api.dto.SchoolSubjectDataRequestDTO;
import com.madzialenka.schoolmanagement.api.dto.SchoolSubjectResponseDTO;
import com.madzialenka.schoolmanagement.db.entity.School;
import com.madzialenka.schoolmanagement.db.entity.SchoolSubject;
import com.madzialenka.schoolmanagement.db.repository.SchoolRepository;
import com.madzialenka.schoolmanagement.db.repository.SchoolSubjectRepository;
import com.madzialenka.schoolmanagement.exception.SchoolNotFoundException;
import com.madzialenka.schoolmanagement.exception.SchoolSubjectNotFoundException;
import com.madzialenka.schoolmanagement.exception.SubjectNotInSchoolException;
import com.madzialenka.schoolmanagement.service.SchoolSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SchoolSubjectServiceImpl implements SchoolSubjectService {

    private static final Sort.Direction DIRECTION = Sort.Direction.ASC;
    private static final String SORT_BY = "id";
    private final SchoolRepository schoolRepository;
    private final SchoolSubjectRepository schoolSubjectRepository;

    @Override
    public SchoolSubjectResponseDTO createSchoolSubject(Long schoolId, SchoolSubjectDataRequestDTO requestDTO) {
        SchoolSubject schoolSubject = new SchoolSubject();
        updateSchoolSubject(schoolSubject, schoolId, requestDTO);
        SchoolSubject savedSchoolSubject = schoolSubjectRepository.save(schoolSubject);
        return createSchoolSubjectResponseDTO(savedSchoolSubject);
    }

    @Override
    public SchoolSubjectResponseDTO updateSchoolSubject(Long schoolId, Long schoolSubjectId,
                                                        SchoolSubjectDataRequestDTO requestDTO) {
        validateSubjectAndSchool(schoolId, schoolSubjectId);
        SchoolSubject foundSubject = getSchoolSubjectById(schoolSubjectId);
        updateSchoolSubjectBasicData(foundSubject, requestDTO);
        SchoolSubject savedSchoolSubject = schoolSubjectRepository.save(foundSubject);
        return createSchoolSubjectResponseDTO(savedSchoolSubject);
    }

    @Override
    public void deleteSchoolSubject(Long schoolId, Long schoolSubjectId) {
        validateSubjectAndSchool(schoolId, schoolSubjectId);
        SchoolSubject foundSchoolSubject = getSchoolSubjectById(schoolSubjectId);
        schoolSubjectRepository.delete(foundSchoolSubject);
    }

    @Override
    public List<SchoolSubjectResponseDTO> getSchoolSubjects(Long schoolId) {
        School school = getSchoolById(schoolId);
        return schoolSubjectRepository.findBySchool(school, Sort.by(DIRECTION, SORT_BY)).stream()
                .map(this::createSchoolSubjectResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GradesMeanResponseDTO getSchoolSubjectGradesMean(Long schoolId, Long schoolSubjectId) {
        validateSubjectAndSchool(schoolId, schoolSubjectId);
        Double mean = schoolSubjectRepository.getSchoolSubjectGradesMean(schoolSubjectId);
        return new GradesMeanResponseDTO(mean);
    }

    private void updateSchoolSubjectBasicData(SchoolSubject foundSubject, SchoolSubjectDataRequestDTO requestDTO) {
        foundSubject.setName(requestDTO.getName());
        foundSubject.setTeacherName(requestDTO.getTeacherName());
    }

    private void validateSubjectAndSchool(Long schoolId, Long schoolSubjectId) {
        School school = getSchoolById(schoolId);
        getSchoolSubjectById(schoolSubjectId);
        if (schoolSubjectRepository.findByIdAndSchool(schoolSubjectId, school).isEmpty()) {
            throw new SubjectNotInSchoolException(schoolId, schoolSubjectId);
        }
    }

    private SchoolSubject getSchoolSubjectById(Long schoolSubjectId) {
        return schoolSubjectRepository.findById(schoolSubjectId)
                .orElseThrow(() -> new SchoolSubjectNotFoundException(schoolSubjectId));
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
