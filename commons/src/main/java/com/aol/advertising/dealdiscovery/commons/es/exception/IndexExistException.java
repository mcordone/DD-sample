
package com.aol.advertising.dealdiscovery.commons.es.exception;

/**
 * Created by mcordones13 on 12/20/16.
 */
public class IndexExistException extends ElasticsearchException {

    public IndexExistException(){
        super();
    }

    public IndexExistException(String message){
        super(message);
    }
}
