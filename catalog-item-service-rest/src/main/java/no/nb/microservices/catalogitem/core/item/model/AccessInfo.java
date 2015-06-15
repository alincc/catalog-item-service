package no.nb.microservices.catalogitem.core.item.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author ronnymikalsen
 *
 */
public class AccessInfo {

    private boolean digital;
    private List<String> contentClasses = new ArrayList<>();

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

}
