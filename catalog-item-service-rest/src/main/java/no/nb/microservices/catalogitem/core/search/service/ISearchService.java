package no.nb.microservices.catalogitem.core.search.service;

import no.nb.microservices.catalogitem.core.search.model.SearchAggregated;
import no.nb.microservices.catalogitem.core.search.model.SearchRequest;
import no.nb.microservices.catalogitem.core.search.model.SuperSearchAggregated;
import no.nb.microservices.catalogitem.core.search.model.SuperSearchRequest;
import org.springframework.data.domain.Pageable;

public interface ISearchService {

    SearchAggregated search(SearchRequest searchRequest, Pageable pageable);

    SuperSearchAggregated superSearch(SuperSearchRequest searchRequest, Pageable pageable);
}
