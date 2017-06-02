
package com.aol.advertising.dealdiscovery.forecast.exception;

/**
 *
 * @author mcordones13
 */
public class FaultBean {
    private int code;
    private String message;
    private String detail;

    /**
     * @param code
     * @param message
     * @param detail
     */
    public FaultBean(int code, String message, String detail) {
        this.message = message;
        this.code = code;
        this.detail = detail;
    }

    /**
     *
     */
    public FaultBean() {

    }

    /**
     * @return the code
     */

    public int getCode() {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     *
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * @param detail the detail to set
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "FaultBean [code=" + code + ", message=" + message + ", detail=" + detail + "]";
    }

}
