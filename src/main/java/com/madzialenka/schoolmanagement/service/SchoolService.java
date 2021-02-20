package com.madzialenka.schoolmanagement.service;

import com.madzialenka.schoolmanagement.api.dto.PageResponseDTO;
import com.madzialenka.schoolmanagement.api.dto.SchoolDataRequestDTO;
import com.madzialenka.schoolmanagement.api.dto.SchoolResponseDTO;
import com.madzialenka.schoolmanagement.api.dto.SchoolSimpleDataRequestDTO;
import org.springframework.data.domain.Sort;

public interface SchoolService {
    SchoolResponseDTO createSchool(SchoolDataRequestDTO schoolDataRequestDTO);

    PageResponseDTO<SchoolResponseDTO> getSchools(String sortBy, Sort.Direction direction,
                                                  Integer pageNumber, Integer pageSize);

    SchoolResponseDTO updateSchool(Long id, SchoolSimpleDataRequestDTO requestDTO);

    void deleteSchool(Long id);
}
