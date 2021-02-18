package com.madzialenka.schoolmanagement.db.repository;

import com.madzialenka.schoolmanagement.db.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByPeselOrEmail(String pesel, String email);

    Optional<Student> findByIdAndSchools_id(Long studentId, Long schoolId);

    @Query("select student from Student student where (student.pesel = :pesel or student.email = :email) " +
            "and student.id <> :id")
    List<Student> findByPeselOrEmailAndNotId(@Param("pesel") String pesel, @Param("email") String email,
                                             @Param("id") Long id);
}
