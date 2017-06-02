
package com.aol.advertising.dealdiscovery.forecast.dao.aceDao;

import com.aol.advertising.dealdiscovery.forecast.domain.es.CountryBid;
import com.aol.advertising.dealdiscovery.forecast.domain.ace.Group;

import java.util.List;

/**
 * Created by mcordones13 on 5/31/16.
 */
public interface AceMapper {

    List<Group> readAllPublishers();

    List<CountryBid> readAllCountries();
    
    List<Group> readAllPackages();
}
