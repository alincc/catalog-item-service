package no.nb.microservices.catalogitem.rest.model;

import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchResponseResource extends ResourceSupport {
    private PageMetadata metadata;
    private EmbeddedWrapper wrapper = new EmbeddedWrapper(); 

    @JsonCreator
    public SearchResponseResource() {
    }
    
    public SearchResponseResource(PageMetadata metadata) {
        super();
        this.metadata = metadata;
    }

    @JsonProperty("page")
    public PageMetadata getMetadata() {
        return metadata;
    }

    @JsonProperty("_embedded")
    public EmbeddedWrapper getEmbedded() {
        return wrapper;
    }

}
