package com.madzialenka.schoolmanagement.service.impl;

import com.madzialenka.schoolmanagement.api.dto.GradeDataRequestDTO;
import com.madzialenka.schoolmanagement.api.dto.GradeResponseDTO;
import com.madzialenka.schoolmanagement.api.dto.SchoolSubjectResponseDTO;
import com.madzialenka.schoolmanagement.api.dto.StudentSimpleResponseDTO;
import com.madzialenka.schoolmanagement.db.entity.Grade;
import com.madzialenka.schoolmanagement.db.entity.School;
import com.madzialenka.schoolmanagement.db.entity.SchoolSubject;
import com.madzialenka.schoolmanagement.db.entity.Student;
import com.madzialenka.schoolmanagement.db.repository.GradeRepository;
import com.madzialenka.schoolmanagement.db.repository.SchoolRepository;
import com.madzialenka.schoolmanagement.db.repository.SchoolSubjectRepository;
import com.madzialenka.schoolmanagement.db.repository.StudentRepository;
import com.madzialenka.schoolmanagement.exception.*;
import com.madzialenka.schoolmanagement.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GradeServiceImpl implements GradeService {

    private final SchoolRepository schoolRepository;
    private final SchoolSubjectRepository schoolSubjectRepository;
    private final StudentRepository studentRepository;
    private final GradeRepository gradeRepository;

    @Override
    public GradeResponseDTO createGrade(Long schoolId, Long schoolSubjectId, GradeDataRequestDTO requestDTO) {
        validateRequestedData(schoolId, schoolSubjectId, requestDTO);
        Grade grade = new Grade();
        updateGrade(grade, requestDTO, schoolSubjectId);
        Grade savedGrade = gradeRepository.save(grade);
        return createGradeResponseDTO(savedGrade);
    }

    private GradeResponseDTO createGradeResponseDTO(Grade savedGrade) {
        GradeResponseDTO responseDTO = new GradeResponseDTO();
        responseDTO.setId(savedGrade.getId());
        responseDTO.setValue(savedGrade.getValue());
        responseDTO.setStudent(createStudentSimpleResponseDTO(savedGrade));
        responseDTO.setSchoolSubject(createSchoolSubjectResponseDTO(savedGrade));
        return responseDTO;
    }

    private SchoolSubjectResponseDTO createSchoolSubjectResponseDTO(Grade savedGrade) {
        SchoolSubjectResponseDTO responseDTO = new SchoolSubjectResponseDTO();
        SchoolSubject subject = savedGrade.getSchoolSubject();
        responseDTO.setId(subject.getId());
        responseDTO.setName(subject.getName());
        responseDTO.setTeacherName(subject.getTeacherName());
        return responseDTO;
    }

    private StudentSimpleResponseDTO createStudentSimpleResponseDTO(Grade savedGrade) {
        StudentSimpleResponseDTO responseDTO = new StudentSimpleResponseDTO();
        Student student = savedGrade.getStudent();
        responseDTO.setId(student.getId());
        responseDTO.setSurname(student.getSurname());
        responseDTO.setName(student.getName());
        responseDTO.setEmail(student.getEmail());
        responseDTO.setPesel(student.getPesel());
        responseDTO.setGender(student.getGender());
        return responseDTO;
    }

    private void updateGrade(Grade grade, GradeDataRequestDTO requestDTO, Long schoolSubjectId) {
        grade.setValue(requestDTO.getValue());
        Student student = getStudentById(requestDTO.getStudentId());
        grade.setStudent(student);
        SchoolSubject subject = getSubjectById(schoolSubjectId);
        grade.setSchoolSubject(subject);
    }

    private void validateRequestedData(Long schoolId, Long schoolSubjectId, GradeDataRequestDTO requestDTO) {
        School school = schoolRepository.findById(schoolId).orElseThrow(() -> new SchoolNotFoundException(schoolId));
        SchoolSubject subject = getSubjectById(schoolSubjectId);
        Long studentId = requestDTO.getStudentId();
        Student student = getStudentById(studentId);
        if (!school.getSubjects().contains(subject)) {
            throw new SubjectNotInSchoolException(schoolId, schoolSubjectId);
        }
        if (!school.getStudents().contains(student)) {
            throw new StudentNotInSchoolException(schoolId, studentId);
        }
    }

    private SchoolSubject getSubjectById(Long schoolSubjectId) {
        return schoolSubjectRepository.findById(schoolSubjectId)
                .orElseThrow(() -> new SchoolSubjectNotFoundException(schoolSubjectId));
    }

    private Student getStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }
}
