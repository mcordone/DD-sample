
package com.aol.advertising.dealdiscovery.forecast.exception;

/**
 * Created by mcordones13 on 6/3/16.
 */
public class ExternalServiceException extends RuntimeException {

    private FaultBean faultBean;

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public ExternalServiceException() {
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public ExternalServiceException(String message) {
        super(message);
    }

    /**
     * Constructs a new runtime exception with application error code, specified message and
     * detail message
     * @param code   application specific error code
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     *
     * @since 1.4
     */
    public ExternalServiceException(int code, String message, String detail) {
        super(message);

        this.faultBean = new FaultBean(code, message, detail);
    }

    /**
     * @return
     */
    public FaultBean getFaultBean() {

        return faultBean;
    }
}
