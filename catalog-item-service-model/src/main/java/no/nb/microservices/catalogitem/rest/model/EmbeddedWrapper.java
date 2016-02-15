package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EmbeddedWrapper {
    private List<ItemResource> items = new ArrayList<>();
    private List<ContentSearch> contentSearch;
    private JsonNode aggregations;

    public List<ItemResource> getItems() {
        return items;
    }

    public void setItems(List<ItemResource> items) {
        this.items = items;
    }

    public JsonNode getAggregations() {
        return aggregations;
    }

    public void setAggregations(JsonNode aggregations) {
        this.aggregations = aggregations;
    }

    public List<ContentSearch> getContentSearch() {
        return contentSearch;
    }

    public void setContentSearch(List<ContentSearch> contentSearch) {
        this.contentSearch = contentSearch;
    }
}
