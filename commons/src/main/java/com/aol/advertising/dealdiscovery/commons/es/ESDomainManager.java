
package com.aol.advertising.dealdiscovery.commons.es;

import com.aol.advertising.dealdiscovery.commons.es.domain.CatalogQuery;
import com.aol.advertising.dealdiscovery.commons.es.exception.ElasticsearchException;
import com.google.gson.Gson;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.SearchResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.Delete;
import io.searchbox.core.Get;
import io.searchbox.core.Update;
import io.searchbox.core.Bulk;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.aliases.AddAliasMapping;
import io.searchbox.indices.aliases.ModifyAliases;
import io.searchbox.indices.aliases.RemoveAliasMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by mcordones13 on 7/24/16.
 */
public class ESDomainManager {

    private static final Logger LOG = LoggerFactory.getLogger(ESDomainManager.class);
    private JestClient jestClient;
    private JestResult result;
    private String catalogIndex;
    private String catalogType;


    /**
     * @param url
     */
    public ESDomainManager(String url) {
        createJestClient(url);
    }

    /**
     *
     * @param url
     * @param catalogIndex
     * @param catalogType
     */
    public ESDomainManager(String url, String catalogIndex, String catalogType){
        this(url);
        this.catalogIndex = catalogIndex;
        this.catalogType = catalogType;
    }

    /**
     * create JestClient
     * @param url
     */
    private void createJestClient(String url){
        //create JestClient instance
        HttpClientConfig clientConfig = new HttpClientConfig
                .Builder(url)
                .multiThreaded(true)
                .build();
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(clientConfig);
        jestClient = factory.getObject();
    }

    //--- Indices management public api ---

    /**
     *
     * @param indexName
     * @param settings
     * @return JestResult
     */
    public JestResult createIndex(String indexName, String settings){
        result = null;

        //TODO externalize setting, perhaps in an external JSON file to be loaded here instead
        if(settings == null || settings.length() <= 0)
            settings = "{\"settings\" : {\"index\" : {\"number_of_shards\" : 5, \"number_of_replicas\" : 1}}}";

        //create index
        CreateIndex createIndex = new CreateIndex.Builder(indexName).settings(settings).build();

        try {
            result = jestClient.execute(createIndex);
        } catch (IOException e) {
            LOG.error("*** creating index error: " + e.getMessage(), e);
        }

        return result;
    }

    /**
     *
     * @param indexName
     * @return JestResult
     */
    public JestResult removeIndex(String indexName){
        result = null;

        //check if index exist
        if(!isIndexExist(indexName)) {
            JestResult res = new JestResult(new Gson());
            res.setSucceeded(true);
            res.setResponseCode(200);
            return res;
        }

        DeleteIndex deleteIndex = new DeleteIndex.Builder(indexName).build();

        try {
            result = jestClient.execute(deleteIndex);
        } catch (IOException e) {
            LOG.error("*** removing index error: " + e.getMessage(), e);
        }

        return result;
    }

    public boolean isIndexExist(String indexName){
        IndicesExists indicesExists = new IndicesExists.Builder(indexName).build();
        boolean indexExists = false;

        try {
            indexExists = jestClient.execute(indicesExists).isSucceeded();
        } catch (IOException e) {
            LOG.error("*** IndexExist check error: " + e.getMessage(), e);
        }

        return indexExists;
    }

    //--- Mappings management public api ---

    /**
     *
     * @param mappingName
     * @return JestResult
     */
    public JestResult createMapping(String mappingName){
        //// todo: 11/27/16 to be implemented yet
        result = null;

        return result;
    }

    /**
     *
     * @param mappingName
     * @return JestResult
     */
    public JestResult removeMapping(String mappingName){
        result = null;

        return result;
    }

    //--- Aliases management public api ---

    /**
     *
     * @param indexToOperate
     * @param aliasName
     * @return JestResult
     */
    public JestResult createAlias(String indexToOperate, String aliasName){
        result = null;

        AddAliasMapping aliasMapping = new AddAliasMapping.Builder(indexToOperate, aliasName).build();
        ModifyAliases modifyAliases = new ModifyAliases.Builder(aliasMapping).build();

        try {
            result = jestClient.execute(modifyAliases);
        } catch (IOException e) {
            LOG.error("*** creating alias error: " + e.getMessage(), e);
        }

        return result;
    }

    /**
     *
     * @param indexToOperate
     * @param aliasName
     * @return JestResult
     */
    public JestResult removeAlias(String indexToOperate, String aliasName){
        result = null;
        RemoveAliasMapping removeAlias = new RemoveAliasMapping.Builder(indexToOperate, aliasName).build();
        ModifyAliases modifyAliases = new ModifyAliases.Builder(removeAlias).build();

        try {
            result = jestClient.execute(modifyAliases);
        } catch (IOException e) {
            LOG.error("*** removing alias error: " + e.getMessage(), e);
        }

        return result;
    }

    /**
     *
     * @param indexToAddAlias
     * @param aliasNameToAdd
     * @param indexToRemoveAlias
     * @return JestResult
     */
    public JestResult swapAlias(String indexToAddAlias, String indexToRemoveAlias, String aliasNameToAdd ){
        result = null;

        //create AddAliasMapping
        AddAliasMapping addAlias = new AddAliasMapping.Builder(indexToAddAlias, aliasNameToAdd).build();

        //create RemoveAliasMapping
        RemoveAliasMapping removeAlias = new RemoveAliasMapping.Builder(indexToRemoveAlias, aliasNameToAdd).build();

        //create ModifyAliases
        ModifyAliases modifyAliases = new ModifyAliases.Builder(removeAlias).addAlias(addAlias).build();

        try {
            result = jestClient.execute(modifyAliases);
        } catch (IOException e) {
            LOG.error("*** Alias swapping error: " + e.getMessage(), e);
        }

        return result;
    }

    //--- Catalog management public api ---

    /**
     * @param indexName
     * @param mappingName
     * @param refreshStart
     * @param refreshEnd
     * @return JestResult
     */
    public JestResult updateCatalog(String indexName, String mappingName, String catalogIndexName, String catalogType, String refreshStart, String refreshEnd){
        result = null;

        //TODO externalize if possible
        String json = "{" +
                    "\"index_name\": \"%s\"," +
                    "\"mapping_name\": \"%s\"," +
                    "\"refresh_end\": \"%s\"," +
                    "\"refresh_start\": \"%s\"" +
                "}";

        String jsonPayload = String.format(json, indexName, mappingName, refreshStart, refreshEnd);
        Index catalogIndex = new Index.Builder(jsonPayload).index(catalogIndexName).type(catalogType).build();

        try {
            result = jestClient.execute(catalogIndex);
        } catch (IOException e) {
            LOG.error("*** Updating Catalog error: " + e.getMessage(), e);
        }

        return result;
    }

    /**
     * @return String name of current active index name
     */
    public Optional<String> getCurrentActiveIndexNameFromCatalog(String indexName, String catalogIndex, String catalogType){
        //TODO move this query to proper place, perhaps externalize it
        //query return list of all entries in catalog sorted by 'timestamp' in ascending order. Latest entry is always the top item in the list
        String query = "{\"query\":{\"prefix\":{\"index_name\": \"%s\"}},\"sort\":[{\"refresh_end\":{\"order\":\"desc\"}}],\"size\":1}";

        query = String.format(query, indexName);

        //query catalog
        SearchResult searchResult = search(catalogIndex, catalogType, query);

        //get result
        List<SearchResult.Hit<CatalogQuery, Void>> hits = searchResult.getHits(CatalogQuery.class);
        Optional<String> optional = Optional.empty();
        if(hits.isEmpty()) {
            return optional;
        }
        return optional.of(hits.get(0).source.getIndexName());
    }

    /**
     * overloaded version of refreshOperation method
     * @param indexName
     * @param typeName
     * @param aliasName
     * @param data
     * @return
     */
    public JestResult refreshOperation(String indexName,
                                       String typeName,
                                       String aliasName,
                                       List<Index> data){
        if(catalogIndex.length() <= 0 || catalogType.length() <= 0)
            throw new RuntimeException("catalogIndex and / or catalogType fields must not be empty");

        return performRefreshOperation(indexName, typeName, aliasName, catalogIndex, catalogType, data);
    }

    /**
     * overloaded version of refreshOperation method
     * @param indexName
     * @param typeName
     * @param aliasName
     * @param catalogIndex
     * @param catalogType
     * @param data
     * @return
     */
    public JestResult refreshOperation(String indexName,
                                       String typeName,
                                       String aliasName,
                                       String catalogIndex,
                                       String catalogType,
                                       List<Index> data){

        return performRefreshOperation(indexName,
                typeName,
                aliasName,
                catalogIndex,
                catalogType,
                data);

    }

    /**
     *
     * @param indexName
     * @param mappingName
     * @param aliasName
     * @param catalogIndex
     * @param catalogType
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T refreshOperation(String indexName,
                                  String mappingName,
                                  String aliasName,
                                  String catalogIndex,
                                  String catalogType,
                                  List<Index> data,
                                  Class<T> clazz) {
        T result = null;

        //perform operation
        JestResult esResult = performRefreshOperation(indexName, mappingName, aliasName, catalogIndex, catalogType, data);

        //cast operation result to generic type
        result = clazz.cast(esResult); //This can throw ClassCastException, let it bubble

        return result;
    }

    /**
     *
     * @param indexName
     * @param typeName
     * @param aliasName
     * @param catalogIndex
     * @param catalogType
     * @param indexData
     * @return
     */
    private JestResult performRefreshOperation(String indexName,
                                               String typeName,
                                               String aliasName,
                                               String catalogIndex,
                                               String catalogType,
                                               List<Index> indexData){
        /*
        1 - create new index
        2- index/insert data in new index
        3- create alias in index and remove alias from existing (old) index
        4- update catalog index_name field with newly created index name
        5- remove current active index
        6- log operations in logfile index
         */
        JestResult result = null;
        List<Index> data = indexData;
        String refreshStartTimestamp;
        String refreshEndTimestamp;
        String newIndexName;
        String activeIndex = getCurrentActiveIndexNameFromCatalog(indexName, catalogIndex, catalogType).orElse("NO_INDEX");
        String timestampFormat = "yyyy_MM_dd_HH_mm_ss_SSS";

        //add timestamp to new index name so it's unique
        newIndexName = indexName + "_" + new SimpleDateFormat(timestampFormat).format(new Date());

        //create new index
        result = createIndex(newIndexName, null);
        checkRefreshOperationResult(result);

        //data index operation start timestamp for catalog index update
        refreshStartTimestamp = new SimpleDateFormat(timestampFormat).format(new Date());

        //Index updated data into newly created index
        result = bulkInsert(newIndexName, typeName, data);
        checkRefreshOperationResult(result);

        //data index operation end timestamp for catalog index update
        refreshEndTimestamp = new SimpleDateFormat(timestampFormat).format(new Date());

        //create alias in newly created index and remove alias from activeIndex if exist
        if(isIndexExist(activeIndex)) {
            result = swapAlias(newIndexName, activeIndex, aliasName);
            checkRefreshOperationResult(result);
        }
        else {
            result = createAlias(newIndexName, aliasName);
            checkRefreshOperationResult(result);
        }

        //Update catalog
        result = updateCatalog(newIndexName, null, catalogIndex, catalogType, refreshStartTimestamp, refreshEndTimestamp);
        checkRefreshOperationResult(result);

        //Remove current active index
        removeIndex(activeIndex);

        return result;
    }

    /**
     * Throw exception if any of the steps in performRefreshOperation method fails (except removeIndex for now)
     * @param result
     */
    private void checkRefreshOperationResult(JestResult result){
        if(!result.isSucceeded()){
            //throws exception
            throw new ElasticsearchException(result.getJsonObject().get("error").getAsString());
        }
    }

    /**
     * Insert single Index (data) object
     * @param data
     * @return JestResult
     */
    public JestResult insert(Index data){
        JestResult result = null;
        try{
            result = jestClient.execute(data);

        } catch (IOException e) {
            LOG.error("*** Insert index error: " + e.getMessage(), e);
        }

        return result;
    }

    /**
     *
     * @param ESIndex
     * @param ESType
     * @param data
     */
    public JestResult bulkInsert(String ESIndex, String ESType, List<Index> data){
        JestResult result = null;

        try {
            Bulk bulk = new Bulk.Builder()
                    .defaultIndex(ESIndex)
                    .defaultType(ESType)
                    .addAction(data)
                    .build();

            result = jestClient.execute(bulk);
        } catch (IOException e) {
            LOG.error("*** Bulk insert error: " + e.getMessage(), e);
            throw new ElasticsearchException(e.getMessage(), e);
        }

        return result;
    }

    /**
     * Insert multiples Index (data) objects
     * @param ESIndex
     * @param ESId
     * @param ESType
     * @return
     */
    public JestResult get(String ESIndex, String ESId, String ESType) {
        result = null;

        Get get = new Get.Builder(ESIndex, ESId).type(ESType).build();
        try {
            result = jestClient.execute(get);
        } catch (IOException e) {
            LOG.error("*** Get error: " + e.getMessage(), e);
        }
        return result;
    }

    /**
     *
     * @param ESIndex
     * @param ESType
     * @param query
     * @return
     */
    public SearchResult search(String ESIndex, String ESType, String query) {
        result = null;

        Search search = new Search.Builder(query)
                .addIndex(ESIndex)
                .addType(ESType)
                .build();
        SearchResult result = null;
        try {
            result = jestClient.execute(search);
        } catch (IOException e) {
            LOG.error("*** Search error: " + e.getMessage(), e);
        }
        return result;
    }

    /**
     *
     * @param ESIndex
     * @param ESType
     * @param ESId
     * @param query
     * @return JestResult
     */
    public JestResult update(String ESIndex, String ESType, String ESId, String query){
        result = null;

        try {
            result = jestClient.execute(new Update.Builder(query).index(ESIndex).type(ESType).id(ESId).build());
        } catch (IOException e) {
            LOG.error("*** Update error: " + e.getMessage(), e);
        }

        return result;
    }

    /**
     * @apiNote Delete document
     * @param DocId
     * @param ESIndex
     * @param ESType
     * @return JestResult
     */
    public JestResult delete(String DocId, String ESIndex, String ESType){
        result = null;

        try {
            result = jestClient.execute(new Delete.Builder(DocId)
                    .index(ESIndex)
                    .type(ESType)
                    .build());

        } catch (IOException e) {
            LOG.error("*** Delete error: " + e.getMessage(), e);
        }

        return result;
    }
}
