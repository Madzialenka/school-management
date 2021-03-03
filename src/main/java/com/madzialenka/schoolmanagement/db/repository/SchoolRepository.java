package com.madzialenka.schoolmanagement.db.repository;

import com.madzialenka.schoolmanagement.db.entity.School;
import com.madzialenka.schoolmanagement.db.projection.SchoolSubjectCountProjection;
import com.madzialenka.schoolmanagement.db.projection.SchoolSubjectGradesMeanProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {

    @Query(value = "select school_subjects.id schoolSubjectId, avg(grades.value) mean from school_subjects " +
            "left join grades on school_subjects.id = grades.subject_id where school_subjects.school_id = ? " +
            "group by school_subjects.id order by mean desc", nativeQuery = true)
    List<SchoolSubjectGradesMeanProjection> getSchoolSubjectsGradesMean(Long id);

    @Query(value = "select grades.student_id from grades inner join school_subjects on grades.subject_id = " +
            "school_subjects.id where school_subjects.school_id = :id group by grades.student_id " +
            "having avg(grades.value) >= (select avg(grades.value) from grades inner join school_subjects " +
            "on grades.subject_id = school_subjects.id where school_subjects.school_id = :id " +
            "group by grades.student_id order by avg(grades.value) desc limit 1 offset :limit - 1)",
            nativeQuery = true)
    List<Long> getBestStudentsIds(@Param("id") Long id, @Param("limit") Long limit);

    @Query(value = "select grades.student_id from grades inner join school_subjects on grades.subject_id = " +
            "school_subjects.id where school_subjects.school_id = :id group by grades.student_id " +
            "having avg(grades.value) <= (select avg(grades.value) from grades inner join school_subjects " +
            "on grades.subject_id = school_subjects.id where school_subjects.school_id = :id " +
            "group by grades.student_id order by avg(grades.value) asc limit 1 offset :limit - 1)",
            nativeQuery = true)
    List<Long> getWorstStudentsIds(@Param("id") Long id, @Param("limit") Long limit);

    @Query(value = "select name subjectName, count(name) count from school_subjects group by subjectName " +
            "order by count desc limit :limit",
            nativeQuery = true)
    List<SchoolSubjectCountProjection> getSchoolSubjectCount(@Param("limit") Long limit);
}
