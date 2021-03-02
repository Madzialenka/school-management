package com.madzialenka.schoolmanagement.service.impl;

import com.madzialenka.schoolmanagement.api.dto.*;
import com.madzialenka.schoolmanagement.db.entity.School;
import com.madzialenka.schoolmanagement.db.entity.SchoolSubject;
import com.madzialenka.schoolmanagement.db.repository.SchoolRepository;
import com.madzialenka.schoolmanagement.db.repository.SchoolSubjectRepository;
import com.madzialenka.schoolmanagement.db.repository.StudentRepository;
import com.madzialenka.schoolmanagement.exception.SchoolNotFoundException;
import com.madzialenka.schoolmanagement.mapper.StudentResponseDTOMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
class SchoolServiceImplTest {

    private static final String DEFAULT_SORT_BY = "id";
    private static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.ASC;
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 1;

    private SchoolServiceImpl underTest;
    private SchoolRepository schoolRepository;
    private SchoolSubjectRepository schoolSubjectRepository;
    private StudentRepository studentRepository;
    private StudentResponseDTOMapper studentResponseDTOMapper;

    @BeforeEach
    void setUp() {
        schoolRepository = Mockito.mock(SchoolRepository.class);
        schoolSubjectRepository = Mockito.mock(SchoolSubjectRepository.class);
        studentRepository = Mockito.mock(StudentRepository.class);
        studentResponseDTOMapper = Mockito.mock(StudentResponseDTOMapper.class);
        underTest = new SchoolServiceImpl(schoolRepository, schoolSubjectRepository,
                studentRepository, studentResponseDTOMapper);
    }

    @Test
    void createSchool() {
        String town = "Warsaw";
        String schoolNumber = "III";
        List<SchoolSubjectDataRequestDTO> subjects = Arrays.asList(
                new SchoolSubjectDataRequestDTO("maths", "John Smith"),
                new SchoolSubjectDataRequestDTO("history", "Adam Stewart")
        );
        SchoolDataRequestDTO requestDTO = new SchoolDataRequestDTO(town, schoolNumber, subjects);
        Long id = 1L;

        School school = new School(null, town, schoolNumber, null, null);
        School savedSchool = new School(id, town, schoolNumber, null, null);
        Mockito.when(schoolRepository.save(school)).thenReturn(savedSchool);
        List<SchoolSubject> schoolSubjects = Arrays.asList(
                new SchoolSubject(null, "maths", "John Smith", savedSchool, null),
                new SchoolSubject(null, "history", "Adam Stewart", savedSchool, null)
        );
        List<SchoolSubject> savedSubjects = Arrays.asList(
                new SchoolSubject(1L, "maths", "John Smith", savedSchool, null),
                new SchoolSubject(2L, "history", "Adam Stewart", savedSchool, null)
        );
        Mockito.when(schoolSubjectRepository.saveAll(schoolSubjects)).thenReturn(savedSubjects);

        List<SchoolSubjectResponseDTO> responseSubjects = Arrays.asList(
                new SchoolSubjectResponseDTO(1L, "maths", "John Smith"),
                new SchoolSubjectResponseDTO(2L, "history", "Adam Stewart")
        );
        SchoolResponseDTO expected = new SchoolResponseDTO(id, town, schoolNumber, responseSubjects);

        SchoolResponseDTO actual = underTest.createSchool(requestDTO);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getSchools_withNotNullParams() {
        String sortBy = "town";
        Sort.Direction direction = Sort.Direction.DESC;
        int pageNumber = 1;
        int pageSize = 2;
        long numberOfElements = 5L;
        double pages = (double) numberOfElements / (double) pageSize;
        int numberOfPages = (int) Math.ceil(pages);

        List<SchoolSubject> subjects1 = Arrays.asList(
                new SchoolSubject(1L, "maths", "John King", null, null),
                new SchoolSubject(2L, "english", "Matthew Roll", null, null)
        );
        List<SchoolSubject> subjects2 = Arrays.asList(
                new SchoolSubject(3L, "geography", "Linda Morales", null, null),
                new SchoolSubject(4L, "history", "Ann Hat", null, null)
        );
        List<School> elements = Arrays.asList(
                new School(1L, "Warsaw", "III", subjects1, null),
                new School(2L, "London", "IV", subjects2, null)
        );
        PageRequest pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        Page<School> schools = new PageImpl<>(elements, pageable, numberOfElements);
        Mockito.when(schoolRepository.findAll(pageable))
                .thenReturn(schools);

        List<SchoolSubjectResponseDTO> subjectsResponse1 = Arrays.asList(
                new SchoolSubjectResponseDTO(1L, "maths", "John King"),
                new SchoolSubjectResponseDTO(2L, "english", "Matthew Roll")
        );
        List<SchoolSubjectResponseDTO> subjectsResponse2 = Arrays.asList(
                new SchoolSubjectResponseDTO(3L, "geography", "Linda Morales"),
                new SchoolSubjectResponseDTO(4L, "history", "Ann Hat")
        );
        List<SchoolResponseDTO> schoolsResponse = Arrays.asList(
                new SchoolResponseDTO(1L, "Warsaw", "III", subjectsResponse1),
                new SchoolResponseDTO(2L, "London", "IV", subjectsResponse2)
        );
        PageResponseDTO<SchoolResponseDTO> expected = PageResponseDTO.<SchoolResponseDTO>builder()
                .elements(schoolsResponse)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .numberOfPages(numberOfPages)
                .numberOfElements(numberOfElements)
                .build();

        PageResponseDTO<SchoolResponseDTO> actual = underTest.getSchools(sortBy, direction, pageNumber, pageSize);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getSchools_withNullParams() {
        String sortBy = null;
        Sort.Direction direction = null;
        Integer pageNumber = null;
        Integer pageSize = null;
        long numberOfElements = 5L;
        double pages = (double) numberOfElements / (double) DEFAULT_PAGE_SIZE;
        int numberOfPages = (int) Math.ceil(pages);

        List<SchoolSubject> subjects1 = Arrays.asList(
                new SchoolSubject(1L, "maths", "John King", null, null),
                new SchoolSubject(2L, "english", "Matthew Roll", null, null)
        );
        List<SchoolSubject> subjects2 = Arrays.asList(
                new SchoolSubject(3L, "geography", "Linda Morales", null, null),
                new SchoolSubject(4L, "history", "Ann Hat", null, null)
        );
        List<School> elements = Arrays.asList(
                new School(1L, "Warsaw", "III", subjects1, null),
                new School(2L, "London", "IV", subjects2, null)
        );
        PageRequest pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE,
                Sort.by(DEFAULT_SORT_DIRECTION, DEFAULT_SORT_BY));
        Page<School> schools = new PageImpl<>(elements, pageable, numberOfElements);
        Mockito.when(schoolRepository.findAll(pageable)).thenReturn(schools);

        List<SchoolSubjectResponseDTO> subjectsResponse1 = Arrays.asList(
                new SchoolSubjectResponseDTO(1L, "maths", "John King"),
                new SchoolSubjectResponseDTO(2L, "english", "Matthew Roll")
        );
        List<SchoolSubjectResponseDTO> subjectsResponse2 = Arrays.asList(
                new SchoolSubjectResponseDTO(3L, "geography", "Linda Morales"),
                new SchoolSubjectResponseDTO(4L, "history", "Ann Hat")
        );
        List<SchoolResponseDTO> schoolsResponse = Arrays.asList(
                new SchoolResponseDTO(1L, "Warsaw", "III", subjectsResponse1),
                new SchoolResponseDTO(2L, "London", "IV", subjectsResponse2)
        );
        PageResponseDTO<SchoolResponseDTO> expected = PageResponseDTO.<SchoolResponseDTO>builder()
                .elements(schoolsResponse)
                .pageNumber(DEFAULT_PAGE_NUMBER)
                .pageSize(DEFAULT_PAGE_SIZE)
                .numberOfPages(numberOfPages)
                .numberOfElements(numberOfElements)
                .build();

        PageResponseDTO<SchoolResponseDTO> actual = underTest.getSchools(sortBy, direction, pageNumber, pageSize);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateSchool() {
        Long id = 1L;
        String town = "Warsaw";
        String schoolNumber = "III";
        SchoolSimpleDataRequestDTO requestDTO = new SchoolSimpleDataRequestDTO(town, schoolNumber);

        List<SchoolSubject> subjects = Arrays.asList(
                new SchoolSubject(1L, "maths", "John King", null, null),
                new SchoolSubject(2L, "english", "Matthew Roll", null, null)
        );
        School school = new School(id, "London", "X", subjects, null);
        Mockito.when(schoolRepository.findById(id)).thenReturn(Optional.of(school));
        School updatedSchool = new School(id, town, schoolNumber, subjects, null);
        Mockito.when(schoolRepository.save(updatedSchool)).thenReturn(updatedSchool);

        List<SchoolSubjectResponseDTO> subjectsResponse = Arrays.asList(
                new SchoolSubjectResponseDTO(1L, "maths", "John King"),
                new SchoolSubjectResponseDTO(2L, "english", "Matthew Roll")
        );
        SchoolResponseDTO expected = new SchoolResponseDTO(id, town, schoolNumber, subjectsResponse);

        SchoolResponseDTO actual = underTest.updateSchool(id, requestDTO);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateSchool_withThrownException() {
        Long id = 1L;
        SchoolSimpleDataRequestDTO requestDTO = Mockito.mock(SchoolSimpleDataRequestDTO.class);

        Mockito.when(schoolRepository.findById(id)).thenReturn(Optional.empty());

        SchoolNotFoundException e =
                assertThrows(SchoolNotFoundException.class, () -> underTest.updateSchool(id, requestDTO));
        Assertions.assertEquals(String.format("School with id: %d not found", id), e.getMessage());
    }

    @Test
    void deleteSchool() {
        Long id = 1L;

        School school = new School(id, null, null, null, null);
        Mockito.when(schoolRepository.findById(id)).thenReturn(Optional.of(school));

        underTest.deleteSchool(id);
        Mockito.verify(schoolRepository).delete(school);
    }

    @Test
    void deleteSchool_withThrownException() {
        Long id = 1L;

        Mockito.when(schoolRepository.findById(id)).thenReturn(Optional.empty());

        SchoolNotFoundException e =
                assertThrows(SchoolNotFoundException.class, () -> underTest.deleteSchool(id));
        Assertions.assertEquals(String.format("School with id: %d not found", id), e.getMessage());
    }
}