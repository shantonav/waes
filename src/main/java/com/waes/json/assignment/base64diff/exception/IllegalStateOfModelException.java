package com.waes.json.assignment.base64diff.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;



public class IllegalStateOfModelException extends  RuntimeException{
    public IllegalStateOfModelException() {
    }

    public IllegalStateOfModelException(String message) {
        super(message);
    }

    public IllegalStateOfModelException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalStateOfModelException(Throwable cause) {
        super(cause);
    }

    public IllegalStateOfModelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
