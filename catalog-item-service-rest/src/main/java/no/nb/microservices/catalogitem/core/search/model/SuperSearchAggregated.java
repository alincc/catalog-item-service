package no.nb.microservices.catalogitem.core.search.model;

import java.util.Map;

public class SuperSearchAggregated {
    private Map<String, SearchAggregated> searchAggregateds;

    public SuperSearchAggregated(Map<String, SearchAggregated> searchAggregateds) {
        this.searchAggregateds = searchAggregateds;
    }

    public Map<String, SearchAggregated> getSearchAggregateds() {
        return searchAggregateds;
    }
}
