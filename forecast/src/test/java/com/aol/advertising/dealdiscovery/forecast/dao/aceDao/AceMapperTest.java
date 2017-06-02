
package com.aol.advertising.dealdiscovery.forecast.dao.aceDao;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aol.advertising.dealdiscovery.forecast.application.AppConfig;

/**
 * @author meitalofri08
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class AceMapperTest {
    @Autowired
    private AceMapper aceMapper;

    @BeforeClass
    public static void setUp() {
        System.setProperty("env", "dev");
    }

    @Test
    public void dummyTest() {
        Assert.assertEquals(true, true);
    }
    


}
