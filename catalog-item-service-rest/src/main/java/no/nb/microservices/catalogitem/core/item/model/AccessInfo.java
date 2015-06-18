package no.nb.microservices.catalogitem.core.item.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author ronnymikalsen
 * @author rolfmathisen
 *
 */
public class AccessInfo {

    public static final String VIEWABILITY_ALL = "ALL";
    public static final String VIEWABILITY_NONE = "NONE";
    
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
        assert getContentClasses() != null;
        
        return getContentClasses().contains("public");
    }

    public boolean hasAccess() {
        return hasAccess;
    }

    public void setHasAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }
    
    public String getViewability() {
        return hasAccess() ? AccessInfo.VIEWABILITY_ALL : AccessInfo.VIEWABILITY_NONE;
    }

	public String accessAllowedFrom() {
		if (contentClasses.contains("public") | contentClasses.contains("mavispublic") | contentClasses.contains("statfjordpublic") | 
				contentClasses.contains("friggpublic") | contentClasses.contains("showonly")) {
			return "EVERYWHERE";
		} else if (contentClasses.contains("bokhylla")) {
			return "NORWAY";
		} else if (contentClasses.contains("avisibibliotek")) {
			return  "LIBRARY";
		} else if (contentClasses.contains("restricted")) {
			return "NB";
		} else {
			return "UNIVERSALLY_RESTRICTED";
		}
	}

}
