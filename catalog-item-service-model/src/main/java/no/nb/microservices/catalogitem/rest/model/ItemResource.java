package no.nb.microservices.catalogitem.rest.model;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemResource extends ResourceSupport {
    
    private String id;

    @JsonCreator
    public ItemResource() {
    }
    
    @JsonProperty(value="id")
    public String geItemtId() {
        return id;
    }

}
