package com.madzialenka.schoolmanagement.service;

import com.madzialenka.schoolmanagement.api.dto.SchoolSubjectDataRequestDTO;
import com.madzialenka.schoolmanagement.api.dto.SchoolSubjectResponseDTO;

public interface SchoolSubjectService {
    SchoolSubjectResponseDTO createSchoolSubject(Long schoolId, SchoolSubjectDataRequestDTO requestDTO);

    SchoolSubjectResponseDTO updateSchoolSubject(Long schoolId, Long schoolSubjectId,
                                                 SchoolSubjectDataRequestDTO requestDTO);

    void deleteSchoolSubject(Long schoolId, Long schoolSubjectId);
}
