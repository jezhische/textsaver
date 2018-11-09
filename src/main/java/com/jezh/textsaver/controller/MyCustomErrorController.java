package com.jezh.textsaver.controller;

import com.jezh.textsaver.exception.ApiExceptionDetails;
import com.jezh.textsaver.exception.CustomRestExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

//@Controller
public class MyCustomErrorController implements ErrorController {

    @RequestMapping("/error")
    @ResponseBody
//    @ExceptionHandler({HttpRequestMethodNotSupportedException.class, HttpMediaTypeNotSupportedException.class, NoHandlerFoundException.class})
    public ResponseEntity<Object> handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        String message = String.format("<html><body><h2>Error Page</h2><div>Status code: <b>%s</b></div>"
                        + "<div>Exception Message: <b>%s</b></div><body></html>",
                statusCode, exception==null? "N/A": exception.getMessage());
        ApiExceptionDetails details = ApiExceptionDetails.builder()
                .errors(Arrays.asList(exception.getLocalizedMessage()))
                .message(message)
                .status(HttpStatus.valueOf(statusCode))
                .build();
        return ResponseEntity.status(HttpStatus.valueOf(statusCode)).headers(new HttpHeaders()).body(details);
    }

    @Autowired
    CustomRestExceptionHandler myErrorHandler;

//    @RequestMapping("/error")
//    @ResponseBody
//    public ResponseEntity<Object> handleError(Exception ex, WebRequest request) throws Exception {
//        return myErrorHandler.handleException(ex, request);
//    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
