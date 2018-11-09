package com.jezh.textsaver.exception;

import com.jezh.textsaver.controller.TextCommonDataController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/** Handler for common REST exceptions handling extends {@code ResponseEntityExceptionHandler}.
 * The {@link #handleException(Exception, WebRequest)} method, that is the facade method of the parent
 * {@code ResponseEntityExceptionHandler} class, has a list of handled exceptions in its
 * {@link org.springframework.web.bind.annotation.ExceptionHandler} classes array for handling some standard
 * internal Spring MVC exceptions and appropriate methods to handle them. Since Spring doesn't let me to define
 * more than a single time an {@code Exception} class in a method annotated {@code @ExceptionHandler}, I must to
 * override these methods to override the behavior of appropriate exception handler.
 * For other exceptions I can create my own {@code @ExceptionHandler} handlers.
 * @author https://www.baeldung.com/global-error-handler-in-a-spring-rest-api
 * @author Ivan Kuchuhurnyi
 */

//@Order(value = Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(basePackageClasses = {TextCommonDataController.class})
//@Component
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

// ------------------------------------------------------------------------------------------------------ 400
    /** thrown when argument annotated with @Valid failed validation.
     * @see ApiExceptionDetails
     * @see #handleException(Exception, WebRequest)
     * @return a ResponseEntity instance containing all the basic exception info in the ApiExceptionDetails container
     * */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors())
            errors.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
        for (ObjectError objectError : ex.getBindingResult().getGlobalErrors())
            errors.add(objectError.getObjectName() + ": " + objectError.getDefaultMessage());
        ApiExceptionDetails exDetails = ApiExceptionDetails.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getLocalizedMessage())
                .errors(errors)
                .build();
        return handleExceptionInternal(ex, exDetails, headers, exDetails.getStatus(), request);
    }

// ------------------------------------------------------------------------------------------------------ 400
    /** reports the result of constraint violations.
     * @see ApiExceptionDetails
     * @return a ResponseEntity instance containing all the basic exception info in the ApiExceptionDetails container */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + "" + violation.getPropertyPath() + ": "
                    + violation.getMessage());
        }
        ApiExceptionDetails details = ApiExceptionDetails.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getLocalizedMessage())
                .errors(errors)
                .build();
        return
//                ResponseEntity.badRequest().body(details);
                ResponseEntity.status(details.getStatus()).headers(new HttpHeaders()).body(details);
//                new ResponseEntity<Object>(details, new HttpHeaders(), details.getStatus());
    }

// ------------------------------------------------------------------------------------------------------ 400
    /** thrown when the part of a multipart request not found
     * @see ApiExceptionDetails
     * @see #handleException(Exception, WebRequest)
     * @return a ResponseEntity instance containing all the basic exception info in the ApiExceptionDetails container */
    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
                                                               HttpStatus status, WebRequest request) {
        ApiExceptionDetails details = ApiExceptionDetails.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getLocalizedMessage())
                .errors(Arrays.asList("Parameters invalid. The " + ex.getVariableName() + " variable is missed. " +
                        "Please review the guideline"))
                .build();
        return new ResponseEntity<Object>(details, new HttpHeaders(), details.getStatus());
    }

// ------------------------------------------------------------------------------------------------------ 400
    /** thrown when method argument is not the expected type.
     * @see ApiExceptionDetails
     * @return a ResponseEntity instance containing all the basic exception info in the ApiExceptionDetails container */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String error = ex.getRequiredType() != null?
                ex.getName() + " should be of type " + ex.getRequiredType().getName()
                : "Unknown required type MethodArgumentTypeMismatchException";
        ApiExceptionDetails details = ApiExceptionDetails.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getLocalizedMessage())
                .errors(Arrays.asList(error))
                .build();
        return new ResponseEntity<Object>(details, new HttpHeaders(), details.getStatus());
    }


    //    =====================================================================
// ------------------------------------------------------------------------------- 404 DON'T WORK????
    /** thrown when no handlers found to fulfill the request. NB that 404 error is handled by DispatcherServlet
     * by default, so to throw this exception, it's required to set the property {@code setThrowExceptionIfNoHandlerFound}
     * of DispatcherServlet to true ( is set in {@link com.jezh.textsaver.TextsaverApplication}).
     * @see ApiExceptionDetails
     * @see #handleException(Exception, WebRequest)
     * @return a ResponseEntity instance containing all the basic exception info in the ApiExceptionDetails container */
    @Override
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
                                                                   HttpStatus status, WebRequest request) {
        String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
        ApiExceptionDetails details = ApiExceptionDetails.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(ex.getLocalizedMessage())
                .errors(Arrays.asList(error))
                .build();
        return handleExceptionInternal(ex, details, headers, status, request);
    }
//    =====================================================================

// ------------------------------------------------------------------------------- 404 DON'T WORK????
    /** thrown when no handlers found to fulfill the request. NB that 404 error is handled by DispatcherServlet
     * by default, so to throw this exception, it's required to set the property {@code setThrowExceptionIfNoHandlerFound}
     * of DispatcherServlet to true ( is set in {@link com.jezh.textsaver.TextsaverApplication}).
     * @see ApiExceptionDetails
     * @see #handleException(Exception, WebRequest)
     * @return a ResponseEntity instance containing all the basic exception info in the ApiExceptionDetails container */
        @ExceptionHandler({NoSuchElementException.class})
    //    @ResponseStatus(HttpStatus.NOT_FOUND)
        public ResponseEntity<Object> handleEntityNotFoundException(NoSuchElementException ex) {
            String error = "No handler found for " + ex.getLocalizedMessage();
            ApiExceptionDetails details = ApiExceptionDetails.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message(ex.getLocalizedMessage())
                    .errors(Arrays.asList(error))
                    .build();
            return new ResponseEntity<Object>(details, new HttpHeaders(), details.getStatus());
        }

// ------------------------------------------------------------------------------- 404 DON'T WORK????


// ------------------------------------------------------------------------------------------------------ 405
    /** thrown when one sends a requested with an unsupported HTTP method
     * @see ApiExceptionDetails
     * @see #handleException(Exception, WebRequest)
     * @return a ResponseEntity instance containing all the basic exception info in the ApiExceptionDetails container
     * */
    @Override
//    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatus status,
                                                                         WebRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(ex.getMethod()).append(" method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> sb.append(t + " "));
        ApiExceptionDetails details = ApiExceptionDetails.builder()
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .message(ex.getLocalizedMessage())
                .errors(Arrays.asList(sb.toString()))
                .build();
        return super.handleHttpRequestMethodNotSupported(ex, headers, status, request);
    }

//    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//    public ResponseEntity<Object> handleExcHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
//                                                                         WebRequest request) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(ex.getMethod()).append(" method is not supported for this request. Supported methods are ");
//        ex.getSupportedHttpMethods().forEach(t -> sb.append(t + " "));
//        ApiExceptionDetails details = ApiExceptionDetails.builder()
//                .status(HttpStatus.METHOD_NOT_ALLOWED)
//                .message(ex.getLocalizedMessage())
//                .errors(Arrays.asList(sb.toString()))
//                .build();
//        return ResponseEntity.status(details.getStatus()).headers(new HttpHeaders()).body(details);
//    }
}

