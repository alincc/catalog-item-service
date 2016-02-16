package no.nb.microservices.catalogitem.core.content.service;

import no.nb.microservices.catalogcontentsearch.rest.model.AnnotationList;
import no.nb.microservices.catalogcontentsearch.rest.model.Hit;
import no.nb.microservices.catalogitem.core.content.repository.ContentRepository;
import no.nb.microservices.catalogitem.core.item.service.SecurityInfo;
import no.nb.microservices.catalogitem.core.item.service.TracableId;
import no.nb.microservices.catalogitem.rest.model.ContentSearch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class IIIFContentSearchServiceTest {

    @Mock
    private ContentRepository contentRepository;

    @InjectMocks
    private IIIFContentSearchService contentSearchService;

    @Test
    public void whenContentSearchThenReturnContentSearchWithId() throws Exception {
        AnnotationList annotationList = createAnnotationList("id1", "text before ", "text after");
        when(contentRepository.search("id1", "queryString", null, null, null, null)).thenReturn(annotationList);

        Future<ContentSearch> futureSearch = contentSearchService.search("queryString", new TracableId(null, "id1", new SecurityInfo()));

        while(!futureSearch.isDone()) { Thread.sleep(1L); }
        ContentSearch contentSearch = futureSearch.get();
        assertThat(contentSearch.getId(), is("id1"));
        verify(contentRepository, times(1)).search("id1", "queryString", null, null, null, null);
    }

    @Test
    public void whenContentSearchThenReturnContentSearchWithTextAroundQueryString() throws Exception {
        AnnotationList annotationList = createAnnotationList("id1", "text before ", "text after");
        when(contentRepository.search("id1", "queryString", null, null, null, null)).thenReturn(annotationList);

        Future<ContentSearch> futureSearch = contentSearchService.search("queryString", new TracableId(null, "id1", new SecurityInfo()));

        while(!futureSearch.isDone()) { Thread.sleep(1L); }
        ContentSearch contentSearch = futureSearch.get();
        assertThat(contentSearch.getText(), is("text before <em>queryString</em> text after"));
        verify(contentRepository, times(1)).search("id1", "queryString", null, null, null, null);
    }

    private AnnotationList createAnnotationList(String id, String textBefore, String textAfter) {
        AnnotationList annotationList = new AnnotationList();
        List<Hit> hits = new ArrayList<>();
        Hit hit = new Hit();
        hit.setAnnotations(Arrays.asList(id));
        hit.setBefore(textBefore);
        hit.setAfter(textAfter);
        hits.add(hit);
        annotationList.setHits(hits);
        return annotationList;
    }
}
