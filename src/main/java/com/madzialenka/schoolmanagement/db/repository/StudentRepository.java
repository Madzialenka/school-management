package com.madzialenka.schoolmanagement.db.repository;

import com.madzialenka.schoolmanagement.db.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByPeselOrEmail(String pesel, String email);
}
