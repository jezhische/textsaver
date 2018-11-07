package com.jezh.textsaver.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

/** Auxiliary class for exception response content creating
 * */
@Getter
@AllArgsConstructor
@Builder
public class ApiExceptionDetails {

    private HttpStatus status;
    private String message;
    private List<String> errors;

//    public ApiExceptionDetails(HttpStatus status, String message, List<String> errors) {
//        this.status = status;
//        this.message = message;
//        this.errors = errors;
//    }

//    public ApiExceptionDetails(HttpStatus status, String message, String error) {
//        this.status = status;
//        this.message = message;
//        this.errors = Arrays.asList(error);
//    }
}
