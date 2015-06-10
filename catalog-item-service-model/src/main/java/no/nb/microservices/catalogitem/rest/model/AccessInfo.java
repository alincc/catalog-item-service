package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author ronnymikalsen
 *
 */
public class AccessInfo {

    @JsonProperty("isDigital")
    private boolean digital;

    public boolean isDigital() {
        return digital;
    }

    public void setDigital(boolean digital) {
        this.digital = digital;
    }
    
}
