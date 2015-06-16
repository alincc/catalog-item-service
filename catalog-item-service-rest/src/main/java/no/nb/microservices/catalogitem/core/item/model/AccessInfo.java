package no.nb.microservices.catalogitem.core.item.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author ronnymikalsen
 *
 */
public class AccessInfo {

    public final static String VIEWABILITY_ALL = "ALL";
    public final static String VIEWABILITY_NONE = "NONE";
    
    private boolean digital;
    private List<String> contentClasses = new ArrayList<>();
    private boolean hasAccess;

    public boolean isDigital() {
        return digital;
    }

    public void setDigital(boolean digital) {
        this.digital = digital;
    }

    public List<String> getContentClasses() {
        return contentClasses;
    }

    public void setContentClasses(List<String> contentClasses) {
        this.contentClasses = contentClasses;
    }
    
    public boolean isPublicDomain() {
        assert contentClasses != null;
        
        return contentClasses.contains("public");
    }

    public boolean isHasAccess() {
        return hasAccess;
    }

    public void setHasAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }
    
    public String getViewability() {
        return hasAccess ? AccessInfo.VIEWABILITY_ALL : AccessInfo.VIEWABILITY_NONE;
    }

}
