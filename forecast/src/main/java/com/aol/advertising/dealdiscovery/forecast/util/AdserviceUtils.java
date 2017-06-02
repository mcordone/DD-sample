
package com.aol.advertising.dealdiscovery.forecast.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.aol.advertising.dealdiscovery.forecast.domain.ace.Deal;

/**
 * Created by meitlofri08 1/9/2017
 */
public final class AdserviceUtils {

    private AdserviceUtils() {
    }

    public static Map<Long, List<Deal>> buildDealsListByAdserviceId(List<Deal> deals) {
        Map<Long, List<Deal>> dealsByAdserviceId = new HashMap<Long, List<Deal>>();
        List<Deal> dealsList;
        if (deals == null || deals.isEmpty()) {
            return dealsByAdserviceId;
        }
        for (Deal d : deals) {
            dealsList = dealsByAdserviceId.get((Long) d.getAdserviceId());
            if (dealsList == null) {
                dealsList = new LinkedList<Deal>();
            }
            dealsList.add(d);
            dealsByAdserviceId.put((Long) d.getAdserviceId(), dealsList);
        }
        return dealsByAdserviceId;
    }
}
