package no.nb.microservices.catalogitem.core.search.model;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogsearchindex.AggregationResource;
import org.springframework.data.domain.Page;

import java.util.List;

public class SearchAggregated {
    private Page<Item> page;
    private List<AggregationResource> aggregations;
    private String scrollId;
    private SearchRequest searchRequest;

    public SearchAggregated(Page<Item> page, List<AggregationResource> aggregations, String scrollId, SearchRequest searchRequest) {
        this.page = page;
        this.aggregations = aggregations;
        this.scrollId = scrollId;
        this.searchRequest = searchRequest;
    }

    public Page<Item> getPage() {
        return page;
    }

    public void setPage(Page<Item> page) {
        this.page = page;
    }

    public List<AggregationResource> getAggregations() {
        return aggregations;
    }

    public void setAggregations(List<AggregationResource> aggregations) {
        this.aggregations = aggregations;
    }

    public String getScrollId() {
        return scrollId;
    }

    public void setScrollId(String scrollId) {
        this.scrollId = scrollId;
    }

    public SearchRequest getSearchRequest() {
        return searchRequest;
    }

    public void setSearchRequest(SearchRequest searchRequest) {
        this.searchRequest = searchRequest;
    }
}
