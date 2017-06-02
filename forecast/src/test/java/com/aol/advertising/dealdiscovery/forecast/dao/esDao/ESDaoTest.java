
package com.aol.advertising.dealdiscovery.forecast.dao.esDao;

import com.aol.advertising.dealdiscovery.commons.es.ESDomainManager;
import com.aol.advertising.dealdiscovery.forecast.application.AppConfig;
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
 * Created by mcordones13 on 1/9/17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class ESDaoTest {

    @Autowired
    private ESDao esDao;

    private static ESDomainManager domainManager;

    private static Logger logger = LoggerFactory.getLogger(ESDaoTest.class);

    @BeforeClass
    public static void setUp(){
        System.setProperty("env", "dev");


        domainManager = new ESDomainManager("http://search-dev-es-deal-diymycpftzjantkonlhgyayc6i.us-east-1.es.amazonaws.com"
                ,"catalog"
                ,"internal");
    }

    @Test
    public void dummyTest() {
        Assert.assertEquals(true, true);
    }

}