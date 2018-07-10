package com.waes.json.assignment.base64diff.controller;

import com.waes.json.assignment.base64diff.domain.ApiError;
import com.waes.json.assignment.base64diff.domain.SystemError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {



    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        return new ResponseEntity(new  ApiError(SystemError.INPUT_INCOMPLETE_ERROR.getErrorCode(),
                SystemError.INPUT_INCOMPLETE_ERROR.getErrorMessage(),
                ex.getMessage().substring(0,ex.getMessage().indexOf(":"))), status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return new ResponseEntity(new  ApiError(SystemError.INPUT_PATH_VARIABLE_ERROR.getErrorCode(),
                SystemError.INPUT_PATH_VARIABLE_ERROR.getErrorMessage(),
                ex.getMessage().substring(0,ex.getMessage().indexOf(":"))), status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        return new ResponseEntity(new  ApiError(SystemError.INPUT_DATA_ERROR.getErrorCode(),
                SystemError.INPUT_DATA_ERROR.getErrorMessage(),
                "Constraint validation exception",errors), status);
    }


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body,
                                                             HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        if ( ex instanceof MethodArgumentTypeMismatchException){
            return new ResponseEntity(new  ApiError(SystemError.INPUT_DATA_ERROR.getErrorCode(),
                    SystemError.INPUT_DATA_ERROR.getErrorMessage(),
                    ex.getLocalizedMessage()), status);
        }else {
            return new ResponseEntity(new  ApiError(SystemError.GENERAL_ERROR.getErrorCode(),
                    SystemError.GENERAL_ERROR.getErrorMessage(),
                    ex.getLocalizedMessage()), status);
        }
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity(new  ApiError(SystemError.HTTP_ERROR.getErrorCode(),
                SystemError.HTTP_ERROR.getErrorMessage(),
                ex.getLocalizedMessage()), status);
    }


}
