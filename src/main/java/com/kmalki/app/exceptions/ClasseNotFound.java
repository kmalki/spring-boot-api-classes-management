package com.kmalki.app.exceptions;

import org.springframework.http.HttpStatus;

public class ClasseNotFound extends ApiException {
    public ClasseNotFound(String errorMessage) {
        super(errorMessage, HttpStatus.NOT_FOUND);
    }
}
