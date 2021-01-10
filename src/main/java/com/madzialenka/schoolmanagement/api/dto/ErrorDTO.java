package com.madzialenka.schoolmanagement.api.dto;

public class ErrorDTO {
    private String errorMessage;

    public ErrorDTO(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
