package com.waes.json.assignment.base64diff.domain;


public enum StatusCode {
    OBJECTS_SAME("Left and right data are the same"),
    OBJECT_NOT_SAME_LENGTH("Left and right data are not of the same length"),
    OBJECT_HAS_DIFFERENCES("Left and right data have differences");

    private String message;

    private StatusCode(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
