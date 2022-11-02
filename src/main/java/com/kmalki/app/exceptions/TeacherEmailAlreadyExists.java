package com.kmalki.app.exceptions;

import org.springframework.http.HttpStatus;

public class TeacherEmailAlreadyExists extends ApiException {
    public TeacherEmailAlreadyExists(String errorMessage, HttpStatus status) {
        super(errorMessage, status);
    }
}
