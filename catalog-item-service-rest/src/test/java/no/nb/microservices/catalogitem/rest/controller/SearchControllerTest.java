package no.nb.microservices.catalogitem.rest.controller;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.search.model.SearchAggregated;
import no.nb.microservices.catalogitem.core.search.model.SearchRequest;
import no.nb.microservices.catalogitem.core.search.model.SuperSearchAggregated;
import no.nb.microservices.catalogitem.core.search.model.SuperSearchRequest;
import no.nb.microservices.catalogitem.core.search.service.ISearchService;
import no.nb.microservices.catalogitem.rest.model.ItemSearchResource;
import no.nb.microservices.catalogitem.rest.model.ContentSearch;
import no.nb.microservices.catalogitem.rest.model.SuperItemSearchResource;
import no.nb.microservices.catalogsearchindex.AggregationResource;
import no.nb.microservices.catalogsearchindex.NBSearchType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchControllerTest {

    @Mock
    private ISearchService searchService;
    private SearchController searchController;

    @Before
    public void init() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/catalog/v1/search?q=Junit");
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);

        RequestContextHolder.setRequestAttributes(attributes);
    }

    @After
    public void cleanUp() {
        RequestContextHolder.resetRequestAttributes();
    }


    @Before
    public void setup() {
        searchController = new SearchController(searchService);
    }

    @Test
    public void whenSuperSearchAndOnlyHitOnBooksThenReturnSuperItemSearchResourceWithOnlyBooks() throws Exception {
        SuperSearchRequest searchRequest = new SuperSearchRequest();
        searchRequest.setQ("q");
        PageRequest pageRequest = new PageRequest(0, 5);
        when(searchService.superSearch(searchRequest, pageRequest)).thenReturn(getSuperSearchAggregated(searchRequest));

        ResponseEntity<SuperItemSearchResource> entity = searchController.superSearch(searchRequest, pageRequest);

        assertThat(entity.getBody().getEmbedded().getBooks().getEmbedded().getItems(), hasSize(5));
    }

    private SuperSearchAggregated getSuperSearchAggregated(SearchRequest searchRequest) {
        Page<Item> pageBooks = new PageImpl<>(Arrays.asList(
                new Item.ItemBuilder("id1").build(),
                new Item.ItemBuilder("id2").build(),
                new Item.ItemBuilder("id3").build(),
                new Item.ItemBuilder("id4").build(),
                new Item.ItemBuilder("id5").build()), new PageRequest(0, 5), 18);

        SearchAggregated searchAggregated = new SearchAggregated(pageBooks, Collections.emptyList(), null, searchRequest);

        List<ContentSearch> contentSearches = new ArrayList<>();
        contentSearches.add(new ContentSearch("id1", "Det var fint vært i <em>London</em> i 1994"));
        contentSearches.add(new ContentSearch("id2", "Det var fint vært i <em>London</em> i 1995"));
        contentSearches.add(new ContentSearch("id3", "Det var fint vært i <em>London</em> i 1996"));
        contentSearches.add(new ContentSearch("id4", "Det var fint vært i <em>London</em> i 1997"));
        contentSearches.add(new ContentSearch("id5", "Det var fint vært i <em>London</em> i 1998"));
        searchAggregated.setContentSearches(contentSearches);

        Map<String, SearchAggregated> searchAggregateds = new HashMap<>();
        searchAggregateds.put("bøker", searchAggregated);
        return new SuperSearchAggregated(searchAggregateds);
    }

    @Test
    public void whenSuperSearchThenReturnTextAroundSearchString() throws Exception {
        SuperSearchRequest searchRequest = new SuperSearchRequest();
        searchRequest.setQ("London");

        PageRequest pageRequest = new PageRequest(0, 5);

        when(searchService.superSearch(searchRequest, pageRequest)).thenReturn(getSuperSearchAggregated(searchRequest));

        ResponseEntity<SuperItemSearchResource> entity = searchController.superSearch(searchRequest, pageRequest);

        assertThat(entity.getBody().getEmbedded().getBooks().getEmbedded().getContentSearch(), hasSize(5));
    }
}
