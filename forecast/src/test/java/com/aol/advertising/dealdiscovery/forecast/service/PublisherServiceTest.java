
package com.aol.advertising.dealdiscovery.forecast.service;


import com.aol.advertising.dealdiscovery.forecast.dao.aceDao.AceDao;
import com.aol.advertising.dealdiscovery.forecast.dao.esDao.ESDao;
import com.aol.advertising.dealdiscovery.forecast.dao.lanaDao.LanaDao;
import com.aol.advertising.dealdiscovery.forecast.domain.ace.Deal;
import com.aol.advertising.dealdiscovery.forecast.domain.ace.Group;
import com.aol.advertising.dealdiscovery.forecast.domain.es.ForecastFilterResult;
import com.aol.advertising.dealdiscovery.forecast.domain.es.GroupForecastBids;
import com.aol.advertising.dealdiscovery.forecast.domain.lana.NCRResult;
import io.searchbox.client.JestResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

/**
 * Created by mcordones13 on 5/31/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class PublisherServiceTest {
    private static final Logger LOG = LoggerFactory.getLogger(PublisherServiceTest.class);

    @Mock
    private LanaDao lanaDao;

    @Mock
    private AceDao aceDao;

    @Mock
    private ESDao esDao;

    @InjectMocks
    private PublisherService classUnderTest = new PublisherService();

    @Test
    public void testGenerateLanaPublisherData() throws Exception {
        PublisherService lanaServiceMock = mock(PublisherService.class);
        Map<String, LinkedList<GroupForecastBids>> publisherForecasts = mock(Map.class);
        JestResult expectedResult = mock(JestResult.class);

        when(esDao.storePublisherForecastData(publisherForecasts)).thenReturn(expectedResult);

        Mockito.doReturn(expectedResult).when(lanaServiceMock).generateLanaPublisherData();
        JestResult jestResult = lanaServiceMock.generateLanaPublisherData();
        assertNotNull(jestResult);

        classUnderTest.generateLanaPublisherData();

        verify(esDao, times(1)).storePublisherForecastData(anyMap());
        verify(lanaServiceMock, times(1)).generateLanaPublisherData();
    }

    @Test
    public void testParseDataForPublishers() throws Exception {
        List<Group> publisherList = new LinkedList<>();
        Group pub1 = new Group();
        pub1.setGroupName("pub1");
        Group pub2 = new Group();
        pub2.setGroupName("pub2");
        Deal deal1 = new Deal(1002L, "deal1", 1L);
        Deal deal2 = new Deal(1002L, "deal2", 1L);
        Deal deal3 = new Deal(1002L, "deal3", 1L);
        Deal deal4 = new Deal(1002L, "deal4", 1L);
        List<Deal> dealList1 = Arrays.asList(deal1, deal2);
        List<Deal> dealList2 = Arrays.asList(deal3, deal4);
        pub1.setDeals(dealList1);
        pub2.setDeals(dealList2);
        publisherList.add(pub1);
        publisherList.add(pub2);

        ForecastFilterResult filterResult = mock(ForecastFilterResult.class);
        Map<Long, String> aceCountryMap = new HashMap<>();

        NCRResult lanaResult = mock(NCRResult.class);
        when(lanaDao.getAggregatedLanaGroupBidsAndUniques(anyList())).thenReturn(lanaResult);
        when(lanaDao.getGroupCountryBid(anyList())).thenReturn(lanaResult);

        Method method = PublisherService.class.getDeclaredMethod("parseDataForPublishers", List.class, ForecastFilterResult.class, Map.class);
        method.setAccessible(true);
        method.invoke(classUnderTest, publisherList, filterResult, aceCountryMap);

        verify(lanaDao, times(2)).getAggregatedLanaGroupBidsAndUniques(anyList());
        verify(lanaDao, times(2)).getGroupCountryBid(anyList());

    }
}