
package com.aol.advertising.dealdiscovery.forecast.domain.ace;

/**
 * Created by mcordones13 on 7/11/16.
 */
public class Deal {
    private Long providerId;
    private String dealId;
    private Long adserviceId;

    public Deal() {}
    
    public Deal(Long providerId, String dealId) {
        this.providerId = providerId;
        this.dealId = dealId;
    }

    public Deal(Long providerId, String dealId, Long adserviceId) {
        this.providerId = providerId;
        this.dealId = dealId;
        this.adserviceId = adserviceId;
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
    
    public Long getAdserviceId() {
        return adserviceId;
    }

    public void setAdserviceId(Long adserviceId) {
        this.adserviceId = adserviceId;
    }

    @Override
    public String toString() {
        return "Deal{" +
                "providerId=" + providerId +
                ", dealId='" + dealId + '\'' +
                ", adserviceId='" + adserviceId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Deal deal = (Deal) o;

        if (!providerId.equals(deal.providerId)) return false;
        return dealId.equals(deal.dealId);

    }

    @Override
    public int hashCode() {
        int result = providerId.hashCode();
        result = 31 * result + dealId.hashCode();
        return result;
    }
}
