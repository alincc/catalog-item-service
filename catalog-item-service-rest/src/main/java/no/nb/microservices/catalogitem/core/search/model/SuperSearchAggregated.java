package no.nb.microservices.catalogitem.core.search.model;

import org.springframework.hateoas.PagedResources;

import java.util.Map;

public class SuperSearchAggregated {
    private PagedResources.PageMetadata metadata;
    private Map<String, SearchAggregated> searchAggregateds;

    public SuperSearchAggregated(PagedResources.PageMetadata metadata, Map<String, SearchAggregated> searchAggregateds) {
        this.metadata = metadata;
        this.searchAggregateds = searchAggregateds;
    }

    public Map<String, SearchAggregated> getSearchAggregateds() {
        return searchAggregateds;
    }

    public PagedResources.PageMetadata getMetadata() {
        return metadata;
    }
}
