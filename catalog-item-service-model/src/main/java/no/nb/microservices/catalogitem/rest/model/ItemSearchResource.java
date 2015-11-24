package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceSupport;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ItemSearchResource extends ResourceSupport {
    @JsonProperty("page")
    private PagedResources.PageMetadata metadata;
    @JsonProperty("_embedded")
    private EmbeddedWrapper wrapper = new EmbeddedWrapper();

    @JsonCreator
    public ItemSearchResource() {
    }

    public ItemSearchResource(PagedResources.PageMetadata metadata) {
        super();
        this.metadata = metadata;
    }

    @JsonIgnore
    public PagedResources.PageMetadata getMetadata() {
        return metadata;
    }

    @JsonIgnore
    public EmbeddedWrapper getEmbedded() {
        return wrapper;
    }

}