package com.madzialenka.schoolmanagement.api.controller;

import com.madzialenka.schoolmanagement.api.dto.GradeDataRequestDTO;
import com.madzialenka.schoolmanagement.api.dto.GradeResponseDTO;
import com.madzialenka.schoolmanagement.api.dto.GradeSimpleResponseDTO;
import com.madzialenka.schoolmanagement.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("schools/{schoolId}/school-subjects/{schoolSubjectId}/grades")
public class GradeController {
    private final GradeService gradeService;

    @PostMapping
    public GradeResponseDTO createGrade(@PathVariable("schoolId") Long schoolId,
                                        @PathVariable("schoolSubjectId") Long schoolSubjectId,
                                        @RequestBody GradeDataRequestDTO requestDTO) {
        return gradeService.createGrade(schoolId, schoolSubjectId, requestDTO);
    }

    @GetMapping
    public List<GradeSimpleResponseDTO> getGrades(@PathVariable("schoolId") Long schoolId,
                                                  @PathVariable("schoolSubjectId") Long schoolSubjectId) {
        return gradeService.getGrades(schoolId, schoolSubjectId);
    }

    @DeleteMapping("{gradeId}")
    public void deleteGrade(@PathVariable("schoolId") Long schoolId,
                            @PathVariable("schoolSubjectId") Long schoolSubjectId,
                            @PathVariable("gradeId") Long gradeId) {
        gradeService.deleteGrade(schoolId, schoolSubjectId, gradeId);
    }
}
