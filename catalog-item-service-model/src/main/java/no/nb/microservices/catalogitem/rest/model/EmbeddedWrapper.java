package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class EmbeddedWrapper {
    private List<ItemResource> items = new ArrayList<>();
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
}
