package no.nb.microservices.catalogitem.core.index.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nb.microservices.catalogitem.core.index.model.SearchResult;
import no.nb.microservices.catalogitem.core.index.repository.IndexRepository;
import no.nb.microservices.catalogitem.core.item.service.SecurityInfo;
import no.nb.microservices.catalogsearchindex.ItemResource;
import no.nb.microservices.catalogsearchindex.SearchResource;

@Service
public class IndexServiceImpl implements IndexService {

    private final IndexRepository indexRepository;

    @Autowired
    public IndexServiceImpl(IndexRepository indexRepository) {
        super();
        this.indexRepository = indexRepository;
    }

    @Override
    public SearchResult search(String q, SecurityInfo securityInfo) {

        SearchResource result = indexRepository.search(q,null,0,999, new ArrayList(), null,
                securityInfo.getxHost(), securityInfo.getxPort(), securityInfo.getxRealIp(), securityInfo.getSsoToken());

        List<String> ids = new ArrayList<>();
        for (ItemResource item : result.getEmbedded().getItems()) {
            ids.add(item.getItemId());
        }
        return new SearchResult(ids, result.getMetadata().getTotalElements(), result.getEmbedded().getAggregations());
    }

}
