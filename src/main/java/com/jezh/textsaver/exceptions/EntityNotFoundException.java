package com.jezh.textsaver.exceptions;

import java.sql.SQLException;

public class EntityNotFoundException extends SQLException {

    public EntityNotFoundException() {
        super();
    }

    public EntityNotFoundException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
