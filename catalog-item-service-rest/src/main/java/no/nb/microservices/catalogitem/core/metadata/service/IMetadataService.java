package no.nb.microservices.catalogitem.core.metadata.service;

import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

public interface IMetadataService {

    Mods getModsById(String id);
    
}
