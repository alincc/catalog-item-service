package no.nb.microservices.catalogitem.rest.controller.assembler;

import java.util.List;

import no.nb.microservices.catalogitem.rest.model.AccessInfo;
import no.nb.microservices.catalogmetadata.model.fields.FieldResource;

public final class AccessInfoBuilder {
    public static final String VIEWABILITY_ALL = "ALL";
    public static final String VIEWABILITY_NONE = "NONE";
    
    private FieldResource fields = new FieldResource();
    private boolean hasAccess = false;
    
    public AccessInfoBuilder fields(final FieldResource fields) {
        this.fields = fields != null ? fields : new FieldResource();
        return this;
    }

    public AccessInfoBuilder access(final boolean hasAccess) {
        this.hasAccess = hasAccess;
        return this;
    }

    public AccessInfo build() {
        AccessInfo accessInfo = new AccessInfo();
        accessInfo.setDigital(fields.isDigital());
        accessInfo.setPublicDomain(isPublicDomain());
        accessInfo.setAccessAllowedFrom(accessAllowedFrom());
        accessInfo.setViewability(getViewability());
        return accessInfo;
    }
    
    private boolean isPublicDomain() {
        return fields.getContentClasses().contains("public");
    }
    
    private String accessAllowedFrom() {
        String result = "";
        List<String> contentClasses = fields.getContentClasses();
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
