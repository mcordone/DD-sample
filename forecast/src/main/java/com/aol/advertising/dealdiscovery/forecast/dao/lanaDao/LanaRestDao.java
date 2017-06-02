
package com.aol.advertising.dealdiscovery.forecast.dao.lanaDao;

import com.aol.advertising.dealdiscovery.forecast.exception.ExternalServiceException;
import com.aol.advertising.dealdiscovery.forecast.exception.LanaExceptionWrapper;

import org.glassfish.jersey.client.ClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

/**
 * Created by mcordones13 on 6/1/16.
 */
@Component
public class LanaRestDao implements InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(LanaRestDao.class);

    private Client client;

    /**
     * Invoked by a BeanFactory after it has set all bean properties supplied
     * (and satisfied BeanFactoryAware and ApplicationContextAware).
     * <p>This method allows the bean instance to perform initialization only
     * possible when all bean properties have been set and to throw an
     * exception in the event of misconfiguration.
     *
     * @throws Exception in the event of misconfiguration (such
     *                   as failure to set an essential property) or if initialization fails.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        client = ClientBuilder.newClient();
        client.property(ClientProperties.CONNECT_TIMEOUT, 0);
        client.property(ClientProperties.READ_TIMEOUT, 0);
    }

    /**
     * POST request to LANA
     * @param url
     * @param entity
     * @param headers
     * @param returnType
     * @param <T>
     * @return
     */
    public <T> T post(String url, Object entity, MultivaluedMap<String,String> headers, Class<T> returnType) {
        WebTarget wt = client.target(url);
        MediaType mediaType = new MediaType(headers.getFirst("mediaType"), headers.getFirst("mediaSubType"));

        Invocation.Builder builder = wt.request().accept(headers.getFirst("accept"));

        try {
            Response response = builder.post(Entity.entity(entity, mediaType));
            return processResponse(response, returnType);
        }
        catch(ProcessingException e) {
            LOG.info("***************** ProcessingException " + e.getMessage());
            throw new ExternalServiceException(com.aol.advertising.dealdiscovery.forecast.domain.lana.LanaErrorMessages.LANA_SERVICE_COMMUNICATION_ERROR.getCode(),
                    com.aol.advertising.dealdiscovery.forecast.domain.lana.LanaErrorMessages.LANA_SERVICE_COMMUNICATION_ERROR.getMessage() + url,
                    e.getMessage());
        }
    }

    /**
     * @param resp - Response Object returned LANA POST request.
     * @param type - The expected return Type.
     */
    private <T> T processResponse(Response resp, Class<T> type) {
        switch (resp.getStatusInfo().getFamily()){
            case SUCCESSFUL:
                return resp.readEntity(type);
            default:
                throw handleLanaRestException(resp);

        }
    }

    /**
     *
     * @param resp
     * @return ExternalServiceException
     */
    private ExternalServiceException handleLanaRestException(final Response resp){
        LanaExceptionWrapper lanaExceptionWrapper = new LanaExceptionWrapper();

        if (resp.hasEntity()) {
            try {
                lanaExceptionWrapper = resp.readEntity(LanaExceptionWrapper.class);
            }
            catch (ProcessingException | IllegalStateException e) {
                Response.StatusType status = resp.getStatusInfo();

                LOG.error(com.aol.advertising.dealdiscovery.forecast.domain.lana.LanaErrorMessages.LANA_SERVICE_RESPONSE_ERROR.getMessage(),
                        status.getStatusCode(), e);

                throw new ExternalServiceException(com.aol.advertising.dealdiscovery.forecast.domain.lana.LanaErrorMessages.LANA_SERVICE_RESPONSE_ERROR.getCode(),
                        com.aol.advertising.dealdiscovery.forecast.domain.lana.LanaErrorMessages.LANA_SERVICE_RESPONSE_ERROR.getMessage() + status.getStatusCode(),
                        e.getMessage());
            }
        }

        throw new ExternalServiceException(com.aol.advertising.dealdiscovery.forecast.domain.lana.LanaErrorMessages.LANA_SERVICE_ERROR.getCode(),
                com.aol.advertising.dealdiscovery.forecast.domain.lana.LanaErrorMessages.LANA_SERVICE_ERROR.getMessage(),
                lanaExceptionWrapper.getErrorCode());
    }
}
