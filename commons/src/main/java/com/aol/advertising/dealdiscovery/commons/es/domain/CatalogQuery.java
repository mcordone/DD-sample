
package com.aol.advertising.dealdiscovery.commons.es.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mcordones13 on 7/28/16.
 */
public class CatalogQuery {
    @SerializedName("index_name")
    private String indexName;
    @SerializedName("mapping_name")
    private String mappingName;
    @SerializedName("refresh_end")
    private String refreshEnd;
    @SerializedName("refresh_start")
    private String refreshStart;

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getMappingName() {
        return mappingName;
    }

    public void setMappingName(String mappingName) {
        this.mappingName = mappingName;
    }

    public String getRefreshEnd() {
        return refreshEnd;
    }

    public void setRefreshEnd(String refreshEnd) {
        this.refreshEnd = refreshEnd;
    }

    public String getRefreshStart() {
        return refreshStart;
    }

    public void setRefreshStart(String refreshStart) {
        this.refreshStart = refreshStart;
    }
}

