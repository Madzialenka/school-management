package com.madzialenka.schoolmanagement.exception;

public class SchoolNotFoundException extends RuntimeException {
    public SchoolNotFoundException(Long id) {
        super(String.format("School with id: %d not found", id));
    }
}
