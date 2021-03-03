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

    @Query("select student from Student student join student.grades grade " +
            "where student.id in (:ids) group by student.id order by avg(grade.value) desc")
    List<Student> findAllByIdAndSortByBestGradesMean(@Param("ids") List<Long> bestStudentsIds);

    @Query("select student from Student student join student.grades grade " +
            "where student.id in (:ids) group by student.id order by avg(grade.value) asc")
    List<Student> findAllByIdAndSortByWorstGradesMean(@Param("ids") List<Long> worstStudentsIds);

    @Query(value = "select student_id from students_schools group by student_id " +
            "having count(school_id) >= (select count(school_id) from students_schools " +
            "group by student_id order by count(school_id) desc limit 1 offset :limit - 1)",
            nativeQuery = true)
    List<Long> getBusiestStudentsIds(@Param("limit") Long limit);

    @Query("select student from Student student join student.schools school " +
            "where student.id in (:ids) group by student.id order by count(school.id) desc")
    List<Student> findAllByIdAndSortBySchoolCount(@Param("ids") List<Long> busiestStudentsIds);
}
