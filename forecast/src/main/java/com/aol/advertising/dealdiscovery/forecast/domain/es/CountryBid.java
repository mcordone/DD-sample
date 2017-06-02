
package com.aol.advertising.dealdiscovery.forecast.domain.es;

/**
 * Created by mcordones13 on 9/15/16.
 */
public class CountryBid {
    private Long elementId;
    private Long bid;
    private String countryName;

    public CountryBid() {}

    public CountryBid(Long elementId, Long bid, String countryName) {
        this.countryName = countryName;
        this.bid = bid;
        this.elementId = elementId;
    }

    public Long getElementId() {
        return elementId;
    }

    public void setElementId(Long elementId) {
        this.elementId = elementId;
    }

    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Override
    public String toString() {
        return "CountryBid{" +
                "elementId=" + elementId +
                ", bid=" + bid +
                ", countryName='" + countryName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CountryBid that = (CountryBid) o;

        if (elementId != null ? !elementId.equals(that.elementId) : that.elementId != null) return false;
        if (bid != null ? !bid.equals(that.bid) : that.bid != null) return false;
        return countryName != null ? countryName.equals(that.countryName) : that.countryName == null;

    }

    @Override
    public int hashCode() {
        int result = elementId != null ? elementId.hashCode() : 0;
        result = 31 * result + (bid != null ? bid.hashCode() : 0);
        result = 31 * result + (countryName != null ? countryName.hashCode() : 0);
        return result;
    }
}
