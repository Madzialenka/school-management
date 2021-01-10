package com.madzialenka.schoolmanagement.api.controller;

import com.madzialenka.schoolmanagement.api.dto.SchoolDataRequestDTO;
import com.madzialenka.schoolmanagement.api.dto.SchoolResponseDTO;
import com.madzialenka.schoolmanagement.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("schools")
public class SchoolController {
    private final SchoolService schoolService;

    @PostMapping
    public SchoolResponseDTO createSchool(@RequestBody SchoolDataRequestDTO schoolDataRequestDTO) {
        return schoolService.createSchool(schoolDataRequestDTO);
    }

    @GetMapping
    public List<SchoolResponseDTO> getSchools(@RequestParam(value = "sortBy", required = false) String sortBy,
                                              @RequestParam(value = "sortDirection", required = false) Sort.Direction direction) {
        return schoolService.getSchools(sortBy, direction);
    }
}
