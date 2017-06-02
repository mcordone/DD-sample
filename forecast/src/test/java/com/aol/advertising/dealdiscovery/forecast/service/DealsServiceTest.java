
package com.aol.advertising.dealdiscovery.forecast.service;

import com.aol.advertising.dealdiscovery.forecast.dao.aceDao.AceDao;
import com.aol.advertising.dealdiscovery.forecast.dao.esDao.ESDao;
import com.aol.advertising.dealdiscovery.forecast.dao.lanaDao.LanaDao;
import com.aol.advertising.dealdiscovery.forecast.domain.es.DealForecastBids;
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

import java.util.Collection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by mcordones13 on 11/27/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class DealsServiceTest {

    @Mock
    private LanaDao lanaDao;

    @Mock
    private ESDao esDao;

    @Mock
    private AceDao aceDao;

    @InjectMocks
    private DealsService classUnderTest = new DealsService();

    private static final Logger LOG = LoggerFactory.getLogger(DealsServiceTest.class);

    @Test
    public void testGenerateLanaDealData() throws Exception {
        DealsService dealServiceMock = mock(DealsService.class);
        Collection<DealForecastBids> dealsForecastData = mock(Collection.class);
        JestResult expectedResult = mock(JestResult.class);
        NCRResult lanaResult = mock(NCRResult.class);

        when(lanaDao.getAggregatedLanaDealBidsAndUniqueUsers()).thenReturn(lanaResult);
        when(lanaDao.getDealCountryBid()).thenReturn(lanaResult);
        when(esDao.storeDealForecastData(dealsForecastData)).thenReturn(expectedResult);

        Mockito.doReturn(expectedResult).when(dealServiceMock).generateLanaDealData();
        JestResult jestResult = dealServiceMock.generateLanaDealData();
        assertNotNull(jestResult);
        assertTrue(expectedResult.equals(jestResult));

        classUnderTest.generateLanaDealData();

        verify(lanaDao, times(1)).getAggregatedLanaDealBidsAndUniqueUsers();
        verify(lanaDao, times(1)).getDealCountryBid();
        verify(esDao, times(1)).storeDealForecastData(anyCollection());
        verify(dealServiceMock, times(1)).generateLanaDealData();
    }
}