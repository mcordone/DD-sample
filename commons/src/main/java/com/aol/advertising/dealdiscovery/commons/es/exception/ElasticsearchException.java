
package com.aol.advertising.dealdiscovery.commons.es.exception;

/**
 * Created by mcordones13 on 12/20/16.
 */
public class ElasticsearchException extends RuntimeException {

    public ElasticsearchException(){
        super();
    }

    public ElasticsearchException(String message){
        super(message);
    }

    public ElasticsearchException(String message, Throwable cause){
        super(message, cause);
    }
}
