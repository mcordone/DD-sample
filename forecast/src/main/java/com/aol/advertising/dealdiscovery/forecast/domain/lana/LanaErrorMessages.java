
package com.aol.advertising.dealdiscovery.forecast.domain.lana;

/**
 * Created by mcordones13 on 6/10/16.
 */
public enum LanaErrorMessages {
    /**/
    LANA_SERVICE_CONFIGURATION_ERROR(52000, "LANA API configuration error: LANA base URL is null"),
    /**/
    LANA_SERVICE_ERROR(52001, "Unsuccessful response returned from LANA API"),
    /**/
    LANA_SERVICE_COMMUNICATION_ERROR(52002, "Error communicating with the LANA API at path "),
    /**/
    LANA_SERVICE_RESPONSE_ERROR(52003, "Error reading response text from Lana API response with status code: "),
    /**/
    LANA_SERVICE_REQUEST_ERROR(52004, "Error serializing Lana request");

    private final Integer errorCode;
    private final String errorMessage;

    /**
     * Constructs Validation Error with external Service Error Codes.
     *
     * @param errorCode errorCode
     * @param errorMessage errorMessage
     */
    LanaErrorMessages(final Integer errorCode,
                 final String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Integer getCode(){
        return errorCode;
    }

    public String getMessage(){
        return errorMessage;
    }
}
