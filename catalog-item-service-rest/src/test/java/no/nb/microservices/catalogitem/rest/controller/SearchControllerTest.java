package no.nb.microservices.catalogitem.rest.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.search.model.SearchAggregated;
import no.nb.microservices.catalogitem.core.search.model.SearchRequest;
import no.nb.microservices.catalogitem.core.search.service.ISearchService;
import no.nb.microservices.catalogitem.rest.model.ItemSearchResource;
import no.nb.microservices.catalogsearchindex.AggregationResource;
import no.nb.microservices.catalogsearchindex.NBSearchType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchControllerTest {

    @Mock
    private ISearchService searchService;

    private SearchController searchController;

    @Mock
    private SearchResultResourceAssembler searchResultResourceAssembler;

    @Before
    public void init() {

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/v1/search?q=Junit");
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

        SearchAggregated searchResult = new SearchAggregated(new PageImpl<>(items, pageable, 100), null);
        when(searchService.search(searchRequest, pageable)).thenReturn(searchResult);

        ResponseEntity<ItemSearchResource> result = searchController.search(searchRequest, pageable);

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

        SearchAggregated searchResult = new SearchAggregated(new PageImpl<>(items, pageable, 100), aggregations);

        when(searchService.search(searchRequest, pageable)).thenReturn(searchResult);
        ResponseEntity<ItemSearchResource> result = searchController.search(searchRequest, pageable);

        assertEquals(2, result.getBody().getEmbedded().getAggregations().size());
    }

    @Test
    public void whenSearchInFreeText() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQ("TestSearch");
        searchRequest.setSearchType(NBSearchType.TEXT_SEARCH);

        PageRequest pageable = new PageRequest(0, 10);

        List<Item> items = Arrays.asList(new Item.ItemBuilder("123").build());

        SearchAggregated searchResult = new SearchAggregated(new PageImpl<>(items, pageable, 100), null);
        when(searchService.search(searchRequest, pageable)).thenReturn(searchResult);
        ResponseEntity<ItemSearchResource> result = searchController.search(searchRequest, pageable);

        assertEquals(1, result.getBody().getEmbedded().getItems().size());
    }
}
