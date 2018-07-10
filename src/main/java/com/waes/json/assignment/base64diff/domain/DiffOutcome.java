package com.waes.json.assignment.base64diff.domain;

import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.List;


public class DiffOutcome implements Serializable{
    private StatusCode statusCode;

    private List<Difference> differences;

    public DiffOutcome() {
    }

    public DiffOutcome(StatusCode statusCode, List<Difference> differences) {
        this.statusCode = statusCode;
        this.differences = differences;
    }

    @Override
    public String toString() {
        return "DiffOutcome{" +
                "statusCode=" + statusCode +
                ", differences=" + differences +
                '}';
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    @Nullable
    public List<Difference> getDifferences() {
        return differences;
    }

    public void setDifferences(List<Difference> differences) {
        this.differences = differences;
    }
}
