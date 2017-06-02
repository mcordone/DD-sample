
package com.aol.advertising.dealdiscovery.forecast.domain.es;

import lombok.ToString;

import java.util.List;


/**
 * Created by mcordones13 on 6/21/16.
 */
@ToString
public class DealForecastBids {
    private Long providerId;
    private String dealId;
    private Long impressions;
    private Long uniques;
    private List<CountryBid> country;

    public DealForecastBids(){
    }
    
    public DealForecastBids(Long providerId, String dealId){
        this.providerId = providerId;
        this.dealId = dealId;
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

    public Long getImpressions() {
        return impressions;
    }

    public void setImpressions(Long impressions) {
        this.impressions = impressions;
    }

    public Long getUniques() {
        return uniques;
    }

    public void setUniques(Long uniques) {
        this.uniques = uniques;
    }

    public List<CountryBid> getCountry() {
        return country;
    }

    public void setCountry(List<CountryBid> country) {
        this.country = country;
    }
}
