package no.nb.microservices.catalogitem.rest.model;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemResource extends ResourceSupport {
    
    @JsonProperty(value="id")
    private String id;
    private Metadata metadata;
    private AccessInfo accessInfo;

    @JsonCreator
    public ItemResource() {
        super();
    }
    
    public ItemResource(String id) {
        super();
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

    public AccessInfo getAccessInfo() {
        return accessInfo;
    }

    public void setAccessInfo(AccessInfo accessInfo) {
        this.accessInfo = accessInfo;
    }

}
