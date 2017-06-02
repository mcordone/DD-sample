
package com.aol.advertising.dealdiscovery.forecast.domain.es;

import java.util.List;

/**
 * Created by mcordones13 on 7/18/16.
 */
public class PublisherForecastBids {
    private String publisher;
    private Long impressions;
    private Long uniques;
    private List<CountryBid> country;

    public PublisherForecastBids() {
    }

    public PublisherForecastBids(String publisher) {
        this.publisher = publisher;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
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
