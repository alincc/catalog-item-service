package no.nb.microservices.catalogitem.core.search.model;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.rest.model.ContentSearch;
import no.nb.microservices.catalogsearchindex.SimpleAggregation;
import org.springframework.data.domain.Page;

import java.util.List;

public class SearchAggregated {
    private Page<Item> page;
    private List<SimpleAggregation> aggregations;
    private String scrollId;
    private SearchRequest searchRequest;
    private List<ContentSearch> contentSearches;

    public SearchAggregated(Page<Item> page, List<SimpleAggregation> aggregations, String scrollId, SearchRequest searchRequest) {
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

    public List<SimpleAggregation> getAggregations() {
        return aggregations;
    }

    public void setAggregations(List<SimpleAggregation> aggregations) {
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

    public List<ContentSearch> getContentSearches() {
        return contentSearches;
    }

    public void setContentSearches(List<ContentSearch> contentSearches) {
        this.contentSearches = contentSearches;
    }
}
