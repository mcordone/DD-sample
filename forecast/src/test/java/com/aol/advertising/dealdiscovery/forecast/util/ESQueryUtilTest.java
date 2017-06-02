
package com.aol.advertising.dealdiscovery.forecast.util;

import com.aol.advertising.dealdiscovery.forecast.domain.ace.Deal;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mcordones13 on 7/14/16.
 */
public class ESQueryUtilTest {

    @Test
    public void testGenerateGetDealsQuery() {
        Deal deal1 = new Deal(1002L, "1197");
        Deal deal2 = new Deal(1002L, "97606");
        List<Deal> dealList = new ArrayList<Deal>();
        dealList.add(deal1);
        dealList.add(deal2);
        String query = ESQueryUtil.generateGetDealsQuery(dealList);
        String expectedQuery =
                "{\n" +
                        "   \"size\":1000,\n" +
                        "   \"fields\" : [\"dealId\", \"providerId\"],\n" +
                        "   \"query\":{\n" +
                        "      \"ids\":{\n" +
                        "         \"values\":[\"1002_1197\",\"1002_97606\"]\n" +
                        "      }\n" +
                        "   }\n" +
                        "}";
        Assert.assertEquals(expectedQuery, query);
    }
}
