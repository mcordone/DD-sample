
package com.aol.advertising.dealdiscovery.forecast.service;

import com.aol.advertising.dealdiscovery.forecast.application.AppConfig;
import com.aol.advertising.dealdiscovery.forecast.exception.ExternalServiceException;

import io.searchbox.client.JestResult;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by mcordones13 on 9/27/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class RunningTest {

    @Autowired
    private PublisherService publisherService;

    @Autowired
    private DealsService dealsService;
    
    @Autowired
    private PackageService packageService;

    private JestResult jestResult;

    private static final Logger LOG = LoggerFactory.getLogger(RunningTest.class);

    @BeforeClass
    public static void setUp(){
        System.setProperty("env", "dev");
    }

    @Test
    public void dummyTest() {
        Assert.assertEquals(true, true);
    }


//    @Test
    public void testGetLanaImpForDiscoveredDeals() throws Exception {
        try {
            jestResult = dealsService.generateLanaDealData();
            Assert.assertEquals(true, jestResult.isSucceeded());
        }catch (ExternalServiceException e){
            LOG.error("****** LANA EXCEPTION ERROR: " + e.getFaultBean().getDetail(), e);
            Assert.fail();
        }
    }

//    @Test
    public void testGenerateLanaPublisherData() throws Exception {
        try {
            jestResult = publisherService.generateLanaPublisherData();
            Assert.assertEquals(true, jestResult.isSucceeded());
        } catch (ExternalServiceException e){
            LOG.error("****** LANA EXCEPTION ERROR: " + e.getFaultBean().getDetail(), e);
        }
    }
    
//  @Test
    public void testGenerateLanaPackageData() throws Exception {
        try {
            jestResult = packageService.generateLanaPackageData();
            Assert.assertEquals(true, jestResult.isSucceeded());
        } catch (ExternalServiceException e){
            LOG.error("****** LANA EXCEPTION ERROR: " + e.getFaultBean().getDetail(), e);
        }
    }

}