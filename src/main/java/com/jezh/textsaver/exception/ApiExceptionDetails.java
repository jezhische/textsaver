package com.jezh.textsaver.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
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
    /** The singular annotation is used together with @Builder to create single element 'add' methods in the builder
     * for collections. That is it gives a possibility to use chaining to get similar to {@code vararg} results */
    @Singular
    private List<String> errors;
}
