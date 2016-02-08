package no.nb.microservices.catalogitem.core.index.model;

import no.nb.microservices.catalogsearchindex.AggregationResource;
import no.nb.microservices.catalogsearchindex.ItemResource;

import java.util.List;

public class SearchResult {

    private long totalElements;
    private List<ItemResource> items;
    private List<AggregationResource> aggregations;
    private String scrollId;

    public SearchResult(List<ItemResource> items, long totalElements, List<AggregationResource> aggregations, String scrollId) {
        super();
        this.items = items;
        this.totalElements = totalElements;
        this.aggregations = aggregations;
        this.scrollId = scrollId;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public List<ItemResource> getItems() {
        return items;
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
}
