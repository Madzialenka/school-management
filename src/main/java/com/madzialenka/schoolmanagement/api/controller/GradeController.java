package com.madzialenka.schoolmanagement.api.controller;

import com.madzialenka.schoolmanagement.api.dto.GradeDataRequestDTO;
import com.madzialenka.schoolmanagement.api.dto.GradeResponseDTO;
import com.madzialenka.schoolmanagement.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}
