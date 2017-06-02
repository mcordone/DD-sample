
package com.aol.advertising.dealdiscovery.forecast.service;

import com.aol.advertising.dealdiscovery.forecast.dao.aceDao.AceDao;
import com.aol.advertising.dealdiscovery.forecast.dao.esDao.ESDao;
import com.aol.advertising.dealdiscovery.forecast.dao.lanaDao.LanaDao;
import com.aol.advertising.dealdiscovery.forecast.domain.es.CountryBid;
import com.aol.advertising.dealdiscovery.forecast.domain.es.DealForecastBids;
import com.aol.advertising.dealdiscovery.forecast.domain.es.ForecastFilterResult;
import com.aol.advertising.dealdiscovery.forecast.domain.lana.DealLanaResponse;
import com.aol.advertising.dealdiscovery.forecast.domain.lana.LanaNetworkResult;
import com.aol.advertising.dealdiscovery.forecast.domain.lana.NCRResult;
import com.aol.advertising.dealdiscovery.forecast.exception.ExternalServiceException;
import io.searchbox.client.JestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by mcordones13 on 11/27/16.
 */
@Component
public class DealsService {

    @Autowired
    private LanaDao lanaDao;

    @Autowired
    private ESDao esDao;

    @Autowired
    private AceDao aceDao;

    private static Logger LOG = LoggerFactory.getLogger(DealsService.class);

    /**
     *
     * @return
     */
    public JestResult generateLanaDealData() {

        ForecastFilterResult dealsForecastResults = new ForecastFilterResult();

        try{
            // retrieve Lana impressions and uniques
            NCRResult lanaDealImpressionsAndUniques = lanaDao.getAggregatedLanaDealBidsAndUniqueUsers();
            parseBidsAndUniquesDataForDeals(lanaDealImpressionsAndUniques, dealsForecastResults);
            LOG.info("****** Retrieved " + dealsForecastResults.getDealsForecastMap().size() + " deals from Lana");

            //process country forecast data
            //TODO: commented this code out until we fix connection between lambda and lana-facet
            //processForecastCountryData(dealsForecastResults);

        } catch (ExternalServiceException e){
            LOG.error(e.getMessage(), e);
            throw e;
        }

        return esDao.storeDealForecastData(dealsForecastResults.getDealsForecastMap().values());
    }

    /**
     *
     * @param lanaResult
     * @param dealsForecastResults
     */
    private void parseBidsAndUniquesDataForDeals(NCRResult lanaResult, ForecastFilterResult dealsForecastResults){
        if(lanaResult != null){
            Collection<LanaNetworkResult> lanaResults = lanaResult.getResults();
            DealLanaResponse dlr = null;
            for (LanaNetworkResult lnr : lanaResults) {
                dlr = new DealLanaResponse(lnr);
                dealsForecastResults.addDealForecastData(dlr);
            }
        }
    }

    /**
     *
     * @param dealsForecastResults
     */
    private void processForecastCountryData(ForecastFilterResult dealsForecastResults){
        try{
            // retrieve Ace country data to populate country name
            List<CountryBid> aceCountryBids = aceDao.readAllCountries();
            Map<Long, String> aceCountryBidsMap =
                    aceCountryBids.stream().collect(Collectors.toMap(CountryBid::getElementId, CountryBid::getCountryName));
            LOG.info("****** Retrieved " + aceCountryBidsMap.size() + " countries from ACE");

            // retrieve Lana country bids
            NCRResult lanaDealCountryBid = lanaDao.getDealCountryBid();
            Map<String, LinkedList<CountryBid>> dealCountryBidMap = dealsForecastResults.processCountryBidsForDeal(lanaDealCountryBid);
            LOG.info("****** Retrieved " + dealCountryBidMap.size() + " deals country bid data from Lana");

            dealCountryBidMap.forEach((k, v) -> {
                // adding country name
                v.forEach(p -> p.setCountryName(aceCountryBidsMap.get(p.getElementId())));

                // adding to dealForecastMap
                //Forecast forecast;
                DealForecastBids dealForecastBids = dealsForecastResults.getDealsForecastMap().get(k);
                if (dealForecastBids == null) {
                    dealForecastBids = new DealForecastBids();
                }
                dealForecastBids.setCountry(dealCountryBidMap.get(k));
                dealsForecastResults.getDealsForecastMap().put(k, dealForecastBids);
            });

        } catch (ExternalServiceException e){
            LOG.error(e.getMessage(), e);
            throw e;
        }
    }
}
