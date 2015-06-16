package no.nb.microservices.catalogitem.config;

import no.nb.sesam.ni.niclient.NiClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @author ronnymikalsen
 *
 */
@Configuration
public class NiConfig {

    private final NiSettings niSetting;

    @Autowired
    public NiConfig(NiSettings niSetting) {
        super();
        this.niSetting = niSetting;
    }

    @Bean
    public NiClient getNiClient() {
        try {
            return new NiClient(niSetting.getServers());
        } catch (Exception ex) {
            throw new ConfigurationException("NI Configuration error", ex);
        }
    }
}
