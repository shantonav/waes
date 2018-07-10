package com.waes.json.assignment.base64diff.domain;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.List;


public class ApiError implements Serializable{
    private Integer errorCode;
    private String systemErrorMessage;
    private String rootCause;
    private List<String> errors;

    public ApiError() {
    }

    public ApiError(Integer errorCode, String systemErrorMessage, String rootCause) {
        this.errorCode = errorCode;
        this.systemErrorMessage = systemErrorMessage;
        this.rootCause = rootCause;
    }

    public ApiError(Integer errorCode, String systemErrorMessage, String rootCause, List<String> errors) {
        this.errorCode = errorCode;
        this.systemErrorMessage = systemErrorMessage;
        this.rootCause = rootCause;
        this.errors = errors;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getSystemErrorMessage() {
        return systemErrorMessage;
    }

    public String getRootCause() {
        return rootCause;
    }

    @Nullable
    public List<String> getErrors() {
        return errors;
    }
}
