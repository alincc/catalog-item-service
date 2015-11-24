package no.nb.microservices.catalogitem.rest.controller.assembler;

public enum ResourceTemplateLink {
    MODS ("/v1/catalog/metadata/{id}/mods"),
    PRESENTATION ("/v1/catalog/iiif/{id}/manifest"),
    ENW ("/v1/catalog/reference/{id}/enw"),
    RIS ("/v1/catalog/reference/{id}/ris"),
    WIKI ("/v1/catalog/reference/{id}/wiki"),
    PLAYLIST("/v1/catalog/playlist/{id}/jwplayer.rss"),
    THUMBNAIL("http://www.nb.no/services/image/resolver/{id}/full/{height},0/0/native.jpg"),
    RELATED_ITEMS("/v1/catalog/items/{id}/relatedItems");

    private final String resourceLink;

    ResourceTemplateLink(String resourceLink) {
        this.resourceLink = resourceLink;
    }

    public String getTemplate() {
        return resourceLink;
    }

}
