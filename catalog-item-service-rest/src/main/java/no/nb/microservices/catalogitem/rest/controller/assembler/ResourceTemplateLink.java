package no.nb.microservices.catalogitem.rest.controller.assembler;

public enum ResourceTemplateLink {
    MODS ("/catalog/metadata/{id}/mods"),
    PRESENTATION ("/catalog/iiif/{id}/manifest"),
    ENW ("/catalog/reference/{id}/enw"),
    RIS ("/catalog/reference/{id}/ris"),
    WIKI ("/catalog/reference/{id}/wiki"),
    PLAYLIST("/catalog/playlist/{id}/jwplayer.rss"),
    THUMBNAIL("http://www.nb.no/services/image/resolver/{id}/full/{height},0/0/native.jpg"),
    RELATED_ITEMS("/catalog/items/{id}/relatedItems");

    private final String resourceLink;

    ResourceTemplateLink(String resourceLink) {
        this.resourceLink = resourceLink;
    }

    public String getTemplate() {
        return resourceLink;
    }

}
