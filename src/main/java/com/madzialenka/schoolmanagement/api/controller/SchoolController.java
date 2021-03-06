package com.madzialenka.schoolmanagement.api.controller;

import com.madzialenka.schoolmanagement.api.dto.*;
import com.madzialenka.schoolmanagement.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("{id}/grades-mean")
    public SchoolSubjectsGradesMeanResponseDTO getSchoolSubjectsGradesMean(@PathVariable("id") Long id){
        return schoolService.getSchoolSubjectsGradesMean(id);
    }

    @GetMapping("{id}/best-students")
    public List<StudentResponseDTO> getBestStudents(@PathVariable("id") Long id,
                                                    @RequestParam("limit") Long limit) {
        return schoolService.getBestStudents(id, limit);
    }

    @GetMapping("{id}/worst-students")
    public List<StudentResponseDTO> getWorstStudents(@PathVariable("id") Long id,
                                                     @RequestParam("limit") Long limit) {
        return schoolService.getWorstStudents(id, limit);
    }

    @GetMapping("subject-count-summary")
    public SchoolSubjectCountSummaryResponseDTO getSchoolSubjectCountSummary(@RequestParam("limit") Long limit) {
        return schoolService.getSchoolSubjectCountSummary(limit);
    }
}
