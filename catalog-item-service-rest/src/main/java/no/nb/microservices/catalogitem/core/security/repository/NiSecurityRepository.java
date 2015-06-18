package no.nb.microservices.catalogitem.core.security.repository;

import no.nb.commons.web.util.UserUtils;
import no.nb.sesam.ni.niclient.NiClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author ronnymikalsen
 *
 */
@Repository
public class NiSecurityRepository implements SecurityRepository {

    private final NiClient niClient;
    
    @Autowired    
    public NiSecurityRepository(NiClient niClient) {
        super();
        this.niClient = niClient;
    }

    @Override
    public boolean hasAccess(String id) {
        try {
            return niClient.hasAccess(UserUtils.getSsoToken(), id, null, UserUtils.getClientIp());
        } catch (Exception ex) {
            throw new SecurityException("Error getting access info for id " + id, ex);
        }
    }

}
