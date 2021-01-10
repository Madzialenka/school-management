package com.madzialenka.schoolmanagement.service;

import com.madzialenka.schoolmanagement.api.dto.SchoolDataRequestDTO;
import com.madzialenka.schoolmanagement.api.dto.SchoolResponseDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface SchoolService {
    SchoolResponseDTO createSchool(SchoolDataRequestDTO schoolDataRequestDTO);

    List<SchoolResponseDTO> getSchools(String sortBy, Sort.Direction direction);
}
