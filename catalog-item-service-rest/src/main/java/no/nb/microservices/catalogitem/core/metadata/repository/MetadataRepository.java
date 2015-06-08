package no.nb.microservices.catalogitem.core.metadata.repository;

import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "catalog-metdata-service")
public interface MetadataRepository {

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/mods")
    Mods getModsById(@PathVariable("id") String id);
    
}
