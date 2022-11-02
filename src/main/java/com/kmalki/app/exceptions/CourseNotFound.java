package com.kmalki.app.exceptions;

import org.springframework.http.HttpStatus;

public class CourseNotFound extends ApiException {
    public CourseNotFound(String errorMessage, HttpStatus status) {
        super(errorMessage, status);
    }
}
