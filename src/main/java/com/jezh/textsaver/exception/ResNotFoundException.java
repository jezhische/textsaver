package com.jezh.textsaver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLException;

//@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "There is no such resource")
public class ResNotFoundException extends RuntimeException {

    public ResNotFoundException() {
        super();
    }

    public ResNotFoundException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
