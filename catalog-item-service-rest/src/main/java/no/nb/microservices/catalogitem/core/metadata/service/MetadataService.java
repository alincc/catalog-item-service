package no.nb.microservices.catalogitem.core.metadata.service;

import java.util.concurrent.Future;

import no.nb.microservices.catalogitem.core.item.service.TracableId;
import no.nb.microservices.catalogmetadata.model.fields.FieldResource;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

public interface MetadataService {

    Future<Mods> getModsById(TracableId id);

    Future<FieldResource> getFieldsById(TracableId id);
    
}
