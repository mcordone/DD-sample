
package com.aol.advertising.dealdiscovery.forecast.dao.lanaDao;

import com.aol.advertising.dealdiscovery.forecast.application.Config;
import com.aol.advertising.dealdiscovery.forecast.domain.ace.Deal;
import com.aol.advertising.dealdiscovery.forecast.domain.lana.NCRResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

/**
 * Created by mcordones13 on 6/1/16.
 */
@Component
public class LanaDao {

    @Autowired 
    private LanaRestDao lanaRestDao;

    @Autowired
    private Config config;

    private static final Logger LOG = LoggerFactory.getLogger(LanaDao.class);
    private static final String DEAL_BIDS_PAYLOAD = "{" +
            "\"dataType\": \"BID\",\n" +
            "\"user\": \"QAI-deal-discovery\",\n" +
            "\"requests\": [{" +
            "\"path\": \"ncr\"," +
            "\"facet-categories\": [{" +
            "\"path\": \"deal\"," +
            "\"identifier\": \"transactionalElementId\" }]" +
            "}]" +
            "}";

    private static final String DEAL_UNIQUES_PAYLOAD = "{" +
            "\"dataType\": \"BID\",\n" +
            "\"user\": \"QAI-deal-discovery\",\n" +
            "\"queryDays\":[\"UserWeek\"]," +
            "\"requests\": [{" +
            "\"path\": \"ncr\"," +
            "\"facet-categories\": [{" +
            "\"path\": \"deal\"," +
            "\"identifier\": \"transactionalElementId\" }]" +
            "}]" +
            "}";

    private static final String DEAL_BIDS_UNIQUES_PAYLOAD = "{" +
            "\"dataType\": \"BID\",\n" +
            "\"user\": \"QAI-deal-discovery\",\n" +
            "\"queryDays\":[\"Monday\",\"Tuesday\",\"Wednesday\",\"Thursday\",\"Friday\",\"Saturday\",\"Sunday\",\"UserWeek\"]," +
            "\"requests\": [{" +
            "\"path\": \"ncr\"," +
            "\"facet-categories\": [{" +
            "\"path\": \"deal\"," +
            "\"identifier\": \"transactionalElementId\" }]" +
            "}]" +
            "}";

    private static final String GROUP_BIDS_PAYLOAD = "{\n" +
            "   \"dataType\": \"BID\",\n" +
            "   \"user\": \"QAI-deal-discovery\",\n" +
            "   \"requests\":[\n" +
            "      {\n" +
            "         \"path\":\"ncr\",\n" +
            "         \"facet-categories\":[\n" +
            "            {\n" +
            "               \"path\":\"dsl\",\n" +
            "               \"facets\":[\n" +
            "                  {\n" +
            "                     \"path\":\"group\",\n" +
            "                     \"expression\":\"%s\"\n" +
            "                  }\n" +
            "               ]\n" +
            "            }\n" +
            "         ]\n" +
            "      }\n" +
            "   ]\n" +
            "}";

    private static final String GROUP_UNIQUE_PAYLOAD = "{\n" +
            "   \"queryDays\":[\"UserWeek\"]," +
            "   \"dataType\": \"BID\",\n" +
            "   \"user\": \"QAI-deal-discovery\",\n" +
            "   \"requests\":[\n" +
            "      {\n" +
            "         \"path\":\"ncr\",\n" +
            "         \"facet-categories\":[\n" +
            "            {\n" +
            "               \"path\":\"dsl\",\n" +
            "               \"facets\":[\n" +
            "                  {\n" +
            "                     \"path\":\"group\",\n" +
            "                     \"expression\":\"%s\"\n" +
            "                  }\n" +
            "               ]\n" +
            "            }\n" +
            "         ]\n" +
            "      }\n" +
            "   ]\n" +
            "}";

    private static final String GROUP_BIDS_UNIQUE_PAYLOAD = "{\n" +
            "   \"dataType\": \"BID\",\n" +
            "   \"user\": \"QAI-deal-discovery\",\n" +
            "   \"queryDays\":[\"Monday\",\"Tuesday\",\"Wednesday\",\"Thursday\",\"Friday\",\"Saturday\",\"Sunday\",\"UserWeek\"]," +
            "   \"requests\":[\n" +
            "      {\n" +
            "         \"path\":\"ncr\",\n" +
            "         \"facet-categories\":[\n" +
            "            {\n" +
            "               \"path\":\"dsl\",\n" +
            "               \"facets\":[\n" +
            "                  {\n" +
            "                     \"path\":\"group\",\n" +
            "                     \"expression\":\"%s\"\n" +
            "                  }\n" +
            "               ]\n" +
            "            }\n" +
            "         ]\n" +
            "      }\n" +
            "   ]\n" +
            "}";

    private static final String GROUP_EXPRESSION = "transactionalElementId:'27303:%s:%s'";

    private static final String DEAL_COUNTRY_DISTRIBUTION =
            "{\n" +
            "    \"queryDays\": [\"UserWeek\"],\n" +
            "    \"dataType\": \"BID\",\n" +
            "    \"user\": \"QAI-deal-discovery\",\n" +
            "    \"requests\": [{\n" +
            "        \"path\": \"ncr\",\n" +
            "        \"facet-categories\": [{\n" +
            "            \"path\": \"dealCountry\",\n" +
            "            \"identifier\": \"transactionalElementId\",\n" +
            "            \"nested\": [{\n" +
            "                \"path\": \"country\",\n" +
            "                \"identifier\": \"element-type:country\"\n" +
            "            }]\n" +
            "        }]\n" +
            "    }]\n" +
            "}";

    private static final String GROUP_COUNTRY_DISTRIBUTION =
            "{\n" +
            "    \"queryDays\": [\"UserWeek\"],\n" +
            "    \"dataType\": \"BID\",\n" +
            "    \"user\": \"QAI-deal-discovery\",\n" +
            "    \"requests\": [{\n" +
            "        \"path\": \"ncr\",\n" +
            "        \"facet-categories\": [{\n" +
            "                \"path\": \"groupCountry\",\n" +
            "                \"facets\":[{\n" +
            "                     \"path\":\"deals\",\n" +
            "                     \"expression\":\"%s\"\n" +
            "                  }],\n" +
            "                \"nested\": [{\n" +
            "                  \"path\": \"country\",\n" +
            "                  \"identifier\": \"element-type:country\"\n" +
            "                 }]\n" +
            "        }]\n" +
            "    }]\n" +
            "}";

    private static final String LANA_ACCEPT_HEADER_AGGREGATED_RESPONSE ="application/vnd.lana.query.aggregatedResponse.V1+json";
    private static final String LANA_ACCEPT_HEADER_BLENDED_NCR_RESPONSE ="application/vnd.lana.query.blendedNcrResponse.V1+json";
    private static final String LANA_MEDIA_TYPE_HEADER_TYPE ="application";
    private static final String LANA_MEDIA_TYPE_HEADER_SUBTYPE ="vnd.lana.network.request.v1+json";

    /**
     * @apiNote return bids data per day basis
     */
    public NCRResult getAggregatedLanaDealBids() {
        MultivaluedMap<String, String> headers = new MultivaluedHashMap<String, String>();
        headers.add("mediaType", LANA_MEDIA_TYPE_HEADER_TYPE);
        headers.add("mediaSubType", LANA_MEDIA_TYPE_HEADER_SUBTYPE);
        headers.add("accept", LANA_ACCEPT_HEADER_AGGREGATED_RESPONSE);

        LOG.debug("sending post request to get aggregated bids data for deals");

        NCRResult lanaRes = lanaRestDao.post(config.getLanaApiUri() , DEAL_BIDS_PAYLOAD, headers, NCRResult.class);
        return lanaRes;
    }

    /**
     * @apiNote return average number of bids per day over a 7 day period
     */
    public NCRResult getBlendedLanaDealBids() {
        MultivaluedMap<String, String> headers = new MultivaluedHashMap<String, String>();
        headers.add("mediaType", LANA_MEDIA_TYPE_HEADER_TYPE);
        headers.add("mediaSubType", LANA_MEDIA_TYPE_HEADER_SUBTYPE);
        headers.add("accept", LANA_ACCEPT_HEADER_BLENDED_NCR_RESPONSE);

        LOG.debug("sending post request to get blended bids data for deals");
        NCRResult lanaRes = lanaRestDao.post(config.getLanaApiUri(), DEAL_BIDS_PAYLOAD, headers, NCRResult.class);

        return lanaRes;
    }

    /**
     * @apiNote return unique users data per day basis
     */
    public NCRResult getAggregatedLanaDealUniqueUsers() {
        MultivaluedMap<String, String> headers = new MultivaluedHashMap<String, String>();
        headers.add("mediaType", LANA_MEDIA_TYPE_HEADER_TYPE);
        headers.add("mediaSubType", LANA_MEDIA_TYPE_HEADER_SUBTYPE);
        headers.add("accept", LANA_ACCEPT_HEADER_AGGREGATED_RESPONSE);

        LOG.debug("sending post request to get aggregated unique users data for deals");
        NCRResult lanaRes = lanaRestDao.post(config.getLanaApiUri(), DEAL_UNIQUES_PAYLOAD, headers, NCRResult.class);

        return lanaRes;
    }

    /**
     * @apiNote return bids data per day and average number of unique users per day over a 7 day period
     */
    public NCRResult getAggregatedLanaDealBidsAndUniqueUsers() {
        MultivaluedMap<String, String> headers = new MultivaluedHashMap<String, String>();
        headers.add("mediaType", LANA_MEDIA_TYPE_HEADER_TYPE);
        headers.add("mediaSubType", LANA_MEDIA_TYPE_HEADER_SUBTYPE);
        headers.add("accept", LANA_ACCEPT_HEADER_AGGREGATED_RESPONSE);

        LOG.debug("sending post request to get aggregated unique users data and bids data for deals");
        NCRResult lanaRes = lanaRestDao.post(config.getLanaApiUri(), DEAL_BIDS_UNIQUES_PAYLOAD, headers, NCRResult.class);

        return lanaRes;
    }

    /**
     * @apiNote return average number of group bids per day over a 7 day period (based on a list of deals)
     */
    public NCRResult getBlendedLanaGroupBids(List<Deal> dealList) {
        MultivaluedMap<String, String> headers = new MultivaluedHashMap<String, String>();
        headers.add("mediaType", LANA_MEDIA_TYPE_HEADER_TYPE);
        headers.add("mediaSubType", LANA_MEDIA_TYPE_HEADER_SUBTYPE);
        headers.add("accept", LANA_ACCEPT_HEADER_BLENDED_NCR_RESPONSE);

        NCRResult lanaRes = lanaRestDao.post(config.getLanaApiUri(), generateGroupPayload(GROUP_BIDS_PAYLOAD, dealList), headers, NCRResult.class);
        return lanaRes;
    }

    /**
     * @apiNote return group uniques data per day basis (based on a list of deals)
     */
    public NCRResult getAggregatedLanaGroupUniques(List<Deal> dealList) {
        MultivaluedMap<String, String> headers = new MultivaluedHashMap<String, String>();
        headers.add("mediaType", LANA_MEDIA_TYPE_HEADER_TYPE);
        headers.add("mediaSubType", LANA_MEDIA_TYPE_HEADER_SUBTYPE);
        headers.add("accept", LANA_ACCEPT_HEADER_AGGREGATED_RESPONSE);

        NCRResult lanaRes = lanaRestDao.post(config.getLanaApiUri(), generateGroupPayload(GROUP_UNIQUE_PAYLOAD, dealList), headers, NCRResult.class);
        return lanaRes;
    }

    public NCRResult getAggregatedLanaGroupBidsAndUniques(List<Deal> dealList) {
        MultivaluedMap<String, String> headers = new MultivaluedHashMap<String, String>();
        headers.add("mediaType", LANA_MEDIA_TYPE_HEADER_TYPE);
        headers.add("mediaSubType", LANA_MEDIA_TYPE_HEADER_SUBTYPE);
        headers.add("accept", LANA_ACCEPT_HEADER_AGGREGATED_RESPONSE);

        LOG.debug("sending post request to get aggregated unique users data and bids data for deals");
        NCRResult lanaRes = lanaRestDao.post(config.getLanaApiUri(), generateGroupPayload(GROUP_BIDS_UNIQUE_PAYLOAD, dealList), headers, NCRResult.class);
        return lanaRes;
    }

    public NCRResult getDealCountryBid() {
        MultivaluedMap<String, String> headers = new MultivaluedHashMap<String, String>();
        headers.add("mediaType", LANA_MEDIA_TYPE_HEADER_TYPE);
        headers.add("mediaSubType", LANA_MEDIA_TYPE_HEADER_SUBTYPE);
        headers.add("accept", LANA_ACCEPT_HEADER_AGGREGATED_RESPONSE);

        LOG.debug("sending post request to get country distribution data for deal");
        NCRResult lanaRes = lanaRestDao.post(config.getLanaFacetUri(), DEAL_COUNTRY_DISTRIBUTION, headers, NCRResult.class);
        return lanaRes;
    }

    public NCRResult getGroupCountryBid(List<Deal> dealList) {
        MultivaluedMap<String, String> headers = new MultivaluedHashMap<String, String>();
        headers.add("mediaType", LANA_MEDIA_TYPE_HEADER_TYPE);
        headers.add("mediaSubType", LANA_MEDIA_TYPE_HEADER_SUBTYPE);
        headers.add("accept", LANA_ACCEPT_HEADER_AGGREGATED_RESPONSE);

        LOG.debug("sending post request to get country distribution data for a group");
        NCRResult lanaRes = lanaRestDao.post(config.getLanaApiUri(), generateGroupPayload(GROUP_COUNTRY_DISTRIBUTION, dealList), headers, NCRResult.class);
        return lanaRes;
    }

    private String generateGroupPayload(String payloadBase, List<Deal> dealList) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dealList.size(); i++) {
            Deal deal = dealList.get(i);
            sb.append(String.format(GROUP_EXPRESSION, deal.getProviderId(), deal.getDealId()));
            if (i < dealList.size() - 1) {
                sb.append(" or ");
            }
        }
        String expression = sb.toString();
        String query = String.format(payloadBase, expression);
        return query;
    }
}
