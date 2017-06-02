
package com.aol.advertising.dealdiscovery.forecast.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aol.advertising.dealdiscovery.forecast.dao.aceDao.AceDao;
import com.aol.advertising.dealdiscovery.forecast.domain.ace.Deal;
import io.searchbox.client.JestResult;

import java.lang.reflect.Method;
import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aol.advertising.dealdiscovery.forecast.dao.esDao.ESDao;
import com.aol.advertising.dealdiscovery.forecast.dao.lanaDao.LanaDao;
import com.aol.advertising.dealdiscovery.forecast.domain.ace.Group;
import com.aol.advertising.dealdiscovery.forecast.domain.es.ForecastFilterResult;
import com.aol.advertising.dealdiscovery.forecast.domain.es.GroupForecastBids;
import com.aol.advertising.dealdiscovery.forecast.domain.lana.NCRResult;

/**
 * Created by meitalofri08 on 01/06/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class PackageServiceTest {
    private static final Logger LOG = LoggerFactory.getLogger(PackageServiceTest.class);
    
    @Mock
    private ESDao esDao;
    
    @Mock
    private LanaDao lanaDao;

    @Mock
    private AceDao aceDao;
    
    @InjectMocks
    private PackageService classUnderTest = new PackageService();
    
    @Test
    public void testGenerateLanaPackageData() throws Exception {
        PackageService lanaServiceMock = mock(PackageService.class);
        Map<String, LinkedList<GroupForecastBids>> packageForecasts = mock(Map.class);
        JestResult expectedResult = mock(JestResult.class);

        when(esDao.storePackageForecastData(packageForecasts)).thenReturn(expectedResult);

        Mockito.doReturn(expectedResult).when(lanaServiceMock).generateLanaPackageData();
        JestResult jestResult = lanaServiceMock.generateLanaPackageData();
        assertNotNull(jestResult);

        classUnderTest.generateLanaPackageData();

        verify(esDao, times(1)).storePackageForecastData(anyMap());
        verify(lanaServiceMock, times(1)).generateLanaPackageData();
    }
    
    @Test
    public void testParseDataForPackages() throws Exception {
        List<Group> packageList = new LinkedList<Group>();
        Group package1 = new Group();
        package1.setGroupName("Seasonal");
        Group package2 = new Group();
        package2.setGroupName("Contextual");
        Deal deal1 = new Deal(1002L, "deal1", 1L);
        Deal deal2 = new Deal(1002L, "deal2", 1L);
        Deal deal3 = new Deal(1002L, "deal3", 1L);
        Deal deal4 = new Deal(1002L, "deal4", 1L);
        List<Deal> dealList1 = Arrays.asList(deal1, deal2);
        List<Deal> dealList2 = Arrays.asList(deal3, deal4);
        package1.setDeals(dealList1);
        package2.setDeals(dealList2);
        packageList.add(package1);
        packageList.add(package2);

        ForecastFilterResult filterResult = mock(ForecastFilterResult.class);
        Map<Long, String> aceCountryMap = new HashMap<>();

        NCRResult lanaResult = mock(NCRResult.class);
        when(lanaDao.getAggregatedLanaGroupBidsAndUniques(anyList())).thenReturn(lanaResult);
        when(lanaDao.getGroupCountryBid(anyList())).thenReturn(lanaResult);

        Method method = PackageService.class.getDeclaredMethod("parseDataForPackages", List.class, ForecastFilterResult.class, Map.class);
        method.setAccessible(true);
        method.invoke(classUnderTest, packageList, filterResult, aceCountryMap);

        verify(lanaDao, times(2)).getAggregatedLanaGroupBidsAndUniques(anyList());
        verify(lanaDao, times(2)).getGroupCountryBid(anyList());
    }
}
