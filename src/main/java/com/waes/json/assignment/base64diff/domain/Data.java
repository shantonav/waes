package com.waes.json.assignment.base64diff.domain;

import java.io.Serializable;


public class Data implements Serializable{

    private String base64EncodedString;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Data)) return false;

        Data data = (Data) o;

        return base64EncodedString != null ? base64EncodedString.equals(data.base64EncodedString) : data.base64EncodedString == null;

    }

    @Override
    public int hashCode() {
        return base64EncodedString != null ? base64EncodedString.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Data{" +
                "base64EncodedString='" + base64EncodedString + '\'' +
                '}';
    }

    @Base64InputValidation
    public String getBase64EncodedString() {
        return base64EncodedString;
    }

    public void setBase64EncodedString(String base64EncodedString) {
        this.base64EncodedString = base64EncodedString;
    }
}
