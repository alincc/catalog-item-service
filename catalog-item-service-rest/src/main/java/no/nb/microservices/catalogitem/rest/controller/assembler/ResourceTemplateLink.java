package no.nb.microservices.catalogitem.rest.controller.assembler;

public enum ResourceTemplateLink {
    ITEM_SELF("/catalog/v1/items/{id}"),
    MODS ("/catalog/v1/metadata/{id}/mods"),
    PRESENTATION ("/catalog/v1/iiif/{id}/manifest"),
    ENW ("/catalog/v1/reference/{id}/enw"),
    RIS ("/catalog/v1/reference/{id}/ris"),
    WIKI ("/catalog/v1/reference/{id}/wiki"),
    PLAYLIST("/catalog/v1/playlist/{id}/jwplayer.rss"),
    THUMBNAIL("http://www.nb.no/services/image/resolver/{id}/full/{height},0/0/native.jpg"),
    RELATED_ITEMS("/catalog/v1/items/{id}/relatedItems");

    private final String resourceLink;

    ResourceTemplateLink(String resourceLink) {
        this.resourceLink = resourceLink;
    }

    public String getTemplate() {
        return resourceLink;
    }

}
