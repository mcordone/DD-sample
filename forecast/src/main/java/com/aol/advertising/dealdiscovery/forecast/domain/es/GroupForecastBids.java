
package com.aol.advertising.dealdiscovery.forecast.domain.es;

import java.util.List;

/**
 * Created by mcordones13 on 12/16/16.
 */
public class GroupForecastBids {
    private String groupName;
    private Long impressions;
    private Long uniques;
    private List<CountryBid> country;

    public GroupForecastBids() {
    }
   
    public GroupForecastBids(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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
