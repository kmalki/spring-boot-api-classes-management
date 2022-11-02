package com.kmalki.app.exceptions;

import org.springframework.http.HttpStatus;

public class TeacherNotQualified extends ApiException {
    public TeacherNotQualified(String errorMessage) {
        super(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
