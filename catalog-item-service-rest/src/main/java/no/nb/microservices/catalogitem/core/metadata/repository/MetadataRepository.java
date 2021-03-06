package no.nb.microservices.catalogitem.core.metadata.repository;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

@FeignClient("catalog-metadata-service")
public interface MetadataRepository {

    @RequestMapping(method = RequestMethod.GET, value = "/catalog/v1/metadata/{id}/mods", produces = MediaType.APPLICATION_XML_VALUE)
    Mods getModsById(@PathVariable("id") String id, 
            @RequestParam("X-Forwarded-Host") String xHost, 
            @RequestParam("X-Forwarded-Port") String xPort, 
            @RequestParam("X-Original-IP-Fra-Frontend") String xRealIp, 
            @RequestParam("amsso") String ssoToken);

}
