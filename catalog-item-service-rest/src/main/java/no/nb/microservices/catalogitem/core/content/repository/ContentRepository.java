package no.nb.microservices.catalogitem.core.content.repository;

import no.nb.microservices.catalogcontentsearch.rest.model.AnnotationList;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("catalog-iiif-content-search")
public interface ContentRepository {

    @RequestMapping(method = RequestMethod.GET, value = "/catalog/v1/contentsearch/{id}/search")
    AnnotationList search(@PathVariable("id") String id,
                          @RequestParam("q") String q,
                          @RequestParam("X-Forwarded-Host") String xHost,
                          @RequestParam("X-Forwarded-Port") String xPort,
                          @RequestParam("X-Original-IP-Fra-Frontend") String xRealIp,
                          @RequestParam("amsso") String ssoToken);
}
