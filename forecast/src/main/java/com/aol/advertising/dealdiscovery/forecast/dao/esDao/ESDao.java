
package com.aol.advertising.dealdiscovery.forecast.dao.esDao;

import com.aol.advertising.dealdiscovery.commons.es.ESDomainManager;
import com.aol.advertising.dealdiscovery.forecast.application.Config;
import com.aol.advertising.dealdiscovery.forecast.domain.ace.Deal;
import com.aol.advertising.dealdiscovery.forecast.domain.es.DealForecastBids;
import com.aol.advertising.dealdiscovery.forecast.domain.es.GroupForecastBids;
import com.aol.advertising.dealdiscovery.forecast.util.ESQueryUtil;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import io.searchbox.core.SearchResult;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by mcordones13 on 7/24/16.
 */
@Component
public class ESDao implements InitializingBean {

    @Autowired
    private Config config;

    private ESDomainManager domainManager;

    @Override
    public void afterPropertiesSet() throws Exception {
        domainManager = new ESDomainManager(config.getESDomainUrl(),
                config.getCatalogIndex(), config.getCatalogType());
    }


    public JestResult storeDealForecastData(Collection<DealForecastBids> dealsForecastData) {
        List<Index> dealsForecastIndexes = new LinkedList<Index>();
        for(DealForecastBids d : dealsForecastData){
            String id = d.getProviderId() + "_" + d.getDealId();
            dealsForecastIndexes.add(new Index.Builder(d).id(id).build());
        }

        return domainManager.refreshOperation(config.getDealIndex(),
                config.getDealType(),
                config.getDealAlias(),
                dealsForecastIndexes);
    }

    public JestResult storePublisherForecastData(Map<String, LinkedList<GroupForecastBids>> publisherForecasts) {
        List<Index> publisherForecastIndexes = new LinkedList<Index>();

        publisherForecasts.forEach((k, v) -> {
            v.forEach(item ->{
                publisherForecastIndexes.add(new Index.Builder(item).type(k).id(item.getGroupName()).build());
            });

        });

        return domainManager.refreshOperation(config.getPubIndex(),
                null,
                config.getPubAlias(),
                publisherForecastIndexes);
    }
    
    public JestResult storePackageForecastData(Map<String, LinkedList<GroupForecastBids>> packageForecasts) {
        List<Index> packageForecastIndexes = new LinkedList<Index>();

        packageForecasts.forEach((k, v) -> {
            v.forEach(item ->{
                packageForecastIndexes.add(new Index.Builder(item).type(k).id(item.getGroupName()).build());
            });

        });

        return domainManager.refreshOperation(config.getPkgIndex(),
                null,
                config.getPkgAlias(),
                packageForecastIndexes);
    }

    public List<Deal> filterDealsByES(List<Deal> aceDeals) {
        String esQuery = ESQueryUtil.generateGetDealsQuery(aceDeals);

        SearchResult searchResult = domainManager.search(config.getDealIndex(),
                config.getDealType(),
                esQuery);

        List<Deal> esDeals = ESQueryUtil.parseJsonArrayToUniqueDealList(ESQueryUtil.parseSearchResultToJsonArray(searchResult));
        return esDeals;
    }
}
