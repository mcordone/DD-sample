
package com.aol.advertising.dealdiscovery.common.es;

import com.aol.advertising.dealdiscovery.commons.es.ESDomainManager;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Created by mcordones13 on 10/1/16.
 */
@FixMethodOrder(MethodSorters.JVM)
public class ESDomainManagerTest {

    private static ESDomainManager esDomainManager;

    private static final Logger LOG = LoggerFactory.getLogger(ESDomainManagerTest.class);
    private static final String INDEX_NAME_FIRST = "test_index_first";
    private static final String INDEX_NAME_SECOND = "test_index_second";
    private static final String ALIAS_NAME = "deals"; //test_alias
    private static final String ES_ACKNOWLEDGED = "{\"acknowledged\":true}";
    private static final String DEALS_FORECAST_INDEX_NAME = "private_deals";
    private static final String DEALS_FORECAST_TYPE_NAME = "deals";
    private static final String CATALOG_INDEX = "catalog";
    private static final String CATALOG_TYPE = "internal";

    @BeforeClass
    public static void setUp() throws Exception {
        String ES_DOMAIN_URL = "http://search-dev-es-deal-diymycpftzjantkonlhgyayc6i.us-east-1.es.amazonaws.com";
        esDomainManager = new ESDomainManager(ES_DOMAIN_URL);
    }

    @Test
    public void testCreateIndex() throws Exception {
        String settings = "{\"settings\" : {\n" +
                "        \"number_of_shards\" : 2,\n" +
                "        \"number_of_replicas\" : 1\n" +
                "    }}\n";

        JestResult result = esDomainManager.createIndex(INDEX_NAME_FIRST, settings);
        assertNotNull(result);
        assertEquals(200, result.getResponseCode());
        assertEquals(ES_ACKNOWLEDGED, result.getJsonString());
    }

    @Test
    public void testRemoveIndex() throws Exception {
        JestResult result = esDomainManager.removeIndex(INDEX_NAME_FIRST);
        assertNotNull(result);
        assertEquals(200, result.getResponseCode());
        assertEquals(ES_ACKNOWLEDGED, result.getJsonString());
    }

    @Test
    public void testCreateAlias() throws Exception {
        //Create index
        JestResult result = esDomainManager.createIndex(INDEX_NAME_FIRST, null);

        assertNotNull(result);
        assertEquals(200, result.getResponseCode());
        assertEquals(ES_ACKNOWLEDGED, result.getJsonString());

        //create alias
        result = esDomainManager.createAlias(INDEX_NAME_FIRST,ALIAS_NAME);
        assertNotNull(result);
        assertEquals(200, result.getResponseCode());
        assertEquals(ES_ACKNOWLEDGED, result.getJsonString());
    }

    @Test
    public void testRemoveAlias() throws Exception {
        //remove alias
        JestResult result = esDomainManager.removeAlias(INDEX_NAME_FIRST,ALIAS_NAME);

        assertNotNull(result);
        assertEquals(200, result.getResponseCode());
        assertEquals(ES_ACKNOWLEDGED, result.getJsonString());

        //remove index
        result = esDomainManager.removeIndex(INDEX_NAME_FIRST);

        assertNotNull(result);
        assertEquals(200, result.getResponseCode());
        assertEquals(ES_ACKNOWLEDGED, result.getJsonString());
    }

    @Test
    public void testSwapAlias() throws Exception {
        JestResult result = null;

        //Create first index
        result = esDomainManager.createIndex(INDEX_NAME_FIRST, null);
        assertNotNull(result);
        assertEquals(200, result.getResponseCode());
        assertEquals(ES_ACKNOWLEDGED, result.getJsonString());

        //create first alias in INDEX_NAME_FIRST
        result = esDomainManager.createAlias(INDEX_NAME_FIRST, ALIAS_NAME);
        assertNotNull(result);
        assertEquals(200, result.getResponseCode());
        assertEquals(ES_ACKNOWLEDGED, result.getJsonString());

        //Create second index
        result = esDomainManager.createIndex(INDEX_NAME_SECOND, null);
        assertNotNull(result);
        assertEquals(200, result.getResponseCode());
        assertEquals(ES_ACKNOWLEDGED, result.getJsonString());

        //add alias to INDEX_NAME_SECOND and remove alias from INDEX_NAME_FIRST
        result = esDomainManager.swapAlias(INDEX_NAME_SECOND, INDEX_NAME_FIRST, ALIAS_NAME);
        assertNotNull(result);
        assertEquals(200, result.getResponseCode());
        assertEquals(ES_ACKNOWLEDGED, result.getJsonString());

        //remove alias
        result = esDomainManager.removeAlias(INDEX_NAME_SECOND, ALIAS_NAME);
        assertNotNull(result);
        assertEquals(200, result.getResponseCode());
        assertEquals(ES_ACKNOWLEDGED, result.getJsonString());

        //remove first index
        result = esDomainManager.removeIndex(INDEX_NAME_FIRST);
        assertNotNull(result);
        assertEquals(200, result.getResponseCode());
        assertEquals(ES_ACKNOWLEDGED, result.getJsonString());

        //remove second index
        result = esDomainManager.removeIndex(INDEX_NAME_SECOND);
        assertNotNull(result);
        assertEquals(200, result.getResponseCode());
        assertEquals(ES_ACKNOWLEDGED, result.getJsonString());
    }

    @Test
    public void testRefreshOperation(){
        JestResult result = null;
        List<Index> data = new ArrayList<>();
        data.add(new Index.Builder("{\"dealId\":\"12345\", \"providerId\":\"1003\"}").build());
        data.add(new Index.Builder("{\"dealId\":\"6789\", \"providerId\":\"1004\"}").build());

       result = esDomainManager.refreshOperation(DEALS_FORECAST_INDEX_NAME,
               DEALS_FORECAST_TYPE_NAME,
               ALIAS_NAME,
               CATALOG_INDEX,
               CATALOG_TYPE,
               data);
        assertNotNull(result);
        assertEquals(true, result.isSucceeded());
        //assertEquals(201, result.getResponseCode());
        //assertEquals(ES_ACKNOWLEDGED, result.getJsonString());
    }

    @Test
    public void updateCatalog() throws InterruptedException {
        JestResult result = null;
        String indexName = "private_deals_test";
        String mappingName = "testing_mapping";
        String catalogName = "catalog";
        String catalogType = "internal";
        String refreshStartTimestamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US).format(new Date());
        Thread.sleep(1000);
        String refreshEndTimestamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US).format(new Date());

        result = esDomainManager.updateCatalog(indexName, mappingName, catalogName, catalogType, refreshStartTimestamp, refreshEndTimestamp);
        assertNotNull(result);
        assertEquals(201, result.getResponseCode());
    }

    @Test
    public void getCurrentActiveIndexNameFromCatalog(){
        String indexName = "private_deals_test";
        String catalogName = "catalog";
        String catalogType = "internal";
        Optional<String> result = esDomainManager.getCurrentActiveIndexNameFromCatalog(indexName,catalogName, catalogType);
        assertNotNull(result);
    }
}