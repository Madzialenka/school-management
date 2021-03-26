package com.madzialenka.schoolmanagement.service.impl;

import com.madzialenka.schoolmanagement.api.dto.*;
import com.madzialenka.schoolmanagement.db.entity.School;
import com.madzialenka.schoolmanagement.db.entity.SchoolSubject;
import com.madzialenka.schoolmanagement.db.entity.Student;
import com.madzialenka.schoolmanagement.db.projection.SchoolSubjectCountProjection;
import com.madzialenka.schoolmanagement.db.projection.SchoolSubjectGradesMeanProjection;
import com.madzialenka.schoolmanagement.db.repository.SchoolRepository;
import com.madzialenka.schoolmanagement.db.repository.SchoolSubjectRepository;
import com.madzialenka.schoolmanagement.db.repository.StudentRepository;
import com.madzialenka.schoolmanagement.exception.SchoolNotFoundException;
import com.madzialenka.schoolmanagement.mapper.StudentResponseDTOMapper;
import com.madzialenka.schoolmanagement.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class SchoolServiceImpl implements SchoolService {

    private static final String DEFAULT_SORT_BY = "id";
    private static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.ASC;
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 1;

    private final SchoolRepository schoolRepository;
    private final SchoolSubjectRepository schoolSubjectRepository;
    private final StudentRepository studentRepository;
    private final StudentResponseDTOMapper studentResponseDTOMapper;

    @Override
    public SchoolResponseDTO createSchool(SchoolDataRequestDTO requestDTO) {
        School school = new School();
        School savedSchool = updateAndSaveSchoolData(requestDTO, school);
        List<SchoolSubject> schoolSubjects = requestDTO.getSubjects().stream()
                .map(subjectRequestDTO -> createSchoolSubject(savedSchool, subjectRequestDTO))
                .collect(Collectors.toList());
        List<SchoolSubject> savedSubjects = schoolSubjectRepository.saveAll(schoolSubjects);
        return createSchoolResponseDTO(savedSchool, savedSubjects);
    }

    @Override
    public PageResponseDTO<SchoolResponseDTO> getSchools(String sortBy, Sort.Direction direction,
                                                         Integer pageNumber, Integer pageSize) {
        sortBy = Optional.ofNullable(sortBy).orElse(DEFAULT_SORT_BY);
        direction = Optional.ofNullable(direction).orElse(DEFAULT_SORT_DIRECTION);
        pageNumber = Optional.ofNullable(pageNumber).orElse(DEFAULT_PAGE_NUMBER);
        pageSize = Optional.ofNullable(pageSize).orElse(DEFAULT_PAGE_SIZE);
        PageRequest pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        Page<School> schools = schoolRepository.findAll(pageable);
        List<SchoolResponseDTO> elements = schools.getContent().stream()
                .map(school -> createSchoolResponseDTO(school, school.getSubjects()))
                .collect(Collectors.toList());
        return createPageResponseDTO(schools, elements);
    }

    @Override
    public SchoolResponseDTO updateSchool(Long id, SchoolSimpleDataRequestDTO requestDTO) {
        School foundSchool = getSchoolById(id);
        updateSchool(requestDTO, foundSchool);
        School savedSchool = schoolRepository.save(foundSchool);
        return createSchoolResponseDTO(savedSchool, savedSchool.getSubjects());
    }

    @Override
    public void deleteSchool(Long id) {
        School foundSchool = getSchoolById(id);
        schoolRepository.delete(foundSchool);
    }

    @Override
    public SchoolSubjectsGradesMeanResponseDTO getSchoolSubjectsGradesMean(Long id) {
        validateSchoolExistence(id);
        List<SchoolSubjectGradesMeanProjection> projections = schoolRepository.getSchoolSubjectsGradesMean(id);
        List<SchoolSubjectGradesMeanDTO> means = projections.stream()
                .map(this::createSchoolSubjectGradesMeanDTO)
                .collect(Collectors.toList());
        return new SchoolSubjectsGradesMeanResponseDTO(means);
    }

    @Override
    public List<StudentResponseDTO> getBestStudents(Long id, Long limit) {
        validateSchoolExistence(id);
        List<Long> bestStudentsIds = schoolRepository.getBestStudentsIds(id, limit);
        List<Student> students = studentRepository.findAllByIdAndSortByBestGradesMean(bestStudentsIds);
        return students.stream()
                .map(studentResponseDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponseDTO> getWorstStudents(Long id, Long limit) {
        validateSchoolExistence(id);
        List<Long> worstStudentsIds = schoolRepository.getWorstStudentsIds(id, limit);
        List<Student> students = studentRepository.findAllByIdAndSortByWorstGradesMean(worstStudentsIds);
        return students.stream()
                .map(studentResponseDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public SchoolSubjectCountSummaryResponseDTO getSchoolSubjectCountSummary(Long limit) {
        List<SchoolSubjectCountProjection> projections = schoolRepository.getSchoolSubjectCount(limit);
        List<SchoolSubjectCountDTO> subjectCounts = projections.stream()
                .map(this::createSchoolSubjectCountDTO)
                .collect(Collectors.toList());
        return new SchoolSubjectCountSummaryResponseDTO(subjectCounts);
    }

    private SchoolSubjectCountDTO createSchoolSubjectCountDTO(SchoolSubjectCountProjection projection) {
        SchoolSubjectCountDTO dto = new SchoolSubjectCountDTO();
        dto.setSubjectName(projection.getSubjectName());
        dto.setCount(projection.getCount());
        return dto;
    }

    private PageResponseDTO<SchoolResponseDTO> createPageResponseDTO(Page<School> schools,
                                                                     List<SchoolResponseDTO> elements) {
        return PageResponseDTO.<SchoolResponseDTO>builder()
                .elements(elements)
                .pageNumber(schools.getNumber())
                .pageSize(schools.getSize())
                .numberOfPages(schools.getTotalPages())
                .numberOfElements(schools.getTotalElements())
                .build();
    }

    private SchoolSubjectGradesMeanDTO createSchoolSubjectGradesMeanDTO(SchoolSubjectGradesMeanProjection projection) {
        SchoolSubjectGradesMeanDTO dto = new SchoolSubjectGradesMeanDTO();
        dto.setSchoolSubjectId(projection.getSchoolSubjectId());
        dto.setMean(projection.getMean());
        return dto;
    }

    private School getSchoolById(Long id) {
        return schoolRepository.findById(id).orElseThrow(() -> new SchoolNotFoundException(id));
    }

    private void updateSchool(SchoolSimpleDataRequestDTO requestDTO, School foundSchool) {
        foundSchool.setTown(requestDTO.getTown());
        foundSchool.setSchoolNumber(requestDTO.getSchoolNumber());
    }

    private SchoolResponseDTO createSchoolResponseDTO(School savedSchool, List<SchoolSubject> savedSubjects) {
        SchoolResponseDTO responseDTO = new SchoolResponseDTO();
        responseDTO.setId(savedSchool.getId());
        responseDTO.setTown(savedSchool.getTown());
        responseDTO.setSchoolNumber(savedSchool.getSchoolNumber());
        List<SchoolSubjectResponseDTO> subjectResponseDTOs = savedSubjects.stream()
                .map(this::createSchoolSubjectResponseDTO)
                .collect(Collectors.toList());
        responseDTO.setSubjects(subjectResponseDTOs);
        return responseDTO;
    }

    private SchoolSubjectResponseDTO createSchoolSubjectResponseDTO(SchoolSubject schoolSubject) {
        SchoolSubjectResponseDTO subjectResponseDTO = new SchoolSubjectResponseDTO();
        subjectResponseDTO.setId(schoolSubject.getId());
        subjectResponseDTO.setName(schoolSubject.getName());
        subjectResponseDTO.setTeacherName(schoolSubject.getTeacherName());
        return subjectResponseDTO;
    }

    private SchoolSubject createSchoolSubject(School savedSchool, SchoolSubjectDataRequestDTO subjectRequestDTO) {
        SchoolSubject subject = new SchoolSubject();
        subject.setName(subjectRequestDTO.getName());
        subject.setTeacherName(subjectRequestDTO.getTeacherName());
        subject.setSchool(savedSchool);
        return subject;
    }

    private School updateAndSaveSchoolData(SchoolDataRequestDTO requestDTO, School school) {
        school.setTown(requestDTO.getTown());
        school.setSchoolNumber(requestDTO.getSchoolNumber());
        return schoolRepository.save(school);
    }

    private void validateSchoolExistence(Long id) {
        getSchoolById(id);
    }
}
