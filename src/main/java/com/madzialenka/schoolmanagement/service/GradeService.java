package com.madzialenka.schoolmanagement.service;

import com.madzialenka.schoolmanagement.api.dto.GradeDataRequestDTO;
import com.madzialenka.schoolmanagement.api.dto.GradeResponseDTO;
import com.madzialenka.schoolmanagement.api.dto.GradeSimpleResponseDTO;

import java.util.List;

public interface GradeService {
    GradeResponseDTO createGrade(Long schoolId, Long schoolSubjectId, GradeDataRequestDTO requestDTO);

    List<GradeSimpleResponseDTO> getGrades(Long schoolId, Long schoolSubjectId);

    void deleteGrade(Long schoolId, Long schoolSubjectId, Long gradeId);
}
