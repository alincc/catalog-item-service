package no.nb.microservices.catalogitem.rest.model;

public class MediaTypeResult {
    private String mediaType;
    private ItemSearchResource result;

    protected MediaTypeResult() {
    }

    public MediaTypeResult(String mediaType, ItemSearchResource result) {
        this.mediaType = mediaType;
        this.result = result;
    }

    public String getMediaType() {
        return mediaType;
    }

    public ItemSearchResource getResult() {
        return result;
    }
}
