package com.jezh.textsaver.exceptions;

import java.sql.SQLException;

public class RepositoryNotFoundException extends SQLException {

    public RepositoryNotFoundException() {
        super();
    }

    public RepositoryNotFoundException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
