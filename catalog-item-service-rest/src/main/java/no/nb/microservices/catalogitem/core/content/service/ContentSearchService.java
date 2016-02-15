package no.nb.microservices.catalogitem.core.content.service;

import no.nb.microservices.catalogitem.core.item.service.SecurityInfo;
import no.nb.microservices.catalogitem.rest.model.ContentSearch;

import java.util.concurrent.Future;

public interface ContentSearchService {

    Future<ContentSearch> search(String id, String queryString, SecurityInfo securityInfo);
}
