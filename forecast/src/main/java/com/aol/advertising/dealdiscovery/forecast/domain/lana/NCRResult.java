
package com.aol.advertising.dealdiscovery.forecast.domain.lana;

import java.util.Collection;

/**
 * Created by mcordones13 on 6/2/16.
 */

public class NCRResult {

    private long executionTime;

    private Collection<LanaNetworkResult> results;

    public Collection<LanaNetworkResult> getResults() {
        return results;
    }

    public void setResults(Collection<LanaNetworkResult> results) {
        this.results = results;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    @Override
    public String toString() {
        return "NCRResult{" +
                "executionTime=" + executionTime +
                ", results=" + results +
                '}';
    }
}
