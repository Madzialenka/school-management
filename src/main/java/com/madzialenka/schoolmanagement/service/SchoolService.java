package com.madzialenka.schoolmanagement.service;

import com.madzialenka.schoolmanagement.api.dto.SchoolDataRequestDTO;
import com.madzialenka.schoolmanagement.api.dto.SchoolResponseDTO;

public interface SchoolService {
    SchoolResponseDTO createSchool(SchoolDataRequestDTO schoolDataRequestDTO);
}
