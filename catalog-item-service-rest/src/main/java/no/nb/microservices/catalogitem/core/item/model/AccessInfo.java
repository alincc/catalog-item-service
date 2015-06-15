package no.nb.microservices.catalogitem.core.item.model;

/**
 * 
 * @author ronnymikalsen
 *
 */
public class AccessInfo {

    private boolean digital;
    private boolean publicDomain;

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
}
