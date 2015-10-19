package no.nb.microservices.catalogitem.rest.controller.assembler;

public enum ResourceTemplateLink {
    MODS ("/catalog/metadata/{id}/mods"),
    PRESENTATION ("/catalog/iiif/{id}/manifest"),
    ENW ("/catalog/reference/{id}/enw"),
    RIS ("/catalog/reference/{id}/ris"),
    WIKI ("/catalog/reference/{id}/wiki");
    
    private final String resourceLink;

    ResourceTemplateLink(String resourceLink) {
        this.resourceLink = resourceLink;
    }

    public String getTemplate() {
        return resourceLink;
    }

}
