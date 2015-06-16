package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author ronnymikalsen
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessInfo {

    @JsonProperty("isDigital")
    private boolean digital;
    
    @JsonProperty("isPublicDomain")
    private boolean publicDomain;

    private String viewability;
    
    public boolean isDigital() {
        return digital;
    }

    public void setDigital(boolean digital) {
        this.digital = digital;
    }

    public boolean isPublicDomain() {
        return publicDomain;
    }

    public void setPublicDomain(boolean publicDomain) {
        this.publicDomain = publicDomain;
    }

    public String getViewability() {
        return viewability;
    }

    public void setViewability(String viewability) {
        this.viewability = viewability;
    }

    
}
