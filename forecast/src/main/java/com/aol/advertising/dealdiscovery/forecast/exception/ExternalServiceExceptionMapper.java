
package com.aol.advertising.dealdiscovery.forecast.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by mcordones13 on 6/3/16.
 */
public class ExternalServiceExceptionMapper implements ExceptionMapper<ExternalServiceException> {
    /**
     * Map an exception to a {@link Response}. Returning
     * {@code null} results in a {@link Response.Status#NO_CONTENT}
     * response. Throwing a runtime exception results in a
     * {@link Response.Status#INTERNAL_SERVER_ERROR} response.
     *
     * @param ex the exception to map to a response.
     * @return a response mapped from the supplied exception.
     */
    @Override
    public Response toResponse(final ExternalServiceException ex) {
        return Response.status(400).entity(ex.getFaultBean()).type(MediaType.APPLICATION_JSON).build();
    }
}
