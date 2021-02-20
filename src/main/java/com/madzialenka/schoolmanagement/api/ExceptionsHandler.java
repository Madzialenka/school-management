package com.madzialenka.schoolmanagement.api;

import com.madzialenka.schoolmanagement.api.dto.ErrorDTO;
import com.madzialenka.schoolmanagement.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionsHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({SchoolNotFoundException.class, StudentNotFoundException.class,
            SchoolSubjectNotFoundException.class, GradeNotFoundException.class})
    public ErrorDTO handleNotFoundException(RuntimeException e) {
        log.error("handleNotFoundException", e);
        return new ErrorDTO(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        return new ErrorDTO(e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(";\n")));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({StudentAlreadyExistsException.class})
    public ErrorDTO handleStudentAlreadyExistsException(StudentAlreadyExistsException e) {
        log.error("handleStudentAlreadyExistsException", e);
        return new ErrorDTO(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler({StudentNotInSchoolException.class, SubjectNotInSchoolException.class})
    public ErrorDTO handleNotInSchoolException(RuntimeException e) {
        log.error("handleNotInSchoolException", e);
        return new ErrorDTO(e.getMessage());
    }
}
