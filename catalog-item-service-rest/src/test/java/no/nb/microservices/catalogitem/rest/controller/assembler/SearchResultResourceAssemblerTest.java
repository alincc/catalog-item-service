package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.search.model.SearchAggregated;
import no.nb.microservices.catalogitem.core.search.model.SearchRequest;
import no.nb.microservices.catalogitem.core.search.model.SearchRequestBuilder;
import no.nb.microservices.catalogitem.rest.controller.SearchResultResourceAssembler;
import no.nb.microservices.catalogitem.rest.model.ItemSearchResource;
import no.nb.microservices.catalogitem.rest.model.ContentSearch;
import no.nb.microservices.catalogsearchindex.AggregationResource;
import no.nb.microservices.catalogsearchindex.FacetValueResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.Link;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

public class SearchResultResourceAssemblerTest {

    private SearchRequest searchRequest;

    @Before
    public void init() {

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/catalog/v1/search?q=Junit");
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);

        RequestContextHolder.setRequestAttributes(attributes);

        searchRequest = new SearchRequestBuilder().withQ("q").build();
    }

    @After
    public void cleanUp() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void whenOnAnyPageReturnValueShouldHaveASelfLinkElement() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();

        Page<Item> page = new PageImpl<>(new ArrayList<>(), new PageRequest(0, 10) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page, null, null, searchRequest);

        ItemSearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
        assertEquals("Should have a self-referential link element", "self", searchResultResource.getId().getRel());
    }

    @Test
    public void whenOnFirstPageThenReturnValueShouldNotHaveAPreviousLinkElement() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();

        Page<Item> page = new PageImpl<>(new ArrayList<>(), new PageRequest(0, 10) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page, null, null, searchRequest);

        ItemSearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
        assertNull(searchResultResource.getLink(Link.REL_PREVIOUS));
    }

    @Test
    public void whenOnLastPageThenReturnValueShouldNotHaveANextLinkElement() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();

        Page<Item> page = new PageImpl<>(new ArrayList<>(), new PageRequest(100, 10) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page, null, null, searchRequest);

        ItemSearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
        assertNull(searchResultResource.getLink(Link.REL_NEXT));
    }

    @Test
    public void whenOnFirstPageAndSizeIsSmallerThanSizeThenReturnValueShouldHaveANextLinkElement() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();

        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item.ItemBuilder("id1").build());
        items.add(new Item.ItemBuilder("id2").build());
        items.add(new Item.ItemBuilder("id3").build());

        Page<Item> page = new PageImpl<>(items, new PageRequest(0, 2) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page, null, null, searchRequest);

        ItemSearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
        assertNotNull(searchResultResource.getLink(Link.REL_NEXT));
    }

    @Test
    public void whenNotOnFirstPageThenReturnValueShouldHaveAFirstLinkElement() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();

        Page<Item> page = new PageImpl<>(new ArrayList<>(), new PageRequest(2, 10) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page, null, null, searchRequest);

        ItemSearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
        assertNotNull(searchResultResource.getLink(Link.REL_FIRST));
    }

    @Test
    public void whenNotOnLastPageThenReturnValueShouldHaveALastLinkElement() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();

        Page<Item> page = new PageImpl<>(new ArrayList<>(), new PageRequest(10, 10) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page, null, null, searchRequest);

        ItemSearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
        assertNotNull(searchResultResource.getLink(Link.REL_LAST));
    }

    @Test
    public void whenItNotOnLastPageThenReturnValueShouldHaveALastLinkElement() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();

        Page<Item> page = new PageImpl<>(new ArrayList<>(), new PageRequest(10, 10) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page, null, null, searchRequest);

        ItemSearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
        assertNotNull(searchResultResource.getLink(Link.REL_LAST));
    }

    @Test
    public void whenSearchResultHasItemsThenReturnValueShouldHaveItemsElement() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();

        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item.ItemBuilder("id1").build());
        items.add(new Item.ItemBuilder("id2").build());

        Page<Item> page = new PageImpl<>(items, new PageRequest(0, 10) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page, null, null, searchRequest);

        ItemSearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
        assertEquals("Items should have 2 items", 2, searchResultResource.getEmbedded().getItems().size());
    }

    @Test
    public void whenSearchResultHasNoItemsThenReturnValueShouldHaveNoItems() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();

        Page<Item> page = new PageImpl<>(new ArrayList<>(), new PageRequest(0, 10) , 1000);
        SearchAggregated searchAggregated = new SearchAggregated(page, null, null, searchRequest);

        ItemSearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);
        assertEquals("Items should have 0 items", 0, searchResultResource.getEmbedded().getItems().size());
    }

    @Test
    public void whenSearchResultsHasAggregationsThenAggregationShouldHaveNameAndFacetValues() {
        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();
        Page<Item> page = new PageImpl<>(new ArrayList<>(), new PageRequest(0, 10) , 1000);

        List<AggregationResource> aggregations = new ArrayList<>();

        AggregationResource a1 = new AggregationResource("ddc1");
        List<FacetValueResource> facetValues1 = new ArrayList<>();
        facetValues1.add(new FacetValueResource("01", 5));
        a1.setFacetValues(facetValues1);

        AggregationResource a2 = new AggregationResource("mediatype");
        List<FacetValueResource> facetValues2 = new ArrayList<>();
        facetValues2.add(new FacetValueResource("Bøker", 10));
        a2.setFacetValues(facetValues2);

        aggregations.add(a1);
        aggregations.add(a2);

        SearchAggregated searchAggregated = new SearchAggregated(page, aggregations, null, searchRequest);

        ItemSearchResource searchResultResource = searchResultResourceAssembler.toResource(searchAggregated);

        assertEquals("Should have 2 aggregations", 2, searchResultResource.getEmbedded().getAggregations().size());
    }

    @Test
    public void whenSearchAggregatedHasContentSearchesThenItShouldBeIncludedInResource() throws Exception {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQ("b");
        Page<Item> page = new PageImpl<>(Arrays.asList(new Item.ItemBuilder("id1").build()), new PageRequest(0, 1), 1);
        SearchAggregated searchAggregated = new SearchAggregated(page, Collections.emptyList(), null, searchRequest);
        List<ContentSearch> contentSearches = Arrays.asList(new ContentSearch("id1", "a <em>b</em> c "));
        searchAggregated.setContentSearches(contentSearches);

        SearchResultResourceAssembler searchResultResourceAssembler = new SearchResultResourceAssembler();
        ItemSearchResource itemSearchResource = searchResultResourceAssembler.toResource(searchAggregated);

        assertThat(itemSearchResource.getEmbedded().getContentSearch(), hasSize(1));
    }
}
