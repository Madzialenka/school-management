package com.madzialenka.schoolmanagement.service.impl;

import com.madzialenka.schoolmanagement.api.dto.GradesMeanResponseDTO;
import com.madzialenka.schoolmanagement.api.dto.SchoolSubjectDataRequestDTO;
import com.madzialenka.schoolmanagement.api.dto.SchoolSubjectResponseDTO;
import com.madzialenka.schoolmanagement.db.entity.School;
import com.madzialenka.schoolmanagement.db.entity.SchoolSubject;
import com.madzialenka.schoolmanagement.db.repository.SchoolRepository;
import com.madzialenka.schoolmanagement.db.repository.SchoolSubjectRepository;
import com.madzialenka.schoolmanagement.exception.SchoolNotFoundException;
import com.madzialenka.schoolmanagement.exception.SchoolSubjectNotFoundException;
import com.madzialenka.schoolmanagement.exception.SubjectNotInSchoolException;
import com.madzialenka.schoolmanagement.service.SchoolSubjectService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class SchoolSubjectServiceImpl implements SchoolSubjectService {

    private static final Sort.Direction DIRECTION = Sort.Direction.ASC;
    private static final String SORT_BY = "id";
    private static final String SHEET_NAME = "School Subjects";
    private static final String NAME_COLUMN_TITLE = "Name";
    private static final String SCHOOL_COLUMN_TITLE = "School";
    private static final String TEACHER_COLUMN_TITLE = "Teacher";

    private final SchoolRepository schoolRepository;
    private final SchoolSubjectRepository schoolSubjectRepository;

    @Override
    public SchoolSubjectResponseDTO createSchoolSubject(Long schoolId, SchoolSubjectDataRequestDTO requestDTO) {
        SchoolSubject schoolSubject = new SchoolSubject();
        updateSchoolSubject(schoolSubject, schoolId, requestDTO);
        SchoolSubject savedSchoolSubject = schoolSubjectRepository.save(schoolSubject);
        return createSchoolSubjectResponseDTO(savedSchoolSubject);
    }

    @Override
    public SchoolSubjectResponseDTO updateSchoolSubject(Long schoolId, Long schoolSubjectId,
                                                        SchoolSubjectDataRequestDTO requestDTO) {
        validateSubjectAndSchool(schoolId, schoolSubjectId);
        SchoolSubject foundSubject = getSchoolSubjectById(schoolSubjectId);
        updateSchoolSubjectBasicData(foundSubject, requestDTO);
        SchoolSubject savedSchoolSubject = schoolSubjectRepository.save(foundSubject);
        return createSchoolSubjectResponseDTO(savedSchoolSubject);
    }

    @Override
    public void deleteSchoolSubject(Long schoolId, Long schoolSubjectId) {
        validateSubjectAndSchool(schoolId, schoolSubjectId);
        SchoolSubject foundSchoolSubject = getSchoolSubjectById(schoolSubjectId);
        schoolSubjectRepository.delete(foundSchoolSubject);
    }

    @Override
    public List<SchoolSubjectResponseDTO> getSchoolSubjects(Long schoolId) {
        School school = getSchoolById(schoolId);
        return schoolSubjectRepository.findBySchool(school, Sort.by(DIRECTION, SORT_BY)).stream()
                .map(this::createSchoolSubjectResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GradesMeanResponseDTO getSchoolSubjectGradesMean(Long schoolId, Long schoolSubjectId) {
        validateSubjectAndSchool(schoolId, schoolSubjectId);
        Double mean = schoolSubjectRepository.getSchoolSubjectGradesMean(schoolSubjectId);
        return new GradesMeanResponseDTO(mean);
    }

    @Override
    public Workbook exportSchoolSubjectsToExcel(Long schoolId) {
        School school = getSchoolById(schoolId);
        List<SchoolSubject> schoolSubjects = schoolSubjectRepository.findBySchool(school, Sort.by(DIRECTION, SORT_BY));
        return createSchoolSubjectsExcel(school, schoolSubjects);
    }

    private Workbook createSchoolSubjectsExcel(School school, List<SchoolSubject> schoolSubjects) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(SHEET_NAME);
        createTitleRow(sheet);
        completeExcelWithSchoolSubjectsData(school, schoolSubjects, sheet);
        setAutoSizeColumns(sheet);
        return workbook;
    }

    private void setAutoSizeColumns(Sheet sheet) {
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
    }

    private void completeExcelWithSchoolSubjectsData(School school, List<SchoolSubject> schoolSubjects, Sheet sheet) {
        for (int i = 1; i < schoolSubjects.size() + 1; i++) {
            SchoolSubject subject = schoolSubjects.get(i - 1);
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(subject.getName());
            row.createCell(1).setCellValue(String.format("%s %s", school.getTown(), school.getSchoolNumber()));
            row.createCell(2).setCellValue(subject.getTeacherName());
        }
    }

    private void createTitleRow(Sheet sheet) {
        Row titleRow = sheet.createRow(0);
        titleRow.createCell(0).setCellValue(NAME_COLUMN_TITLE);
        titleRow.createCell(1).setCellValue(SCHOOL_COLUMN_TITLE);
        titleRow.createCell(2).setCellValue(TEACHER_COLUMN_TITLE);
    }

    private void updateSchoolSubjectBasicData(SchoolSubject foundSubject, SchoolSubjectDataRequestDTO requestDTO) {
        foundSubject.setName(requestDTO.getName());
        foundSubject.setTeacherName(requestDTO.getTeacherName());
    }

    private void validateSubjectAndSchool(Long schoolId, Long schoolSubjectId) {
        School school = getSchoolById(schoolId);
        getSchoolSubjectById(schoolSubjectId);
        if (schoolSubjectRepository.findByIdAndSchool(schoolSubjectId, school).isEmpty()) {
            throw new SubjectNotInSchoolException(schoolId, schoolSubjectId);
        }
    }

    private SchoolSubject getSchoolSubjectById(Long schoolSubjectId) {
        return schoolSubjectRepository.findById(schoolSubjectId)
                .orElseThrow(() -> new SchoolSubjectNotFoundException(schoolSubjectId));
    }

    private SchoolSubjectResponseDTO createSchoolSubjectResponseDTO(SchoolSubject savedSchoolSubject) {
        SchoolSubjectResponseDTO responseDTO = new SchoolSubjectResponseDTO();
        responseDTO.setId(savedSchoolSubject.getId());
        responseDTO.setName(savedSchoolSubject.getName());
        responseDTO.setTeacherName(savedSchoolSubject.getTeacherName());
        return responseDTO;
    }

    private void updateSchoolSubject(SchoolSubject schoolSubject, Long schoolId, SchoolSubjectDataRequestDTO requestDTO) {
        School school = getSchoolById(schoolId);
        schoolSubject.setName(requestDTO.getName());
        schoolSubject.setTeacherName(requestDTO.getTeacherName());
        schoolSubject.setSchool(school);
    }

    private School getSchoolById(Long schoolId) {
        return schoolRepository.findById(schoolId).orElseThrow(() -> new SchoolNotFoundException(schoolId));
    }
}
