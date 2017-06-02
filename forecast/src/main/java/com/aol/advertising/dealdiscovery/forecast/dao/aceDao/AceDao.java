
package com.aol.advertising.dealdiscovery.forecast.dao.aceDao;
import com.aol.advertising.dealdiscovery.forecast.domain.es.CountryBid;
import com.aol.advertising.dealdiscovery.forecast.domain.ace.Group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by mcordones13 9/20/2016
 */
@Component
public class AceDao {

    @Autowired
    private AceMapper aceMapper;

    public List<Group> readAllPublishers() {
        return aceMapper.readAllPublishers();
    }

    public List<CountryBid> readAllCountries() {
        return aceMapper.readAllCountries();
    }
    
    public List<Group> readAllPackages(){
        return aceMapper.readAllPackages();
    }
}
