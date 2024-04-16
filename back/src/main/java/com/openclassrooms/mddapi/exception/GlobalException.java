package com.openclassrooms.mddapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class GlobalException extends RuntimeException {

    @Getter
    private final HttpStatus httpStatus;

    private final String message;

    public GlobalException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
