package com.madzialenka.schoolmanagement.api.controller;

import com.madzialenka.schoolmanagement.api.dto.PageResponseDTO;
import com.madzialenka.schoolmanagement.api.dto.SchoolDataRequestDTO;
import com.madzialenka.schoolmanagement.api.dto.SchoolResponseDTO;
import com.madzialenka.schoolmanagement.api.dto.SchoolSimpleDataRequestDTO;
import com.madzialenka.schoolmanagement.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("schools")
public class SchoolController {
    private final SchoolService schoolService;

    @PostMapping
    public SchoolResponseDTO createSchool(@Valid @RequestBody SchoolDataRequestDTO schoolDataRequestDTO) {
        return schoolService.createSchool(schoolDataRequestDTO);
    }

    @GetMapping
    public PageResponseDTO<SchoolResponseDTO> getSchools(@RequestParam(value = "sortBy", required = false)
                                                                 String sortBy,
                                                         @RequestParam(value = "sortDirection", required = false)
                                                                 Sort.Direction direction,
                                                         @RequestParam(value = "pageNumber", required = false)
                                                                 Integer pageNumber,
                                                         @RequestParam(value = "pageSize", required = false)
                                                                 Integer pageSize) {
        return schoolService.getSchools(sortBy, direction, pageNumber, pageSize);
    }

    @PutMapping("{id}")
    public SchoolResponseDTO updateSchool(@PathVariable("id") Long id,
                                          @Valid @RequestBody SchoolSimpleDataRequestDTO requestDTO) {
        return schoolService.updateSchool(id, requestDTO);
    }

    @DeleteMapping("{id}")
    public void deleteSchool(@PathVariable("id") Long id) {
        schoolService.deleteSchool(id);
    }
}
