package no.nb.microservices.catalogitem.core.index;

import no.nb.microservices.catalogitem.core.index.repository.IndexRepository;
import no.nb.microservices.catalogitem.core.index.service.IndexService;
import no.nb.microservices.catalogitem.core.index.service.IndexServiceImpl;
import no.nb.microservices.catalogitem.core.item.service.SecurityInfo;
import no.nb.microservices.catalogitem.core.search.model.SearchRequest;
import no.nb.microservices.catalogsearchindex.EmbeddedWrapper;
import no.nb.microservices.catalogsearchindex.NBSearchType;
import no.nb.microservices.catalogsearchindex.SearchResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;

import java.util.ArrayList;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class IndexServiceImplTest {

    @Test
    public void searchInMetadata() {
        IndexRepository indexRepository = mock(IndexRepository.class);
        IndexService indexService = new IndexServiceImpl(indexRepository);

        PagedResources.PageMetadata metadata = new PagedResources.PageMetadata(0, 10, 100);
        SearchResource searchResource = new SearchResource(metadata);
        EmbeddedWrapper wrapper = new EmbeddedWrapper();
        wrapper.setItems(new ArrayList<>());
        searchResource.setEmbedded(wrapper);

        when(indexRepository.search(eq("searchString"), anyString(), anyInt(), anyInt(), anyList(), anyString(), eq(NBSearchType.FIELD_RESTRICTED_SEARCH), anyString(), anyString(), anyString(), anyString())).thenReturn(searchResource);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQ("searchString");
        searchRequest.setSearchType(NBSearchType.FIELD_RESTRICTED_SEARCH);

        Pageable pageable = new PageRequest(0, 10);

        SecurityInfo securityInfo = new SecurityInfo();
        securityInfo.setSsoToken("");
        securityInfo.setxHost("");
        securityInfo.setxPort("");
        securityInfo.setxRealIp("");
        indexService.search(searchRequest, pageable, securityInfo);

        verify(indexRepository).search(eq("searchString"), anyString(), anyInt(), anyInt(), anyList(), anyString(), eq(NBSearchType.FIELD_RESTRICTED_SEARCH), anyString(), anyString(), anyString(), anyString());
    }
}
