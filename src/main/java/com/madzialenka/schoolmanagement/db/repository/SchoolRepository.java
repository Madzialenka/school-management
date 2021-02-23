package com.madzialenka.schoolmanagement.db.repository;

import com.madzialenka.schoolmanagement.db.entity.School;
import com.madzialenka.schoolmanagement.db.projection.SchoolSubjectGradesMeanProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {

    @Query(value = "select school_subjects.id schoolSubjectId, avg(grades.value) mean from school_subjects " +
            "left join grades on school_subjects.id = grades.subject_id where school_subjects.school_id = ? " +
            "group by school_subjects.id order by mean desc", nativeQuery = true)
    List<SchoolSubjectGradesMeanProjection> getSchoolSubjectsGradesMean(Long id);
}
