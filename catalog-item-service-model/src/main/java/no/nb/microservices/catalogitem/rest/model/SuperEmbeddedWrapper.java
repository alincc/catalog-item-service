package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuperEmbeddedWrapper {
    private List<MediaTypeResult> mediaTypeResults;

    public List<MediaTypeResult> getMediaTypeResults() {
        return mediaTypeResults;
    }

    public void setMediaTypeResults(List<MediaTypeResult> mediaTypeResults) {
        this.mediaTypeResults = mediaTypeResults;
    }
}