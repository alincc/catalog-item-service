package no.nb.microservices.catalogitem.core.index.service;

import no.nb.microservices.catalogitem.core.index.model.SearchResult;
import no.nb.microservices.catalogitem.core.item.service.SecurityInfo;
import no.nb.microservices.catalogitem.core.item.service.TracableId;
import no.nb.microservices.catalogitem.core.search.model.SearchRequest;
import no.nb.microservices.catalogsearchindex.SearchResource;
import org.springframework.data.domain.Pageable;

import java.util.concurrent.Future;

public interface IndexService {

    SearchResult search(SearchRequest searchRequest, Pageable pageable, SecurityInfo securityInfo);
    Future<SearchResource> getSearchResource(TracableId id);
    
}
