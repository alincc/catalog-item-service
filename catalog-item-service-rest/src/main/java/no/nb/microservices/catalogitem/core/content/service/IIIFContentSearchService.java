package no.nb.microservices.catalogitem.core.content.service;

import no.nb.microservices.catalogcontentsearch.rest.model.AnnotationList;
import no.nb.microservices.catalogcontentsearch.rest.model.Hit;
import no.nb.microservices.catalogitem.core.content.repository.ContentRepository;
import no.nb.microservices.catalogitem.core.item.service.TracableId;
import no.nb.microservices.catalogitem.rest.model.ContentSearch;
import org.apache.htrace.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class IIIFContentSearchService implements ContentSearchService {

    private final ContentRepository contentRepository;

    @Autowired
    public IIIFContentSearchService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    @Override
    @Async
    public Future<ContentSearch> search(String queryString, TracableId tracableId) {
        Trace.continueSpan(tracableId.getSpan());
        AnnotationList annotationList = contentRepository.search(tracableId.getId(),
                queryString,
                tracableId.getSecurityInfo().getxHost(),
                tracableId.getSecurityInfo().getxPort(),
                tracableId.getSecurityInfo().getxRealIp(),
                tracableId.getSecurityInfo().getSsoToken());

        if(annotationList.getHits() != null && !annotationList.getHits().isEmpty()) {
            Hit hit = annotationList.getHits().get(0);
            String text = new StringBuilder()
                    .append(hit.getBefore())
                    .append("<em>")
                    .append(queryString)
                    .append("</em>")
                    .append(" ")
                    .append(hit.getAfter())
                    .toString();
            return new AsyncResult<>(new ContentSearch(tracableId.getId(), text));
        }
        return null;
    }
}
