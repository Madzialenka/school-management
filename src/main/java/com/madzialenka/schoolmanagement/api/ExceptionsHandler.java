package com.madzialenka.schoolmanagement.api;

import com.madzialenka.schoolmanagement.api.dto.ErrorDTO;
import com.madzialenka.schoolmanagement.exception.SchoolNotFoundException;
import com.madzialenka.schoolmanagement.exception.StudentAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({SchoolNotFoundException.class})
    public ErrorDTO handleSchoolNotFoundException(SchoolNotFoundException e) {
        return new ErrorDTO(e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({StudentAlreadyExistsException.class})
    public ErrorDTO handleStudentAlreadyExistsException(StudentAlreadyExistsException e) {
        return new ErrorDTO(e.getMessage());
    }
}
