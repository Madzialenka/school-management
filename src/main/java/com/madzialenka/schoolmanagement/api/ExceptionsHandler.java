package com.madzialenka.schoolmanagement.api;

import com.madzialenka.schoolmanagement.api.dto.ErrorDTO;
import com.madzialenka.schoolmanagement.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({SchoolNotFoundException.class, StudentNotFoundException.class,
            SchoolSubjectNotFoundException.class, GradeNotFoundException.class})
    public ErrorDTO handleNotFoundException(RuntimeException e) {
        return new ErrorDTO(e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({StudentAlreadyExistsException.class})
    public ErrorDTO handleStudentAlreadyExistsException(StudentAlreadyExistsException e) {
        return new ErrorDTO(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler({StudentNotInSchoolException.class, SubjectNotInSchoolException.class})
    public ErrorDTO handleNotInSchoolException(RuntimeException e) {
        return new ErrorDTO(e.getMessage());
    }
}
