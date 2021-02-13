package com.madzialenka.schoolmanagement.exception;

public class StudentAlreadyExistsException extends RuntimeException{
    public StudentAlreadyExistsException(String pesel, String email) {
        super(String.format("Student with pesel = %s or with email = %s already exists", pesel, email));
    }
}
