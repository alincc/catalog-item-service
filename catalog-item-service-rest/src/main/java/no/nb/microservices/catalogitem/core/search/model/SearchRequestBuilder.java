package no.nb.microservices.catalogitem.core.search.model;

import no.nb.microservices.catalogsearchindex.NBSearchType;

import java.util.Arrays;
import java.util.List;

public class SearchRequestBuilder {

    private String q;
    private String aggs;
    private NBSearchType searchType;
    private List<String> filter;
    private List<String> boost;
    private String bottomLeft;
    private String topRight;
    private String precision;
    private List<String> fields;
    private List<String> sort;
    private boolean explain;
    private boolean grouping;
    private List<String> should;

    public SearchRequestBuilder() {
    }

    public SearchRequestBuilder(String q, String aggs, NBSearchType searchType, String[] filter, String[] boost,
                                String bottomLeft, String topRight, String precision, String[] fields, String[] sort,
                                boolean explain, boolean grouping, String[] should) {
        this.q = q;
        this.aggs = aggs;
        this.searchType = searchType;
        this.filter = getAsList(filter);
        this.boost = getAsList(boost);
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
        this.precision = precision;
        this.fields = getAsList(fields);
        this.sort = getAsList(sort);
        this.explain = explain;
        this.grouping = grouping;
        this.should = getAsList(should);
    }

    public SearchRequestBuilder(String q, String aggs, NBSearchType searchType, List<String> filter, List<String> boost,
                                String bottomLeft, String topRight, String precision, List<String> fields, List<String> sort,
                                boolean explain, boolean grouping, List<String> should) {
        this.q = q;
        this.aggs = aggs;
        this.searchType = searchType;
        this.filter = filter;
        this.boost = boost;
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
        this.precision = precision;
        this.fields = fields;
        this.sort = sort;
        this.explain = explain;
        this.grouping = grouping;
        this.should = should;
    }

    public SearchRequestBuilder(SearchRequest searchRequest) {
        this.q = searchRequest.getQ();
        this.aggs = searchRequest.getAggs();
        this.searchType = searchRequest.getSearchType();
        this.filter = searchRequest.getFilter();
        this.boost = searchRequest.getBoost();
        this.bottomLeft = searchRequest.getBottomLeft();
        this.topRight = searchRequest.getTopRight();
        this.precision = searchRequest.getPrecision();
        this.fields = searchRequest.getFields();
        this.explain = searchRequest.isExplain();
        this.grouping = searchRequest.isGrouping();
        this.sort = searchRequest.getSort();
        this.should = searchRequest.getShould();
    }

    public SearchRequestBuilder withQ(String q) {
        this.q = q;
        return this;
    }

    public SearchRequestBuilder withFilter(String[] filter) {
        this.filter = Arrays.asList(filter);
        return this;
    }

    private String[] getAsArray(List<String> list) {
        String[] array = new String[list.size()];
        return list.toArray(array);
    }

    private List<String> getAsList(String[] array) {
        if(array != null) {
            return Arrays.asList(array);
        }
        return null;
    }

    public SearchRequest build() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQ(q);
        searchRequest.setAggs(aggs);
        searchRequest.setSearchType(searchType);

        if(filter != null) {
            searchRequest.setFilter(getAsArray(filter));
        }

        if(boost != null) {
            searchRequest.setBoost(getAsArray(boost));
        }

        if(sort != null) {
            searchRequest.setSort(getAsArray(sort));
        }

        searchRequest.setBottomLeft(bottomLeft);
        searchRequest.setTopRight(topRight);
        searchRequest.setPrecision(precision);
        searchRequest.setFields(fields);
        searchRequest.setGrouping(grouping);
        searchRequest.setExplain(explain);
        searchRequest.setShould(should);

        return searchRequest;
    }
}
