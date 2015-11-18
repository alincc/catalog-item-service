package no.nb.microservices.catalogitem.core.search.model;

import no.nb.microservices.catalogsearchindex.AggregationResource;
import no.nb.microservices.catalogsearchindex.ItemResource;
import org.springframework.data.domain.Page;

import java.util.List;

public class SearchAggregated {
    private Page<ItemResource> page;
    private List<AggregationResource> aggregations;

    public SearchAggregated(Page<ItemResource> page, List<AggregationResource> aggregations) {
        this.page = page;
        this.aggregations = aggregations;
    }

    public Page<ItemResource> getPage() {
        return page;
    }

    public void setPage(Page<ItemResource> page) {
        this.page = page;
    }

    public List<AggregationResource> getAggregations() {
        return aggregations;
    }

    public void setAggregations(List<AggregationResource> aggregations) {
        this.aggregations = aggregations;
    }
}
