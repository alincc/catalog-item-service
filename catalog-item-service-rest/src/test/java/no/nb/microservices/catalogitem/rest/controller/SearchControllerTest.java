package no.nb.microservices.catalogitem.rest.controller;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.item.service.ItemService;
import no.nb.microservices.catalogitem.core.search.model.SearchAggregated;
import no.nb.microservices.catalogitem.core.search.model.SearchRequest;
import no.nb.microservices.catalogitem.core.search.model.SuperSearchAggregated;
import no.nb.microservices.catalogitem.core.search.service.ISearchService;
import no.nb.microservices.catalogitem.rest.model.ItemSearchResource;
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
    public void whenSearchThenReturnItems() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQ("Supersonic");

        PageRequest pageable = new PageRequest(0, 10);

        List<Item> items = Arrays.asList(new Item.ItemBuilder("123").build(), new Item.ItemBuilder("456").build());

        SearchAggregated searchResult = new SearchAggregated(new PageImpl<>(items, pageable, 100), null, null, searchRequest);
        when(searchService.search(any(SearchRequest.class), any(Pageable.class))).thenReturn(searchResult);

        ResponseEntity<ItemSearchResource> result = searchController.search(searchRequest.getQ(), null, null, null, null, null, null,
                null, null, false, false, null, null, null, pageable);

        assertNotNull("Search result should not be null", result);
        assertTrue("Status code should be successful", result.getStatusCode().is2xxSuccessful());
        assertEquals("It should have two items", 2, result.getBody().getEmbedded().getItems().size());
    }

    @Test
    public void whenSearchWithAggregationThenReturnListOfAggregations() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQ("TestSearch");
        searchRequest.setAggs("ddc1, mediatype");

        PageRequest pageable = new PageRequest(0, 10);

        List<Item> items = Arrays.asList(new Item.ItemBuilder("123").build());

        List<AggregationResource> aggregations = new ArrayList<>();
        aggregations.add(new AggregationResource("ddc1"));
        aggregations.add(new AggregationResource("mediatype"));

        SearchAggregated searchResult = new SearchAggregated(new PageImpl<>(items, pageable, 100), aggregations, null, searchRequest);

        when(searchService.search(any(SearchRequest.class), any(Pageable.class))).thenReturn(searchResult);
        
        ResponseEntity<ItemSearchResource> result = searchController.search(searchRequest.getQ(), searchRequest.getAggs(), null, null, null, null, null,
                null, null, false, false, null, null, null, pageable);

        assertEquals(2, result.getBody().getEmbedded().getAggregations().size());
    }

    @Test
    public void whenSearchInFreeText() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQ("TestSearch");
        searchRequest.setSearchType(NBSearchType.TEXT_SEARCH);

        PageRequest pageable = new PageRequest(0, 10);

        List<Item> items = Arrays.asList(new Item.ItemBuilder("123").build());

        SearchAggregated searchResult = new SearchAggregated(new PageImpl<>(items, pageable, 100), null, null, searchRequest);
        when(searchService.search(any(SearchRequest.class), any(Pageable.class))).thenReturn(searchResult);
        ResponseEntity<ItemSearchResource> result = searchController.search(searchRequest.getQ(), null, searchRequest.getSearchType(), null, null, null, null,
                null, null, false, false, null, null, null, pageable);

        assertEquals(1, result.getBody().getEmbedded().getItems().size());
    }

    @Test
    public void whenSuperSearchAndOnlyHitOnBooksThenReturnSuperItemSearchResourceWithOnlyBooks() throws Exception {
        SearchRequest searchRequest = new SearchRequest();
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

        Map<String, SearchAggregated> searchAggregateds = new HashMap<>();
        searchAggregateds.put("bøker", searchAggregated);
        return new SuperSearchAggregated(searchAggregateds);
    }

    @Test
    public void testBoosting() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQ("boost me");
        searchRequest.setBoost(new String[]{"title,10", "name,4"});
        PageRequest pageable = new PageRequest(0, 10);
        List<Item> items = Arrays.asList(new Item.ItemBuilder("123").build());
        SearchAggregated searchResult = new SearchAggregated(new PageImpl<>(items, pageable, 10), null, null, searchRequest);
        when(searchService.search(any(SearchRequest.class), any(Pageable.class))).thenReturn(searchResult);

        searchController.search(searchRequest.getQ(), null, null, null, new String[]{"title,10", "name,4"}, null, null,
                null, null, false, false, null, null, null, pageable);

        verify(searchService, times(1)).search(any(SearchRequest.class), any(Pageable.class));
    }
}
