package no.nb.microservices.catalogitem.rest.controller;

public enum ResourceTemplateLink {
    MODS ("/catalog/metadata/{id}/mods");
    
    private final String resourceLink;

    ResourceTemplateLink(String resourceLink) {
        this.resourceLink = resourceLink;
    }

    public String getTemplate() {
        return resourceLink;
    }

}
