
package com.aol.advertising.dealdiscovery.forecast.domain.lana;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mcordones13 on 6/2/16.
 */
public class LanaNetworkResult {

    @JsonProperty("potential-result")
    private Long potentialResult;

    @JsonProperty("target-result")
    private Long targetResult;

    @JsonProperty("adjusted-result")
    private Long adjustedResult;

    @JsonProperty("path")
    private String path;


    public Long getPotentialResult() {
        return potentialResult;
    }

    public void setPotentialResult(Long potentialResult) {
        this.potentialResult = potentialResult;
    }

    public Long getTargetResult() {
        return targetResult;
    }

    public void setTargetResult(Long targetResult) {
        this.targetResult = targetResult;
    }

    public Long getAdjustedResult() {
        return adjustedResult;
    }

    public void setAdjustedResult(Long adjustedResult) {
        this.adjustedResult = adjustedResult;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "LanaNetworkResult{" +
                "potentialResult=" + potentialResult +
                ", targetResult=" + targetResult +
                ", adjustedResult=" + adjustedResult +
                ", path='" + path + '\'' +
                '}';
    }
}