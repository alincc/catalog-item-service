package no.nb.microservices.catalogitem.core.index.model;

import no.nb.microservices.catalogsearchindex.AggregationResource;

import java.util.List;

/**
 * 
 * @author ronnymikalsen
 *
 */
public class SearchResult {

    private long totalElements;
    private List<String> ids;
    private List<AggregationResource> aggregations;

    public SearchResult(List<String> ids, long totalElements, List<AggregationResource> aggregations) {
        super();
        this.ids = ids;
        this.totalElements = totalElements;
        this.aggregations = aggregations;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public List<String> getIds() {
        return ids;
    }

    public List<AggregationResource> getAggregations() {
        return aggregations;
    }

    public void setAggregations(List<AggregationResource> aggregations) {
        this.aggregations = aggregations;
    }
}
