package com.madzialenka.schoolmanagement.db.repository;

import com.madzialenka.schoolmanagement.db.entity.School;
import com.madzialenka.schoolmanagement.db.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByPeselOrEmail(String pesel, String email);

    Optional<Student> findByIdAndSchool(Long studentId, School school);
}
