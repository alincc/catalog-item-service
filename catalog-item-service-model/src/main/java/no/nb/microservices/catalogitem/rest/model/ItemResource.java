package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.annotation.*;
import org.springframework.hateoas.ResourceSupport;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"expand", "id", "title", "_links", "accessInfo", "metadata" })
public class ItemResource extends ResourceSupport {
    
    @JsonProperty(value="id")
    private String id;
    private String title;
    private String expand;
    private Metadata metadata;
    private AccessInfo accessInfo;
    private RelatedItemResource relatedItems;
    private JsonNode explain;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public JsonNode getExplain() {
        return explain;
    }

    public void setExplain(JsonNode explain) {
        this.explain = explain;
    }
}
