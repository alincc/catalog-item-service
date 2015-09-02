package no.nb.microservices.catalogitem.core.item.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import no.nb.microservices.catalogmetadata.model.fields.FieldResource;

public class AccessInfo {

    public static final String VIEWABILITY_ALL = "ALL";
    public static final String VIEWABILITY_NONE = "NONE";
    
    private boolean digital;
    private List<String> contentClasses = new ArrayList<>();
    private boolean hasAccess;
    
    private AccessInfo(boolean digital, List<String> contentClasses,
            boolean hasAccess) {
        super();
        this.digital = digital;
        this.contentClasses = contentClasses;
        this.hasAccess = hasAccess;
    }

    public boolean isDigital() {
        return digital;
    }

    public List<String> getContentClasses() {
        return Collections.unmodifiableList(contentClasses);
    }

    public boolean isPublicDomain() {
        return getContentClasses().contains("public");
    }

    public boolean hasAccess() {
        return hasAccess;
    }

    public String getViewability() {
        return hasAccess() ? AccessInfo.VIEWABILITY_ALL : AccessInfo.VIEWABILITY_NONE;
    }

    public String accessAllowedFrom() {
        String result = "";
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
    
    public static class Builder {
        
        private FieldResource fields = new FieldResource();
        private boolean hasAccess = false;
        
        public Builder fields(final FieldResource fields) {
            this.fields = fields != null ? fields : new FieldResource();
            return this;
        }

        public Builder hasAccess(final boolean hasAccess) {
            this.hasAccess = hasAccess;
            return this;
        }

        public AccessInfo build() {
            return new AccessInfo(fields.isDigital(), fields.getContentClasses(), hasAccess);
        }
    }

}
