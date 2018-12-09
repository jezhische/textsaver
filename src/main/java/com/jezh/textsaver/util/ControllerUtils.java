package com.jezh.textsaver.util;

import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

public class ControllerUtils {

    public static NoHandlerFoundException getNoHandlerFoundException(HttpServletRequest request) {
        return new NoHandlerFoundException(request.getMethod(), request.getRequestURL().toString(), new HttpHeaders());
    }
}
