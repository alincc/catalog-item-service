package no.nb.microservices.catalogitem.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="ni")
public class NiSettings {
    
    private String[] servers = {};

    public String[] getServers() {
        return servers;
    }

    public void setServers(String[] servers) {
        this.servers = servers;
    }

}
