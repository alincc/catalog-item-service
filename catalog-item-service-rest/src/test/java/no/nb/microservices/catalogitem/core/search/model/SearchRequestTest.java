package no.nb.microservices.catalogitem.core.search.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SearchRequestTest {
    @Test
    public void testRemoveEncoding() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setSort(new String[]{"title%2Cdesc"});
        assertEquals("title,desc", searchRequest.getSort().get(0));
    }
}
