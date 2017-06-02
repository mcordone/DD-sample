
package com.aol.advertising.dealdiscovery.forecast.util;

import com.aol.advertising.dealdiscovery.forecast.domain.ace.Deal;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.searchbox.client.JestResult;
import io.searchbox.core.SearchResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mcordones13 on 7/14/16.
 */
public final class ESQueryUtil {

    private ESQueryUtil() {
        // not called
    }

    public static String generateGetDealsQuery(List<Deal> dealList) {
        String queryBase =
                "{%n" +
                        "   \"size\":1000,%n" +
                        "   \"fields\" : [\"dealId\", \"providerId\"],%n" +
                        "   \"query\":{%n" +
                        "      \"ids\":{%n" +
                        "         \"values\":[%s]%n" +
                        "      }%n" +
                        "   }%n" +
                        "}";

        return String.format(queryBase, generateQueryBlockForDealValues(dealList));
    }

    private static String generateQueryBlockForDealValues(List<Deal> dealList) {
        String queryBase = "\"%s_%s\"";

        dealList = dealList
                .stream()
                .filter(p -> p != null)
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dealList.size(); i++) {
            String dealQuery = String.format(queryBase, dealList.get(i).getProviderId(), dealList.get(i).getDealId());
            sb.append(dealQuery);
            if (i < dealList.size() - 1) {
                sb.append(",");
            }
        }

        return sb.toString();
    }

    public static JsonArray parseSearchResultToJsonArray(SearchResult searchResult) {
        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse(searchResult.getJsonString()).getAsJsonObject();
        JsonArray jsonArray = o.get("hits").getAsJsonObject().get("hits").getAsJsonArray();
        return jsonArray;
    }

    public static int getBulkInsertCount(JestResult jestResult) {
        int result = 0;
        if (jestResult != null) {
            JsonElement items = jestResult.getJsonObject().get("items");
            if (items != null) {
                result = items.getAsJsonArray().size();
            }
        }
        return result;
    }

    public static List<Deal> parseJsonArrayToUniqueDealList(JsonArray jsonArray) {
        HashSet<Deal> dealSet = new HashSet<Deal>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonElement fields = jsonArray.get(i).getAsJsonObject().get("fields");
            String dealId = fields.getAsJsonObject().get("dealId").getAsString();
            Long providerId = fields.getAsJsonObject().get("providerId").getAsLong();
            Deal deal = new Deal(providerId, dealId);
            dealSet.add(deal);
        }
        return new ArrayList(dealSet);
    }
}
