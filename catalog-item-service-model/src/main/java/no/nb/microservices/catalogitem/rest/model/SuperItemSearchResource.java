package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceSupport;

@JsonIgnoreProperties(ignoreUnknown=true)
public class SuperItemSearchResource extends ResourceSupport {

    @JsonProperty("page")
    private PagedResources.PageMetadata metadata;

    @JsonProperty("_embedded")
    private SuperEmbeddedWrapper wrapper = new SuperEmbeddedWrapper();

    public SuperItemSearchResource() {
    }

    public SuperItemSearchResource(PagedResources.PageMetadata metadata, SuperEmbeddedWrapper wrapper) {
        this.metadata = metadata;
        this.wrapper = wrapper;
    }

    @JsonIgnore
    public SuperEmbeddedWrapper getEmbedded() {
        return wrapper;
    }

    @JsonIgnore
    public PagedResources.PageMetadata getMetadata() {
        return metadata;
    }
}
