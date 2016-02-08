package no.nb.microservices.catalogitem.core.index.service;

import no.nb.microservices.catalogitem.core.index.model.SearchResult;
import no.nb.microservices.catalogitem.core.index.repository.IndexRepository;
import no.nb.microservices.catalogitem.core.item.service.SecurityInfo;
import no.nb.microservices.catalogitem.core.item.service.TracableId;
import no.nb.microservices.catalogitem.core.search.model.SearchRequest;
import no.nb.microservices.catalogsearchindex.ItemResource;
import no.nb.microservices.catalogsearchindex.SearchResource;
import org.apache.htrace.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class IndexServiceImpl implements IndexService {

    private final IndexRepository indexRepository;

    @Autowired
    public IndexServiceImpl(IndexRepository indexRepository) {
        super();
        this.indexRepository = indexRepository;
    }

    @Override
    public SearchResult search(SearchRequest searchRequest, Pageable pageable, SecurityInfo securityInfo) {

        SearchResource result = indexRepository.search(
                searchRequest.getQ(),
                null,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                searchRequest.getSort(),
                searchRequest.getBoost(),
                searchRequest.getAggs(),
                searchRequest.getSearchType(),
                searchRequest.getTopRight(),
                searchRequest.getBottomLeft(),
                searchRequest.getPrecision(),
                searchRequest.isExplain(),
                securityInfo.getxHost(),
                securityInfo.getxPort(),
                securityInfo.getxRealIp(),
                securityInfo.getSsoToken());

        return new SearchResult(result.getEmbedded().getItems(), result.getMetadata().getTotalElements(), result.getEmbedded().getAggregations());
    }

    @Override
    public Future<SearchResource> getSearchResource(TracableId id) {
        Trace.continueSpan(id.getSpan());
        SecurityInfo securityInfo = id.getSecurityInfo();

        String query;
        if(id.getId().contains("URN:NBN")) {
            query = "urn:\"" + id.getId() + "\"";
        } else {
            query = "sesamid:" + id.getId();
        }

        SearchResource searchResource = indexRepository.search(query,null,0, 1, new ArrayList(),
                null, null, null, null, null, null,false, securityInfo.getxHost(), securityInfo.getxPort(), securityInfo.getxRealIp(), securityInfo.getSsoToken());

        return new AsyncResult<>(searchResource);

    }

}
