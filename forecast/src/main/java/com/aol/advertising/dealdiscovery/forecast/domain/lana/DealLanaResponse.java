
package com.aol.advertising.dealdiscovery.forecast.domain.lana;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mcordones13 on 6/22/16.
 */

public class DealLanaResponse {

    private static Logger LOG = LoggerFactory.getLogger(DealLanaResponse.class);

    private Long providerId;
    private String dealId;
    private Long forecast;
    private DataTypes type;

    public DealLanaResponse(LanaNetworkResult lnr) {
        String path = lnr.getPath();
        path = path.substring(path.indexOf(":") + 1, path.length());
        String pathProviderId = path.substring(0, path.indexOf(":"));
        try {
            this.providerId = Long.parseLong(pathProviderId);
        } catch (Exception ex) {
            LOG.warn("Not able to convert provider Id from path: " + path);
        }
        this.dealId = path.substring(path.indexOf(":") + 1, path.length());
        this.forecast = lnr.getAdjustedResult();
        this.type = lnr.getPath().contains("UserWeek") ? DataTypes.DEAL_UNIQUE : DataTypes.DEAL_BID;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public Long getForecast() {
        return forecast;
    }

    public void setForecast(Long forecast) {
        this.forecast = forecast;
    }

    public DataTypes getType() {
        return type;
    }

    public void setType(DataTypes type) {
        this.type = type;
    }

}
