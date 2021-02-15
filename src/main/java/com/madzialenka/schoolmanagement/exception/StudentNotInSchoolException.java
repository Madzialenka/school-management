package com.madzialenka.schoolmanagement.exception;

public class StudentNotInSchoolException extends RuntimeException {
    public StudentNotInSchoolException(Long schoolId, Long studentId) {
        super(String.format("Student with id = %d does not attend to school wit id = %d", studentId, schoolId));
    }
}
