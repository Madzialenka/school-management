package com.madzialenka.schoolmanagement.service.impl;

import com.madzialenka.schoolmanagement.api.dto.SchoolDataRequestDTO;
import com.madzialenka.schoolmanagement.api.dto.SchoolResponseDTO;
import com.madzialenka.schoolmanagement.api.dto.SchoolSubjectDataRequestDTO;
import com.madzialenka.schoolmanagement.api.dto.SchoolSubjectResponseDTO;
import com.madzialenka.schoolmanagement.db.entity.School;
import com.madzialenka.schoolmanagement.db.entity.SchoolSubject;
import com.madzialenka.schoolmanagement.db.repository.SchoolRepository;
import com.madzialenka.schoolmanagement.db.repository.SchoolSubjectRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class SchoolServiceImplTest {

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
}