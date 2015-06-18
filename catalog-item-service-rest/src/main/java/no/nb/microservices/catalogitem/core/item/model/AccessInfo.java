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
