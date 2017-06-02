
package com.aol.advertising.dealdiscovery.forecast.domain.es;

import com.aol.advertising.dealdiscovery.forecast.domain.lana.DataTypes;
import com.aol.advertising.dealdiscovery.forecast.domain.lana.DealLanaResponse;
import com.aol.advertising.dealdiscovery.forecast.domain.lana.LanaNetworkResult;
import com.aol.advertising.dealdiscovery.forecast.domain.lana.NCRResult;
import com.aol.advertising.dealdiscovery.forecast.util.CountryAdservice;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Collection;

/**
 * Created by mcordones13 on 6/21/16.
 */

public class ForecastFilterResult {
    private Map<String, DealForecastBids> dealsForecastMap;

    private Map<String, LinkedList<GroupForecastBids>> publisherForecastMap;
    
    private Map<String, LinkedList<GroupForecastBids>> packageForecastMap;

    public ForecastFilterResult(){
        dealsForecastMap = new HashMap<String, DealForecastBids>();
        publisherForecastMap = new HashMap<String, LinkedList<GroupForecastBids>>();
        packageForecastMap = new HashMap<String, LinkedList<GroupForecastBids>>();

    }
    
    public void addDealForecastData (DealLanaResponse dlr){
        String mapKey =  dlr.getProviderId() + "_" + dlr.getDealId();
        DealForecastBids dealForecastBids;
        
        if(dealsForecastMap.containsKey(mapKey)){
            dealForecastBids = dealsForecastMap.get(mapKey);
        }
        else{
            dealForecastBids = new DealForecastBids(dlr.getProviderId(), dlr.getDealId());
        }

        if(dlr.getType().equals(DataTypes.DEAL_UNIQUE)){
            //forecast.setUniqueUserForecast(dlr.getForecast());
            dealForecastBids.setUniques(dlr.getForecast());
        }
        else{
          //each entry is that day's average, to get a weekely average we devide each forecast by 7
            //dealForecastBids.setImpressions(((forecast.getBidForecast()==null)?0:forecast.getBidForecast()/7) + dlr.getForecast());
            Long imprs = ((dealForecastBids.getImpressions() ==null) ? 0 :dealForecastBids.getImpressions() / 7) + dlr.getForecast();
            dealForecastBids.setImpressions(imprs);
        }

        dealsForecastMap.put(mapKey, dealForecastBids);
    }

    public Map<String, LinkedList<CountryBid>> processCountryBidsForDeal (NCRResult ncrResult) {
        Map<String, LinkedList<CountryBid>> dealCountryBidMap = new HashMap<String, LinkedList<CountryBid>>();

        Collection<LanaNetworkResult> lanaResults = ncrResult.getResults();
        for (LanaNetworkResult lanaNetworkResult : lanaResults) {
            String path = lanaNetworkResult.getPath();
            String key = path.split("transactionalElementId/27303:")[1].replace(":", "_");

            Long elementId = Long.parseLong(path.split("elementId:")[1].split("/")[0]);
            Long bid = lanaNetworkResult.getAdjustedResult();
            CountryBid countryBid = new CountryBid(elementId, bid, null); //fixme add country name

            LinkedList<CountryBid> newCountryBidList = null;
            if (dealCountryBidMap.containsKey(key)) {
                newCountryBidList  = dealCountryBidMap.get(key);
            } else {
                newCountryBidList = new LinkedList<CountryBid>();
            }
            newCountryBidList.add(countryBid);
            dealCountryBidMap.put(key, newCountryBidList);
        }

        return dealCountryBidMap;
    }

    public LinkedList<CountryBid> extractCountryBidsForGroup(NCRResult ncrResult, Map<Long, String> aceCountryBidsMap) {
        LinkedList<CountryBid> groupCountryBids = new LinkedList<CountryBid>();

        Collection<LanaNetworkResult> lanaResults = ncrResult.getResults();
        for (LanaNetworkResult lanaNetworkResult : lanaResults) {
            String path = lanaNetworkResult.getPath();

            Long countryId = Long.parseLong(path.split("elementId:")[1]);
            String countryName = aceCountryBidsMap.get(countryId);
            Long bid = lanaNetworkResult.getAdjustedResult();

            groupCountryBids.add(new CountryBid(countryId, bid, countryName));
        }

        return groupCountryBids;
    }

    public void addPublisherForecastData(Long adserviceId, GroupForecastBids publisherForecast){
        String adserviceCountryName = CountryAdservice.findForAdserviceId(adserviceId).getCountry();
        
        LinkedList<GroupForecastBids> adservicePackages = publisherForecastMap.get(adserviceCountryName);
        if(adservicePackages == null){
            adservicePackages = new LinkedList<GroupForecastBids>();
        }
        adservicePackages.add(publisherForecast);
        publisherForecastMap.put(adserviceCountryName, adservicePackages);
    }
    
    public void addPackageForecastData(Long adserviceId, GroupForecastBids packageForecast){
        String adserviceCountryName = CountryAdservice.findForAdserviceId(adserviceId).getCountry();

        LinkedList<GroupForecastBids> adservicePackages = packageForecastMap.get(adserviceCountryName);
        if(adservicePackages == null){
            adservicePackages = new LinkedList<GroupForecastBids>();
        }
        adservicePackages.add(packageForecast);
        packageForecastMap.put(adserviceCountryName, adservicePackages);
    }
    
    public Map<String, DealForecastBids> getDealsForecastMap(){
        return dealsForecastMap;
    }

    public Map<String, LinkedList<GroupForecastBids>> getPublisherForecastMap(){
        return publisherForecastMap;
    }
    
    public Collection<DealForecastBids> getForecasts(){
        return dealsForecastMap.values();
    }
    
    public Map<String, LinkedList<GroupForecastBids>> getPackageForecastMap() { 
        return packageForecastMap;
    }
    
}
