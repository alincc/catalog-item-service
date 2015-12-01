package no.nb.microservices.catalogitem.rest.controller.assembler;

import java.util.List;

import no.nb.microservices.catalogitem.rest.model.AccessInfo;

public final class AccessInfoBuilder {
    public static final String VIEWABILITY_ALL = "ALL";
    public static final String VIEWABILITY_NONE = "NONE";
    
    private boolean hasAccess = false;
    private no.nb.microservices.catalogsearchindex.ItemResource itemResource;
    
    public AccessInfoBuilder access(final boolean hasAccess) {
        this.hasAccess = hasAccess;
        return this;
    }

    public AccessInfoBuilder setItemResource(
            no.nb.microservices.catalogsearchindex.ItemResource itemResource) {
        this.itemResource = itemResource;
        return this;
    }


    public AccessInfo build() {
        AccessInfo accessInfo = new AccessInfo();
        accessInfo.setDigital(itemResource.isDigital());
        accessInfo.setPublicDomain(isPublicDomain());
        accessInfo.setAccessAllowedFrom(accessAllowedFrom());
        accessInfo.setViewability(getViewability());
        return accessInfo;
    }
    
    private boolean isPublicDomain() {
        return itemResource.getContentClasses().contains("public");
    }
    
    private String accessAllowedFrom() {
        String result = "";
        List<String> contentClasses = itemResource.getContentClasses();
        if (contentClasses.contains("public") || contentClasses.contains("mavispublic") || contentClasses.contains("statfjordpublic") || 
                contentClasses.contains("friggpublic") || contentClasses.contains("showonly")) {
            result = "EVERYWHERE";
        } else if (contentClasses.contains("bokhylla")) {
            result = "NORWAY";
        } else if (contentClasses.contains("avisibibliotek")) {
            result =  "LIBRARY";
        } else if (contentClasses.contains("restricted")) {
            result = "NB";
        } else {
            result = "UNIVERSALLY_RESTRICTED";
        }
        return result;
    }    
    
    private String getViewability() {
        return hasAccess ? AccessInfoBuilder.VIEWABILITY_ALL : AccessInfoBuilder.VIEWABILITY_NONE;
    }

}
