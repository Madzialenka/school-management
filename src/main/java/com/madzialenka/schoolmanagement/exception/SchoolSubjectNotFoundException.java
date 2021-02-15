package com.madzialenka.schoolmanagement.exception;

public class SchoolSubjectNotFoundException extends RuntimeException {
    public SchoolSubjectNotFoundException(Long schoolSubjectId) {
        super(String.format("School Subject with id: %d not found", schoolSubjectId));
    }
}
