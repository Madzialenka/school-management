package com.madzialenka.schoolmanagement.db.repository;

import com.madzialenka.schoolmanagement.db.entity.School;
import com.madzialenka.schoolmanagement.db.entity.SchoolSubject;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolSubjectRepository extends JpaRepository<SchoolSubject, Long> {
    List<SchoolSubject> findBySchool(School school, Sort sort);

    Optional<SchoolSubject> findByIdAndSchool(Long schoolSubjectId, School school);
}
