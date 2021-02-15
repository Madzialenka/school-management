package com.madzialenka.schoolmanagement.service;

import com.madzialenka.schoolmanagement.api.dto.GradeDataRequestDTO;
import com.madzialenka.schoolmanagement.api.dto.GradeResponseDTO;

public interface GradeService {
    GradeResponseDTO createGrade(Long schoolId, Long schoolSubjectId, GradeDataRequestDTO requestDTO);
}
