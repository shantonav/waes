package com.waes.json.assignment.base64diff.domain;


public enum SystemError {
    INPUT_INCOMPLETE_ERROR(225000,"Input is not complete"),
    INPUT_PATH_VARIABLE_ERROR(226000,"Input path variable is missing"),
    INPUT_DATA_ERROR(223000,"Input is not correct"),
    GENERAL_ERROR(222222,"Oops, shit happened (which always happens)"),
    HTTP_ERROR(333333,"HTTP_ERROR: read https://spring.io/understanding/REST");

    private Integer errorCode;
    private String errorMessage;

    private SystemError(final Integer errorCode,final String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "SystemError{" +
                "errorCode=" + errorCode +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }

    public Integer getErrorCode(){
        return this.errorCode;
    }

    public String getErrorMessage(){
        return this.errorMessage;
    }
}
