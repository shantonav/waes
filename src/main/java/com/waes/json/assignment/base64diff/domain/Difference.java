package com.waes.json.assignment.base64diff.domain;

import java.io.Serializable;


public class Difference implements Serializable{

    private Integer offset;




    private Boolean textsAreOfDiffLength = Boolean.FALSE;

    public Boolean getTextsAreOfDiffLength() {
        return textsAreOfDiffLength;
    }


    public Difference(Integer offset) {
        this.offset = offset;
    }

    public Difference(Boolean textsAreOfDiffLength) {
        this.textsAreOfDiffLength = textsAreOfDiffLength;
    }


    public Integer getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return "Difference{" +
                "offset=" + offset +
                ", textsAreOfDiffLength=" + textsAreOfDiffLength +
                '}';
    }
}
