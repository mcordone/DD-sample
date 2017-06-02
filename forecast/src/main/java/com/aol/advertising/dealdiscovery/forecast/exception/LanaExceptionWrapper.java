
package com.aol.advertising.dealdiscovery.forecast.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.util.List;

/**
 * Created by mcordones13 on 6/3/16.
 */
@ToString
public class LanaExceptionWrapper {

    private String errorCode;
    @JsonProperty("message")
    private List<FieldError> fieldErrors;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}