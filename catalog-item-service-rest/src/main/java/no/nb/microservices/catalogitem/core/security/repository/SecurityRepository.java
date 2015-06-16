package no.nb.microservices.catalogitem.core.security.repository;

/**
 * 
 * @author ronnymikalsen
 *
 */
public interface SecurityRepository {

    boolean hasAccess(String id);

}
