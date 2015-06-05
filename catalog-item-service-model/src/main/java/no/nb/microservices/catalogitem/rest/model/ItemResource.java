package no.nb.microservices.catalogitem.rest.model;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemResource extends ResourceSupport {
    
    @JsonProperty(value="id")
    private String id;
    private Metadata metadata;

    @JsonCreator
    public ItemResource(String id) {
        this.id = id;
    }
    
    public String geItemtId() {
        return id;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

}
