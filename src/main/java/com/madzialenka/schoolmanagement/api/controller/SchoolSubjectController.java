package com.madzialenka.schoolmanagement.api.controller;

import com.madzialenka.schoolmanagement.api.dto.SchoolSubjectDataRequestDTO;
import com.madzialenka.schoolmanagement.api.dto.SchoolSubjectResponseDTO;
import com.madzialenka.schoolmanagement.service.SchoolSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("schools/{schoolId}/school-subjects")
public class SchoolSubjectController {
    private final SchoolSubjectService schoolSubjectService;

    @PostMapping
    public SchoolSubjectResponseDTO createSchoolSubject(@PathVariable("schoolId") Long schoolId,
                                                        @Valid @RequestBody SchoolSubjectDataRequestDTO requestDTO) {
        return schoolSubjectService.createSchoolSubject(schoolId, requestDTO);
    }

    @PutMapping("{schoolSubjectId}")
    public SchoolSubjectResponseDTO updateSchoolSubject(@PathVariable("schoolId") Long schoolId,
                                                        @PathVariable("schoolSubjectId") Long schoolSubjectId,
                                                        @Valid @RequestBody SchoolSubjectDataRequestDTO requestDTO) {
        return schoolSubjectService.updateSchoolSubject(schoolId, schoolSubjectId, requestDTO);
    }

    @DeleteMapping("{schoolSubjectId}")
    public void deleteSchoolSubject(@PathVariable("schoolId") Long schoolId,
                                    @PathVariable("schoolSubjectId") Long schoolSubjectId) {
        schoolSubjectService.deleteSchoolSubject(schoolId, schoolSubjectId);
    }

    @GetMapping
    public List<SchoolSubjectResponseDTO> getSchoolSubjects(@PathVariable("schoolId") Long schoolId) {
        return schoolSubjectService.getSchoolSubjects(schoolId);
    }
}
