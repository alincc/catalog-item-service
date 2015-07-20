package no.nb.microservices.catalogitem.core.security.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import no.nb.htrace.annotation.Traceable;
import no.nb.sesam.ni.niclient.NiClient;

@Repository
public class NiSecurityRepository implements SecurityRepository {

    private final NiClient niClient;
    
    @Autowired    
    public NiSecurityRepository(NiClient niClient) {
        super();
        this.niClient = niClient;
    }

    @Override
    @Traceable(description="NI hasAccess")
    public boolean hasAccess(String id, String clientIp, String ssoToken) {
        try {
            return niClient.hasAccess(ssoToken, id, null, clientIp);
        } catch (Exception ex) {
            throw new SecurityException("Error getting access info for id " + id, ex);
        }
    }

}
