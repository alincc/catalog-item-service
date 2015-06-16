package no.nb.microservices.catalogitem.config;

/**
 * 
 * @author ronnymikalsen
 *
 */
public class ConfigurationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ConfigurationException() {
        super();
    }
    
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
}
