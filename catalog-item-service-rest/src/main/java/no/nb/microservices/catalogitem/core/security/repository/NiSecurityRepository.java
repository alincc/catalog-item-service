package no.nb.microservices.catalogitem.core.security.repository;

import javax.servlet.http.HttpServletRequest;

import no.nb.commons.web.util.UserUtils;
import no.nb.sesam.ni.niclient.NiClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
            HttpServletRequest request = 
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest();
            return niClient.hasAccess(UserUtils.getSsoToken(request), id, null, UserUtils.getClientIp(request));
        } catch (Exception ex) {
            throw new SecurityException("Error getting access info for id " + id, ex);
        }
    }

}
