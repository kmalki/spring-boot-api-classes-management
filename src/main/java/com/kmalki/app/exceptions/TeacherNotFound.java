package com.kmalki.app.exceptions;

import org.springframework.http.HttpStatus;

public class TeacherNotFound extends ApiException {
    public TeacherNotFound(Long id) {
        super(String.format("Teacher id %d not found", id), HttpStatus.NOT_FOUND);
    }
}
