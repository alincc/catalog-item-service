package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuperEmbeddedWrapper {
    private Map<String, ItemSearchResource> searchResults = new HashMap();

    public Map<String, ItemSearchResource> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(Map<String, ItemSearchResource> searchResults) {
        this.searchResults = searchResults;
    }
}
