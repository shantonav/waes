package com.waes.json.assignment.base64diff.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class Binary {

    @Id
    private Integer id;
    private String base64DecodedStringLeftData;
    private String base64DecodedStringRightData;

    public String getBase64DecodedStringRightData() {
        return base64DecodedStringRightData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Binary)) return false;

        Binary binary = (Binary) o;

        if (id != null ? !id.equals(binary.id) : binary.id != null) return false;
        if (base64DecodedStringLeftData != null ? !base64DecodedStringLeftData.equals(binary.base64DecodedStringLeftData) : binary.base64DecodedStringLeftData != null)
            return false;
        return base64DecodedStringRightData != null ? base64DecodedStringRightData.equals(binary.base64DecodedStringRightData) : binary.base64DecodedStringRightData == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (base64DecodedStringLeftData != null ? base64DecodedStringLeftData.hashCode() : 0);
        result = 31 * result + (base64DecodedStringRightData != null ? base64DecodedStringRightData.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Binary{" +
                "id=" + id +
                ", base64DecodedStringLeftData='" + base64DecodedStringLeftData + '\'' +
                ", base64DecodedStringRightData='" + base64DecodedStringRightData + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBase64DecodedStringLeftData() {
        return base64DecodedStringLeftData;
    }

    public void setBase64DecodedStringLeftData(String base64DecodedStringLeftData) {
        this.base64DecodedStringLeftData = base64DecodedStringLeftData;
    }

    public void setBase64DecodedStringRightData(String base64DecodedStringRightData) {
        this.base64DecodedStringRightData = base64DecodedStringRightData;
    }
}
