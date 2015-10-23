package no.nb.microservices.catalogitem.core.index.service;

import no.nb.microservices.catalogitem.core.index.model.SearchResult;
import no.nb.microservices.catalogitem.core.item.service.SecurityInfo;

public interface IndexService {

    SearchResult search(String q, SecurityInfo securityInfo);
    
}
