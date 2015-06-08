package no.nb.microservices.catalogitem.core.metadata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nb.microservices.catalogitem.core.metadata.repository.MetadataRepository;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

@Service
public class MetadataServiceImpl  implements IMetadataService{

    MetadataRepository metadataRepository;
    
    @Autowired
    public MetadataServiceImpl(MetadataRepository metadataRepository) {
        super();
        this.metadataRepository = metadataRepository;
    }

    @Override
    public Mods getModsById(String id) {
        return metadataRepository.getModsById(id);
    }

}
