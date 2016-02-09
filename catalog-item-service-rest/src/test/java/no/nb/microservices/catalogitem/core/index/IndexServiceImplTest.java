package no.nb.microservices.catalogitem.core.index;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;

import no.nb.microservices.catalogitem.core.index.repository.IndexRepository;
import no.nb.microservices.catalogitem.core.index.service.IndexService;
import no.nb.microservices.catalogitem.core.index.service.IndexServiceImpl;
import no.nb.microservices.catalogitem.core.item.service.SecurityInfo;
import no.nb.microservices.catalogitem.core.search.model.SearchRequest;
import no.nb.microservices.catalogsearchindex.EmbeddedWrapper;
import no.nb.microservices.catalogsearchindex.NBSearchType;
import no.nb.microservices.catalogsearchindex.SearchResource;

@RunWith(MockitoJUnitRunner.class)
public class IndexServiceImplTest {

    private IndexRepository mockIndexRepository;
    private IndexService indexService;

    @Before
    public void setup() {
        mockIndexRepository = mock(IndexRepository.class);
        indexService = new IndexServiceImpl(mockIndexRepository);
    }

    @Test
    public void searchInMetadata() {
        SearchResource searchResource = createSearchResource();

        when(mockIndexRepository.search(eq("searchString"), anyString(), anyInt(), anyInt(), anyList(), anyBoolean(), anyList(), anyList(), anyString(), eq(NBSearchType.FIELD_RESTRICTED_SEARCH),
                anyObject(), anyObject(), anyObject(),
                anyBoolean(), anyString(), anyString(), anyString(), anyString())).thenReturn(searchResource);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQ("searchString");
        searchRequest.setSearchType(NBSearchType.FIELD_RESTRICTED_SEARCH);

        Pageable pageable = new PageRequest(0, 10);

        SecurityInfo securityInfo = createSecurityInfo();
        indexService.search(searchRequest, pageable, securityInfo);

        verify(mockIndexRepository).search(eq("searchString"), anyString(), anyInt(), anyInt(), anyList(), anyBoolean(), anyList(), anyList(), anyString(), eq(NBSearchType.FIELD_RESTRICTED_SEARCH),
                anyObject(), anyObject(), anyObject(),
                anyBoolean(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void geoSearch() {
        SearchResource searchResource = createSearchResource();

        when(mockIndexRepository.search(eq("searchString"),
                anyString(), anyInt(), anyInt(), anyList(), anyBoolean(), anyList(), anyList(), anyString(), anyObject(),
                eq("82.16,74.35"), eq("34.54,-31.46"), eq("5"),
                anyBoolean(), anyString(), anyString(), anyString(), anyString())).thenReturn(searchResource);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQ("searchString");
        searchRequest.setTopRight("82.16,74.35");
        searchRequest.setBottomLeft("34.54,-31.46");
        searchRequest.setPrecision("5");
        Pageable pageable = new PageRequest(0, 10);
        SecurityInfo securityInfo = createSecurityInfo();

        indexService.search(searchRequest, pageable, securityInfo);

        verify(mockIndexRepository).search(eq("searchString"),
                anyString(), anyInt(), anyInt(), anyList(), anyBoolean(), anyList(), anyList(), anyString(), anyObject(),
                eq("82.16,74.35"), eq("34.54,-31.46"), eq("5"),
                anyBoolean(), anyString(), anyString(), anyString(), anyString());
    }

    private SearchResource createSearchResource() {
        PagedResources.PageMetadata metadata = new PagedResources.PageMetadata(0, 10, 100);
        SearchResource searchResource = new SearchResource(metadata);
        EmbeddedWrapper wrapper = new EmbeddedWrapper();
        wrapper.setItems(new ArrayList<>());
        searchResource.setEmbedded(wrapper);
        return searchResource;
    }

    private SecurityInfo createSecurityInfo() {
        SecurityInfo securityInfo = new SecurityInfo();
        securityInfo.setSsoToken("");
        securityInfo.setxHost("");
        securityInfo.setxPort("");
        securityInfo.setxRealIp("");
        return securityInfo;
    }
}
