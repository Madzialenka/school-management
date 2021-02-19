package com.madzialenka.schoolmanagement.service.impl;

import com.madzialenka.schoolmanagement.api.dto.PageResponseDTO;
import com.madzialenka.schoolmanagement.api.dto.SchoolSimpleResponseDTO;
import com.madzialenka.schoolmanagement.api.dto.StudentDataRequestDTO;
import com.madzialenka.schoolmanagement.api.dto.StudentResponseDTO;
import com.madzialenka.schoolmanagement.db.entity.School;
import com.madzialenka.schoolmanagement.db.entity.Student;
import com.madzialenka.schoolmanagement.db.repository.SchoolRepository;
import com.madzialenka.schoolmanagement.db.repository.StudentRepository;
import com.madzialenka.schoolmanagement.exception.StudentAlreadyExistsException;
import com.madzialenka.schoolmanagement.exception.StudentNotFoundException;
import com.madzialenka.schoolmanagement.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {

    private static final String DEFAULT_SORT_BY = "id";
    private static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.ASC;
    public static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 3;
    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;

    @Override
    public StudentResponseDTO createStudent(StudentDataRequestDTO requestDTO) {
        validateStudentCreation(requestDTO);
        Student student = new Student();
        updateStudent(requestDTO, student);
        Student savedStudent = studentRepository.save(student);
        return createStudentResponseDTO(savedStudent);
    }

    @Override
    public PageResponseDTO<StudentResponseDTO> getStudents(String sortBy, Sort.Direction direction,
                                                           Integer pageNumber, Integer pageSize) {
        sortBy = Optional.ofNullable(sortBy).orElse(DEFAULT_SORT_BY);
        direction = Optional.ofNullable(direction).orElse(DEFAULT_SORT_DIRECTION);
        pageNumber = Optional.ofNullable(pageNumber).orElse(DEFAULT_PAGE_NUMBER);
        pageSize = Optional.ofNullable(pageSize).orElse(DEFAULT_PAGE_SIZE);
        PageRequest pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        Page<Student> students = studentRepository.findAll(pageable);
        List<StudentResponseDTO> elements = students.getContent().stream()
                .map(this::createStudentResponseDTO)
                .collect(Collectors.toList());
        return createPageResponseDTO(students, elements);
    }

    private PageResponseDTO<StudentResponseDTO> createPageResponseDTO(Page<Student> students,
                                                                      List<StudentResponseDTO> elements) {
        return PageResponseDTO.<StudentResponseDTO>builder()
                .elements(elements)
                .pageNumber(students.getNumber())
                .pageSize(students.getSize())
                .numberOfPages(students.getTotalPages())
                .numberOfElements(students.getTotalElements())
                .build();
    }

    @Override
    public StudentResponseDTO updateStudent(Long id, StudentDataRequestDTO requestDTO) {
        validateStudentUpdate(requestDTO, id);
        Student foundStudent = getStudentById(id);
        updateStudent(requestDTO, foundStudent);
        Student savedStudent = studentRepository.save(foundStudent);
        return createStudentResponseDTO(savedStudent);
    }

    private void validateStudentUpdate(StudentDataRequestDTO requestDTO, Long id) {
        String pesel = requestDTO.getPesel();
        String email = requestDTO.getEmail();
        if (!studentRepository.findByPeselOrEmailAndNotId(pesel, email, id).isEmpty()) {
            throw new StudentAlreadyExistsException(pesel, email);
        }
    }

    @Override
    public void deleteStudent(Long id) {
        Student foundStudent = getStudentById(id);
        studentRepository.delete(foundStudent);
    }

    private Student getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    private void validateStudentCreation(StudentDataRequestDTO requestDTO) {
        String pesel = requestDTO.getPesel();
        String email = requestDTO.getEmail();
        if (!studentRepository.findByPeselOrEmail(pesel, email).isEmpty()) {
            throw new StudentAlreadyExistsException(pesel, email);
        }
    }

    private StudentResponseDTO createStudentResponseDTO(Student savedStudent) {
        StudentResponseDTO responseDTO = new StudentResponseDTO();
        responseDTO.setId(savedStudent.getId());
        responseDTO.setSurname(savedStudent.getSurname());
        responseDTO.setName(savedStudent.getName());
        responseDTO.setEmail(savedStudent.getEmail());
        responseDTO.setPesel(savedStudent.getPesel());
        responseDTO.setGender(savedStudent.getGender());
        List<SchoolSimpleResponseDTO> schools = savedStudent.getSchools().stream()
                .map(this::createSchoolSimpleResponseDTO)
                .collect(Collectors.toList());
        responseDTO.setSchools(schools);
        return responseDTO;
    }

    private SchoolSimpleResponseDTO createSchoolSimpleResponseDTO(School school) {
        SchoolSimpleResponseDTO schoolResponseDTO = new SchoolSimpleResponseDTO();
        schoolResponseDTO.setId(school.getId());
        schoolResponseDTO.setTown(school.getTown());
        schoolResponseDTO.setSchoolNumber(school.getSchoolNumber());
        return schoolResponseDTO;
    }

    private void updateStudent(StudentDataRequestDTO requestDTO, Student student) {
        student.setSurname(requestDTO.getSurname());
        student.setName(requestDTO.getName());
        student.setEmail(requestDTO.getEmail());
        student.setPesel(requestDTO.getPesel());
        student.setGender(requestDTO.getGender());
        List<School> schools = schoolRepository.findAllById(requestDTO.getSchoolIds());
        student.setSchools(schools);
    }
}
