
package com.aol.advertising.dealdiscovery.forecast.service;

import io.searchbox.client.JestResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aol.advertising.dealdiscovery.forecast.dao.aceDao.AceDao;
import com.aol.advertising.dealdiscovery.forecast.dao.esDao.ESDao;
import com.aol.advertising.dealdiscovery.forecast.dao.lanaDao.LanaDao;
import com.aol.advertising.dealdiscovery.forecast.domain.ace.Deal;
import com.aol.advertising.dealdiscovery.forecast.domain.ace.Group;
import com.aol.advertising.dealdiscovery.forecast.domain.es.CountryBid;
import com.aol.advertising.dealdiscovery.forecast.domain.es.ForecastFilterResult;
import com.aol.advertising.dealdiscovery.forecast.domain.es.GroupForecastBids;
import com.aol.advertising.dealdiscovery.forecast.domain.lana.LanaNetworkResult;
import com.aol.advertising.dealdiscovery.forecast.domain.lana.NCRResult;
import com.aol.advertising.dealdiscovery.forecast.util.AdserviceUtils;

/**
 * Created by mcordones13 on 12/14/16.
 */
@Component
public class PackageService {
    private static Logger LOG = LoggerFactory.getLogger(PackageService.class);

    @Autowired
    private LanaDao lanaDao;

    @Autowired
    private ESDao esDao;

    @Autowired
    private AceDao aceDao;
    
    public JestResult generateLanaPackageData() {
        List<Group> packageList = getPackagesForLana();
        
        // retrieve Ace country data to populate country name
        List<CountryBid> aceCountryBids = aceDao.readAllCountries();
        Map<Long, String> aceCountryBidsMap =
                aceCountryBids.stream().collect(Collectors.toMap(CountryBid::getElementId, CountryBid::getCountryName));

        ForecastFilterResult packageForecastResults = new ForecastFilterResult();
        
        // make lana call to get uniques, impressions and country info
        parseDataForPackages(packageList, packageForecastResults, aceCountryBidsMap);

        return esDao.storePackageForecastData(packageForecastResults.getPackageForecastMap());

    }
    
    /**
     * 
     * @param packageList
     * @param filterResult
     */
    private void parseDataForPackages(List<Group> packageList, ForecastFilterResult filterResult, Map<Long, String> aceCountryBidsMap) {
        Map<Long, List<Deal>> dealsByAdserviceId;
        
        for (Group packageType : packageList) {
            dealsByAdserviceId = AdserviceUtils.buildDealsListByAdserviceId(packageType.getDeals());
            
            if(dealsByAdserviceId != null){
                for(Map.Entry<Long, List<Deal>> entry: dealsByAdserviceId.entrySet()){
                    Long adserviceId = entry.getKey();
                    GroupForecastBids packageForecastBidsByAdservicId = new GroupForecastBids();
                    packageForecastBidsByAdservicId.setGroupName(packageType.getGroupName().toUpperCase());
                    NCRResult packageForecasts = lanaDao.getAggregatedLanaGroupBidsAndUniques(entry.getValue());
                    NCRResult packageBidByCountry = lanaDao.getGroupCountryBid(entry.getValue());
                    LinkedList<CountryBid> packageCountryBids = filterResult.extractCountryBidsForGroup(packageBidByCountry, aceCountryBidsMap);
                    packageForecastBidsByAdservicId.setCountry(packageCountryBids);
                    
                    long packageBids = 0L;
                    long packageUniques = 0L;
                    if (packageForecasts.getResults() != null && !packageForecasts.getResults().isEmpty()) {
                        for (LanaNetworkResult result : packageForecasts.getResults()) {
                            if (result.getPath().contains("UserWeek")) {
                                packageUniques = result.getAdjustedResult();
                            } else {
                                packageBids += result.getAdjustedResult();
                            }
                        }
                        packageBids = packageBids / 7;
                        packageForecastBidsByAdservicId.setImpressions(packageBids);
                        packageForecastBidsByAdservicId.setUniques(packageUniques);
    
                    } else {
                        packageForecastBidsByAdservicId.setImpressions(0L);
                        packageForecastBidsByAdservicId.setUniques(0L);
                    }
                    
                    filterResult.addPackageForecastData(adserviceId, packageForecastBidsByAdservicId);
                }
            }
        }
    }

    
    private List<Group> getPackagesForLana() {

        List<Group> lanaPackages = new LinkedList<Group>();

        List<Group> acePackages = aceDao.readAllPackages();
        if (acePackages == null || acePackages.isEmpty()) {
            return Collections.emptyList();
        }
        List<Deal> aceDeals = new LinkedList<Deal>();

        acePackages.forEach(packageType -> {
            aceDeals.addAll(packageType.getDeals());
        });

        List<Deal> esDeals = esDao.filterDealsByES(aceDeals);
        if (esDeals == null || esDeals.isEmpty()) {
            return Collections.emptyList();
        }

        for (Group packageType : acePackages) {
            List<Deal> validDeals = packageType.getDeals().stream()
                    .filter(esDeals::contains)
                    .collect(Collectors.toList());
            if (validDeals != null && !validDeals.isEmpty()) {
                packageType.setDeals(validDeals);
                lanaPackages.add(packageType);
            }
        }
        return lanaPackages;
    }


}
