package com.madzialenka.schoolmanagement.api.controller;

import com.madzialenka.schoolmanagement.api.dto.GradesMeanResponseDTO;
import com.madzialenka.schoolmanagement.api.dto.SchoolSubjectDataRequestDTO;
import com.madzialenka.schoolmanagement.api.dto.SchoolSubjectResponseDTO;
import com.madzialenka.schoolmanagement.service.SchoolSubjectService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("schools/{schoolId}/school-subjects")
public class SchoolSubjectController {

    private static final String SCHOOL_SUBJECTS_EXCEL_CONTENT_DISPOSITION_VALUE
            = "attachment; filename=SchoolSubjects.xlsx";

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

    @GetMapping("{schoolSubjectId}/grades-mean")
    public GradesMeanResponseDTO getSchoolSubjectGradesMean(@PathVariable("schoolId") Long schoolId,
                                                            @PathVariable("schoolSubjectId") Long schoolSubjectId) {
        return schoolSubjectService.getSchoolSubjectGradesMean(schoolId, schoolSubjectId);
    }

    @GetMapping("xlsx")
    public ResponseEntity<StreamingResponseBody> exportSchoolSubjectsToExcel(@PathVariable("schoolId") Long schoolId) {
        Workbook workbook = schoolSubjectService.exportSchoolSubjectsToExcel(schoolId);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, SCHOOL_SUBJECTS_EXCEL_CONTENT_DISPOSITION_VALUE)
                .body(workbook::write);
    }
}
