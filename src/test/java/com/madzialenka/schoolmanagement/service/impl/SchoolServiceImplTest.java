package com.madzialenka.schoolmanagement.service.impl;

import com.madzialenka.schoolmanagement.api.dto.*;
import com.madzialenka.schoolmanagement.db.entity.School;
import com.madzialenka.schoolmanagement.db.entity.SchoolSubject;
import com.madzialenka.schoolmanagement.db.repository.SchoolRepository;
import com.madzialenka.schoolmanagement.db.repository.SchoolSubjectRepository;
import com.madzialenka.schoolmanagement.exception.SchoolNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class SchoolServiceImplTest {

    private static final String DEFAULT_SORT_BY = "id";
    private static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.ASC;

    private SchoolServiceImpl underTest;
    private SchoolRepository schoolRepository;
    private SchoolSubjectRepository schoolSubjectRepository;

    @BeforeEach
    void setUp() {
        schoolRepository = Mockito.mock(SchoolRepository.class);
        schoolSubjectRepository = Mockito.mock(SchoolSubjectRepository.class);
        underTest = new SchoolServiceImpl(schoolRepository, schoolSubjectRepository);
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

        List<SchoolSubject> subjects1 = Arrays.asList(
                new SchoolSubject(1L, "maths", "John King", null, null),
                new SchoolSubject(2L, "english", "Matthew Roll", null, null)
        );
        List<SchoolSubject> subjects2 = Arrays.asList(
                new SchoolSubject(3L, "geography", "Linda Morales", null, null),
                new SchoolSubject(4L, "history", "Ann Hat", null, null)
        );
        List<School> schools = Arrays.asList(
                new School(1L, "Warsaw", "III", subjects1, null),
                new School(2L, "London", "IV", subjects2, null)
        );
        Mockito.when(schoolRepository.findAll(Sort.by(direction, sortBy))).thenReturn(schools);

        List<SchoolSubjectResponseDTO> subjectsResponse1 = Arrays.asList(
                new SchoolSubjectResponseDTO(1L, "maths", "John King"),
                new SchoolSubjectResponseDTO(2L, "english", "Matthew Roll")
        );
        List<SchoolSubjectResponseDTO> subjectsResponse2 = Arrays.asList(
                new SchoolSubjectResponseDTO(3L, "geography", "Linda Morales"),
                new SchoolSubjectResponseDTO(4L, "history", "Ann Hat")
        );
        List<SchoolResponseDTO> expected = Arrays.asList(
                new SchoolResponseDTO(1L, "Warsaw", "III", subjectsResponse1),
                new SchoolResponseDTO(2L, "London", "IV", subjectsResponse2)
        );

        List<SchoolResponseDTO> actual = underTest.getSchools(sortBy, direction);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getSchools_withNullParams() {
        String sortBy = null;
        Sort.Direction direction = null;

        List<SchoolSubject> subjects1 = Arrays.asList(
                new SchoolSubject(1L, "maths", "John King", null, null),
                new SchoolSubject(2L, "english", "Matthew Roll", null, null)
        );
        List<SchoolSubject> subjects2 = Arrays.asList(
                new SchoolSubject(3L, "geography", "Linda Morales", null, null),
                new SchoolSubject(4L, "history", "Ann Hat", null, null)
        );
        List<School> schools = Arrays.asList(
                new School(1L, "Warsaw", "III", subjects1, null),
                new School(2L, "London", "IV", subjects2, null)
        );
        Mockito.when(schoolRepository.findAll(Sort.by(DEFAULT_SORT_DIRECTION, DEFAULT_SORT_BY))).thenReturn(schools);

        List<SchoolSubjectResponseDTO> subjectsResponse1 = Arrays.asList(
                new SchoolSubjectResponseDTO(1L, "maths", "John King"),
                new SchoolSubjectResponseDTO(2L, "english", "Matthew Roll")
        );
        List<SchoolSubjectResponseDTO> subjectsResponse2 = Arrays.asList(
                new SchoolSubjectResponseDTO(3L, "geography", "Linda Morales"),
                new SchoolSubjectResponseDTO(4L, "history", "Ann Hat")
        );
        List<SchoolResponseDTO> expected = Arrays.asList(
                new SchoolResponseDTO(1L, "Warsaw", "III", subjectsResponse1),
                new SchoolResponseDTO(2L, "London", "IV", subjectsResponse2)
        );

        List<SchoolResponseDTO> actual = underTest.getSchools(sortBy, direction);
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
}