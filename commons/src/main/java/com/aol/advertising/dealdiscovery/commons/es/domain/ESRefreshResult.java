
package com.aol.advertising.dealdiscovery.commons.es.domain;

import com.google.gson.Gson;
import io.searchbox.client.JestResult;

/**
 * Created by mcordones13 on 1/10/17.
 */
public class ESRefreshResult extends JestResult {

    public ESRefreshResult(JestResult source){
        super(source);
    }

    public ESRefreshResult(Gson gson) {
        super(gson);
    }
}
