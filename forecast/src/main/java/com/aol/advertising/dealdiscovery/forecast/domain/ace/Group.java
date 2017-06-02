
package com.aol.advertising.dealdiscovery.forecast.domain.ace;

import java.util.List;

/**
 * Created by mcordones13 on 01/05/17.
 */
public class Group {
    private String groupName;
    private List<Deal> deals;
    
    public String getGroupName() {
        return groupName;
    }
    
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
 
    public List<Deal> getDeals() {
        return deals;
    }

    public void setDeals(List<Deal> deals) {
        this.deals = deals;
    }

    @Override
    public String toString() {
        return "Group {" +
                "groupName='" + groupName + '\'' +
                ", deals=" + deals +
                '}';
    }

}
