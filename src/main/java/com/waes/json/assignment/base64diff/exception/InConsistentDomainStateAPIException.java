package com.waes.json.assignment.base64diff.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InConsistentDomainStateAPIException extends  RuntimeException {
    public InConsistentDomainStateAPIException() {
    }

    public InConsistentDomainStateAPIException(String message) {
        super(message);
    }

    public InConsistentDomainStateAPIException(String message, Throwable cause) {
        super(message, cause);
    }

    public InConsistentDomainStateAPIException(Throwable cause) {
        super(cause);
    }

    public InConsistentDomainStateAPIException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
