package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuperEmbeddedWrapper {
    private Map<String, ItemSearchResource> itemsByMediaType = new HashMap();

    public Map<String, ItemSearchResource> getItemsByMediaType() {
        return itemsByMediaType;
    }

    public void setItemsByMediaType(Map<String, ItemSearchResource> itemsByMediaType) {
        this.itemsByMediaType = itemsByMediaType;
    }
}