
package com.aol.advertising.dealdiscovery.forecast.util;

import java.util.HashMap;
import java.util.Map;


/**
 * enum to represent the mapping from UI country code into adservice id
 * @author mcordones13
 *
 */
public enum CountryAdservice {
    /** US */
    US("US", 1L),
    /** CA */
    CA("CA", 1L),
    /** GB */
    GB("GB", 17L),
    /** JP */
    JP("JP", 311L);

    private final String country;
    private final Long adserviceId;

    private static final Map<Long, CountryAdservice> ADSERVICE_LOOKUP;
    private static final Map<String, CountryAdservice> COUNTRY_LOOKUP;

    static {
        ADSERVICE_LOOKUP = new HashMap<>();
        COUNTRY_LOOKUP = new HashMap<>();
        for (CountryAdservice country : CountryAdservice.values()) {
            // Not put in CA for ADSERVICE_LOOKUP, since adservice 1 should be US
            if (!country.getCountry().equals("CA")) {
                ADSERVICE_LOOKUP.put(country.getAdserviceId(), country);
            }
            COUNTRY_LOOKUP.put(country.getCountry(), country);
        }
    }

    private CountryAdservice(final String countryCode, final long adSvcId) {
        country = countryCode;
        adserviceId = adSvcId;
    }

    /**
     * Get the country code that is used in the UI
     * @return country code
     */
    public String getCountry() {
        return country;
    }

    /**
     * Get the adservice id that matches the country
     * @return adservice id
     */
    public Long getAdserviceId() {
        return adserviceId;
    }

    /**
     * Do a lookup based on the adservice id
     * @param adSvcId - The adservice id.
     * @return
     */
    public static CountryAdservice findForAdserviceId(Long adSvcId) {
        if (ADSERVICE_LOOKUP.containsKey(adSvcId)) {
            return ADSERVICE_LOOKUP.get(adSvcId);
        }
        return null;
    }

    /**
     * Do a lookup based on the country
     * @param countryCode - The country to search for
     * @return
     */
    public static CountryAdservice findForCountry(final String countryCode) {
        if (COUNTRY_LOOKUP.containsKey(countryCode)) {
            return COUNTRY_LOOKUP.get(countryCode);
        }
        return null;
    }
}
