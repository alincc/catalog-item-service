package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

@JsonIgnoreProperties(ignoreUnknown=true)
public class SuperItemSearchResource extends ResourceSupport {

    @JsonProperty("_embedded")
    private SuperEmbeddedWrapper wrapper = new SuperEmbeddedWrapper();

    @JsonIgnore
    public SuperEmbeddedWrapper getEmbedded() {
        return wrapper;
    }
}
