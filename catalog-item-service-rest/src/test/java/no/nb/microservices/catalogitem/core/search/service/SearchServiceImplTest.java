package no.nb.microservices.catalogitem.core.search.service;

import no.nb.commons.web.util.UserUtils;
import no.nb.microservices.catalogitem.core.content.service.ContentSearchService;
import no.nb.microservices.catalogitem.core.index.model.SearchResult;
import no.nb.microservices.catalogitem.core.index.service.IndexService;
import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.item.service.ItemWrapperService;
import no.nb.microservices.catalogitem.core.item.service.SecurityInfo;
import no.nb.microservices.catalogitem.core.item.service.TracableId;
import no.nb.microservices.catalogitem.core.search.model.*;
import no.nb.microservices.catalogitem.rest.model.ContentSearch;
import no.nb.microservices.catalogsearchindex.AggregationResource;
import no.nb.microservices.catalogsearchindex.FacetValueResource;
import no.nb.microservices.catalogsearchindex.ItemResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SearchServiceImplTest {
    private SearchServiceImpl searchService;

    @Mock
    ContentSearchService contentSearchService;

    @Mock
    IndexService indexService;

    @Before
    public void setup() {
        searchService = new SearchServiceImpl(new ItemWrapperServiceStub(), indexService, contentSearchService);
        MockHttpServletRequest request = new MockHttpServletRequest("GET","/catalog/v1/search?q=Junit");

        String ip = "123.45.123.123";
        request.addHeader(UserUtils.REAL_IP_HEADER, ip);

        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @After
    public void cleanUp() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void whenSearchingAndIndexReturnResultsThenResultShouldContainItems() throws Exception {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQ("I love Træna");
        Pageable pageable = new PageRequest(0,10);
        SearchResult searchResult = new SearchResult(Arrays.asList(new ItemResource(), new ItemResource()), 100, null, null);
        when(indexService.search(anyObject(), anyObject(), anyObject())).thenReturn(searchResult);

        SearchAggregated result = searchService.search(searchRequest, pageable);
        assertNotNull("The result should not be null", result);
        assertEquals("The result size should be 2", 2, result.getPage().getContent().size());
    }

    @Test
    public void whenSuperSearchAndIndexReturnResultsThenResultShouldContainMultipleResultsWithMultipleItems() throws Exception {
        SuperSearchRequest searchRequest = new SuperSearchRequest();
        searchRequest.setQ("q");

        when(indexService.search(argThat(new IsSameSearchRequest(searchRequest)), any(), any())).thenReturn(createMediaTypeAggsSearchResult());
        Future<ContentSearch> futureContentSearch = new AsyncResult<>(new ContentSearch("123", "det var <em>q</em>"));
        when(contentSearchService.search(eq(searchRequest.getQ()), any(TracableId.class))).thenReturn(futureContentSearch);
        SearchResult searchResultBooks = new SearchResult(Arrays.asList(new ItemResource()), 40, Collections.emptyList(), null);
        when(indexService.search(argThat(new IsSameSearchRequest(createSearchRequest(new String[]{"mediatype:bøker"}))), any(), any())).thenReturn(searchResultBooks);
        when(indexService.search(argThat(new IsSameSearchRequest(createSearchRequest(new String[]{"mediatype:aviser"}))), any(), any())).thenReturn(searchResultBooks);

        SuperSearchAggregated superSearchAggregated = searchService.superSearch(searchRequest, new PageRequest(0, 1));

        assertThat(superSearchAggregated.getSearchAggregateds().keySet(), hasSize(2));
        assertThat(superSearchAggregated.getSearchAggregateds().get("bøker").getPage().getContent(), hasSize(1));
    }

    @Test
    public void whenSuperSearchThenReturnTextAroundSearchString() throws Exception {
        SuperSearchRequest searchRequest = new SuperSearchRequest();
        searchRequest.setQ("q");

        when(indexService.search(argThat(new IsSameSearchRequest(searchRequest)), any(), any())).thenReturn(createMediaTypeAggsSearchResult());
        Future<ContentSearch> futureContentSearch = new AsyncResult<>(new ContentSearch("123", "det var <em>q</em>"));
        when(contentSearchService.search(eq(searchRequest.getQ()), any(TracableId.class))).thenReturn(futureContentSearch);

        SearchResult searchResultBooks = new SearchResult(Arrays.asList(new ItemResource()), 1, Collections.emptyList(), null);
        when(indexService.search(argThat(new IsSameSearchRequest(createSearchRequest(new String[]{"mediatype:bøker"}))), any(), any())).thenReturn(searchResultBooks);
        when(indexService.search(argThat(new IsSameSearchRequest(createSearchRequest(new String[]{"mediatype:aviser"}))), any(), any())).thenReturn(searchResultBooks);

        SuperSearchAggregated superSearchAggregated = searchService.superSearch(searchRequest, new PageRequest(0, 5));

        assertThat(superSearchAggregated.getSearchAggregateds().keySet(), hasSize(2));
        assertThat(superSearchAggregated.getSearchAggregateds().get("aviser").getContentSearches(), hasSize(1));

        verify(indexService, times(3)).search(any(SearchRequest.class), any(Pageable.class), any(SecurityInfo.class));
        verify(contentSearchService, times(1)).search(eq(searchRequest.getQ()), any(TracableId.class));
    }

    private SearchResult createMediaTypeAggsSearchResult() {
        AggregationResource aggregationResource = new AggregationResource("mediatype");
        aggregationResource.setFacetValues(Arrays.asList(new FacetValueResource("bøker", 1), new FacetValueResource("aviser", 1)));
        List<AggregationResource> aggregations = Arrays.asList(aggregationResource);
        return new SearchResult(Collections.emptyList(), 1, aggregations, null);
    }

    private SearchRequest createSearchRequest(String[] filter) {
        SearchRequest searchRequestBooks = new SearchRequest();
        searchRequestBooks.setQ("q");
        searchRequestBooks.setFilter(filter);
        return searchRequestBooks;
    }
}

class ItemWrapperServiceStub implements ItemWrapperService {
    @Override
    public Future<Item> getById(ItemWrapper itemWrapper) {
        Item item = new Item.ItemBuilder("123").build();
        try {
            return new AsyncResult<>(item);
        } finally {
            itemWrapper.getLatch().countDown();
        }
    }
}

class IsSameSearchRequest extends ArgumentMatcher<SearchRequest> {

    private final SearchRequest searchRequest;

    IsSameSearchRequest(SearchRequest searchRequest) {
        this.searchRequest = searchRequest;
    }

    @Override
    public boolean matches(Object o) {
        if(o instanceof SearchRequest) {
            SearchRequest other = (SearchRequest)o;
            if(!isSameValue(searchRequest.getQ(), other.getQ())) {
                return false;
            }
            if(!isSameValue(searchRequest.getAggs(), other.getAggs())) {
                return false;
            }
            if (!hasSameFilters(other)) return false;

            return true;
        }
        return false;
    }

    private boolean hasSameFilters(SearchRequest other) {
        if(searchRequest.getFilter().size() != other.getFilter().size()) {
            return false;
        }

        for(int i = 0; i < searchRequest.getFilter().size(); i++) {
            if(!isSameValue(searchRequest.getFilter().get(i), other.getFilter().get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean isSameValue(String thisValue, String otherValue) {
        if(thisValue != null && otherValue != null) {
            if(thisValue.equalsIgnoreCase(otherValue)) {
                return true;
            }
        } else if (thisValue == null && otherValue == null) {
            return true;
        }
        return false;
    }
}