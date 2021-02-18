package com.madzialenka.schoolmanagement.exception;

public class GradeNotFoundException extends RuntimeException {
    public GradeNotFoundException(Long id) {
        super(String.format("Grade with id: %d not found", id));
    }
}
