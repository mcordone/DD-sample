
package com.aol.advertising.dealdiscovery.forecast.application;

import org.springframework.core.env.Environment;

/**
 * Created by mcordones13 on 10/12/16.
 */
public class Config {

    private Environment env;

    public Config(Environment env){
            this.env = env;
    }

    public String getLanaApiUri() {
        return env.getProperty(PropertiesConstants.LANA_API_URL);
    }

    public String getLanaFacetUri() {
        return env.getProperty(PropertiesConstants.LANA_FACET_URL);
    }

    public String getESDomainUrl() {
            return env.getProperty(PropertiesConstants.ES_DOMAIN_URL);
    }

    public String getCatalogIndex() {
            return env.getProperty(PropertiesConstants.INDEX_CATALOG);
    }

    public String getCatalogType() {
            return env.getProperty(PropertiesConstants.TYPE_CATALOG);
    }

    public String getDealIndex() {
        return env.getProperty(PropertiesConstants.INDEX_DEAL_FORECAST);
    }

    public String getDealType() {
        return env.getProperty(PropertiesConstants.TYPE_DEAL_FORECAST);
    }

    public String getPubIndex() {
        return env.getProperty(PropertiesConstants.INDEX_PUB_FORECAST);
    }

    public String getPubType() {
        return env.getProperty(PropertiesConstants.TYPE_PUB_FORECAST);
    }

    public String getPkgIndex() {
        return env.getProperty(PropertiesConstants.INDEX_PKG_FORECAST);
    }

    public String getPkgType() {
        return env.getProperty(PropertiesConstants.TYPE_PKG_FORECAST);
    }

    public String getDealAlias() {
        return env.getProperty(PropertiesConstants.ALIAS_DEAL_FORECAST);
    }

    public String getPubAlias() {
        return env.getProperty(PropertiesConstants.ALIAS_PUB_FORECAST);
    }

    public String getPkgAlias() {
        return env.getProperty(PropertiesConstants.ALIAS_PKG_FORECAST);
    }

    public String getDBUrl() {
            return env.getProperty(PropertiesConstants.ACE_DB_URL);
    }

    public String getDBUsername() {
            return env.getProperty(PropertiesConstants.ACE_DB_USERNAME);
    }

    public String getDBPwd() {
            return env.getProperty(PropertiesConstants.ACE_DB_PWD);
    }

    public Environment getEnv() {
        return env;
    }

    public void setEnv(Environment env) {
        this.env = env;
    }
}