package no.nb.microservices.catalogitem.core.metadata.service;

import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

public interface MetadataService {

    Mods getModsById(String id, String xHost, String xPort, String xRealIp,
            String sso);
    
}
