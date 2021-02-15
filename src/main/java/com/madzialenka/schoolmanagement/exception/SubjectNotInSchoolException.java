package com.madzialenka.schoolmanagement.exception;

public class SubjectNotInSchoolException extends RuntimeException {
    public SubjectNotInSchoolException(Long schoolId, Long schoolSubjectId) {
        super(String.format("Subject with id = %d does not exist in school wit id = %d", schoolSubjectId, schoolId));
    }
}
