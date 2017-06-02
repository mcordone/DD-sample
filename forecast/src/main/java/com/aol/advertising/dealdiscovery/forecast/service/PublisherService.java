
package com.aol.advertising.dealdiscovery.forecast.service;

import com.aol.advertising.dealdiscovery.forecast.dao.aceDao.AceDao;
import com.aol.advertising.dealdiscovery.forecast.dao.esDao.ESDao;
import com.aol.advertising.dealdiscovery.forecast.dao.lanaDao.LanaDao;
import com.aol.advertising.dealdiscovery.forecast.domain.es.CountryBid;
import com.aol.advertising.dealdiscovery.forecast.domain.ace.Deal;
import com.aol.advertising.dealdiscovery.forecast.domain.ace.Group;
import com.aol.advertising.dealdiscovery.forecast.domain.es.ForecastFilterResult;
import com.aol.advertising.dealdiscovery.forecast.domain.es.GroupForecastBids;
import com.aol.advertising.dealdiscovery.forecast.domain.lana.LanaNetworkResult;
import com.aol.advertising.dealdiscovery.forecast.domain.lana.NCRResult;
import com.aol.advertising.dealdiscovery.forecast.util.AdserviceUtils;
import io.searchbox.client.JestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by mcordones13 on 5/31/16.
 */

@Component
public class PublisherService {

    private static Logger LOG = LoggerFactory.getLogger(PublisherService.class);

    @Autowired
    private LanaDao lanaDao;

    @Autowired
    private ESDao esDao;

    @Autowired
    private AceDao aceDao;


    public JestResult generateLanaPublisherData() {
        List<Group> publisherList = getPublishersForLana();

        // retrieve Ace country data to populate country name
        List<CountryBid> aceCountryBids = aceDao.readAllCountries();
        Map<Long, String> aceCountryBidsMap =
                aceCountryBids.stream().collect(Collectors.toMap(CountryBid::getElementId, CountryBid::getCountryName));

        ForecastFilterResult publisherForecastResults = new ForecastFilterResult();
        // make lana call to get uniques, impressions and country info
        parseDataForPublishers(publisherList, publisherForecastResults, aceCountryBidsMap);

        return esDao.storePublisherForecastData(publisherForecastResults.getPublisherForecastMap());
    }

    /**
     * 
     * @param publisherList
     * @param filterResult
     */
    private void parseDataForPublishers(List<Group> publisherList, ForecastFilterResult filterResult, Map<Long, String> aceCountryBidsMap) {
        Map<Long, List<Deal>> dealsByAdserviceId;

        for (Group publisher : publisherList) {
            dealsByAdserviceId = AdserviceUtils.buildDealsListByAdserviceId(publisher.getDeals());
            
            if(dealsByAdserviceId != null){
                for(Map.Entry<Long, List<Deal>> entry: dealsByAdserviceId.entrySet()){
                    Long adserviceId = entry.getKey();
                    GroupForecastBids publisherForecastBidsByAdservicId = new GroupForecastBids();
                    publisherForecastBidsByAdservicId.setGroupName(publisher.getGroupName().toUpperCase());
                    NCRResult publisherForecasts = lanaDao.getAggregatedLanaGroupBidsAndUniques(entry.getValue());
                    NCRResult publisherBidByCountry = lanaDao.getGroupCountryBid(entry.getValue());
                    LinkedList<CountryBid> publisherCountryBids = filterResult.extractCountryBidsForGroup(publisherBidByCountry, aceCountryBidsMap);
                    publisherForecastBidsByAdservicId.setCountry(publisherCountryBids);
                    
                    long publisherBids = 0L;
                    long publisherUniques = 0L;
                    if (publisherForecasts.getResults() != null && !publisherForecasts.getResults().isEmpty()) {
                        for (LanaNetworkResult result : publisherForecasts.getResults()) {
                            if (result.getPath().contains("UserWeek")) {
                                publisherUniques = result.getAdjustedResult();
                            } else {
                                publisherBids += result.getAdjustedResult();
                            }
                        }
                        publisherBids = publisherBids / 7;
                        publisherForecastBidsByAdservicId.setImpressions(publisherBids);
                        publisherForecastBidsByAdservicId.setUniques(publisherUniques);
    
                    } else {
                        publisherForecastBidsByAdservicId.setImpressions(publisherBids);
                        publisherForecastBidsByAdservicId.setUniques(publisherUniques);
                    }
                    
                    filterResult.addPublisherForecastData(adserviceId, publisherForecastBidsByAdservicId);
    
                }
            }
        }
    }

    private List<Group> getPublishersForLana() {

        List<Group> lanaPublishers = new LinkedList<Group>();

        List<Group> acePublishers = aceDao.readAllPublishers();
        if (acePublishers == null || acePublishers.isEmpty()) {
            return Collections.emptyList();
        }
        List<Deal> aceDeals = new LinkedList<Deal>();

        acePublishers.forEach(publisher -> {
            aceDeals.addAll(publisher.getDeals());
        });

        List<Deal> esDeals = esDao.filterDealsByES(aceDeals);
        if (esDeals == null || esDeals.isEmpty()) {
            return Collections.emptyList();
        }

        for (Group publisher : acePublishers) {
            List<Deal> validDeals = publisher.getDeals().stream()
                    .filter(esDeals::contains)
                    .collect(Collectors.toList());
            if (validDeals != null && !validDeals.isEmpty()) {
                publisher.setDeals(validDeals);
                lanaPublishers.add(publisher);
            }
        }
        return lanaPublishers;
    }
}
