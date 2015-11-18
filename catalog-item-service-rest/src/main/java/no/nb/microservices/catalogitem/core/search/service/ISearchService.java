package no.nb.microservices.catalogitem.core.search.service;

import no.nb.microservices.catalogitem.core.item.service.SecurityInfo;
import no.nb.microservices.catalogitem.core.search.model.SearchAggregated;
import no.nb.microservices.catalogitem.core.search.model.SearchRequest;
import org.springframework.data.domain.Pageable;

public interface ISearchService {

    SearchAggregated search(SearchRequest searchRequest, Pageable pageable);

}
