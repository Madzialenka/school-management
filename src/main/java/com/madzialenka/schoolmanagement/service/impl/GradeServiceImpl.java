package com.madzialenka.schoolmanagement.service.impl;

import com.madzialenka.schoolmanagement.api.dto.*;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GradeServiceImpl implements GradeService {

    private static final Sort.Direction DIRECTION = Sort.Direction.ASC;
    private static final String SORT_BY = "value";

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

    @Override
    public List<GradeSimpleResponseDTO> getGrades(Long schoolId, Long schoolSubjectId) {
        validateSubjectAndSchool(schoolId, schoolSubjectId);
        SchoolSubject subject = getSubjectById(schoolSubjectId);
        return gradeRepository.findBySchoolSubject(subject, Sort.by(DIRECTION, SORT_BY)).stream()
                .map(this::createGradeSimpleResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteGrade(Long schoolId, Long schoolSubjectId, Long gradeId) {
        validateSubjectAndSchool(schoolId, schoolSubjectId);
        Grade foundGrade = getGradeById(gradeId);
        gradeRepository.delete(foundGrade);
    }

    private Grade getGradeById(Long gradeId) {
        return gradeRepository.findById(gradeId).orElseThrow(() -> new GradeNotFoundException(gradeId));
    }

    private GradeSimpleResponseDTO createGradeSimpleResponseDTO(Grade grade) {
        GradeSimpleResponseDTO responseDTO = new GradeSimpleResponseDTO();
        responseDTO.setId(grade.getId());
        responseDTO.setValue(grade.getValue());
        StudentBasicDataResponseDTO student = createStudentBasicDataResponseDTO(grade);
        responseDTO.setStudent(student);
        return responseDTO;
    }

    private StudentBasicDataResponseDTO createStudentBasicDataResponseDTO(Grade grade) {
        StudentBasicDataResponseDTO responseDTO = new StudentBasicDataResponseDTO();
        Student student = grade.getStudent();
        responseDTO.setSurname(student.getSurname());
        responseDTO.setName(student.getName());
        responseDTO.setEmail(student.getEmail());
        return responseDTO;
    }

    private void validateSubjectAndSchool(Long schoolId, Long schoolSubjectId) {
        School school = getSchoolById(schoolId);
        getSubjectById(schoolSubjectId);
        if (schoolSubjectRepository.findByIdAndSchool(schoolSubjectId, school).isEmpty()) {
            throw new SubjectNotInSchoolException(schoolId, schoolSubjectId);
        }
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
        School school = getSchoolById(schoolId);
        getSubjectById(schoolSubjectId);
        Long studentId = requestDTO.getStudentId();
        getStudentById(studentId);
        if (schoolSubjectRepository.findByIdAndSchool(schoolSubjectId, school).isEmpty()) {
            throw new SubjectNotInSchoolException(schoolId, schoolSubjectId);
        }
        if (studentRepository.findByIdAndSchools_id(studentId, schoolId).isEmpty()) {
            throw new StudentNotInSchoolException(schoolId, studentId);
        }
    }

    private School getSchoolById(Long schoolId) {
        return schoolRepository.findById(schoolId).orElseThrow(() -> new SchoolNotFoundException(schoolId));
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
