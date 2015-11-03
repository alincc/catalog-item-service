package no.nb.microservices.catalogitem.rest.model;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"expand", "id", "_links", "accessInfo", "metadata" })
public class ItemResource extends ResourceSupport {
    
    @JsonProperty(value="id")
    private String id;
    private String expand;
    private Metadata metadata;
    private AccessInfo accessInfo;
    private RelatedItemResource relatedItems;

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

    public String getExpand() {
        return expand;
    }
    
    public void setExpand(String expand) {
        this.expand = expand;
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

    public RelatedItemResource getRelatedItems() {
        return relatedItems;
    }

    public void setRelatedItems(RelatedItemResource relatedItems) {
        this.relatedItems = relatedItems;
    }

}
