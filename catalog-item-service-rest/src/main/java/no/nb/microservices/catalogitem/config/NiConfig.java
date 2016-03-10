package no.nb.microservices.catalogitem.config;

import no.nb.sesam.ni.niclient.NiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"docker", "dev"})
public class NiConfig {

    @Autowired
    private NiSettings niSetting;

    @Bean
    public NiClient getNiClient() {
        try {
            return new NiClient(niSetting.getServers());
        } catch (Exception ex) {
            throw new ConfigurationException("NI Configuration error", ex);
        }
    }
}
