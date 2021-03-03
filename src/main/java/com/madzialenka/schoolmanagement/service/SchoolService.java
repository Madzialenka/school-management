package com.madzialenka.schoolmanagement.service;

import com.madzialenka.schoolmanagement.api.dto.*;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface SchoolService {
    SchoolResponseDTO createSchool(SchoolDataRequestDTO schoolDataRequestDTO);

    PageResponseDTO<SchoolResponseDTO> getSchools(String sortBy, Sort.Direction direction,
                                                  Integer pageNumber, Integer pageSize);

    SchoolResponseDTO updateSchool(Long id, SchoolSimpleDataRequestDTO requestDTO);

    void deleteSchool(Long id);

    SchoolSubjectsGradesMeanResponseDTO getSchoolSubjectsGradesMean(Long id);

    List<StudentResponseDTO> getBestStudents(Long id, Long limit);

    List<StudentResponseDTO> getWorstStudents(Long id, Long limit);

    SchoolSubjectCountSummaryResponseDTO getSchoolSubjectCountSummary(Long limit);
}
