package no.nb.microservices.catalogitem.core.search.model;

import no.nb.microservices.catalogsearchindex.NBSearchType;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class SearchRequestTest {

    @Test
    public void testRemoveEncoding() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setSort(new String[]{"title%2Cdesc"});
        assertEquals("title,desc", searchRequest.getSort().get(0));
    }

    @Test
    public void whenCloningSearchRequestThenReturnNewInstance() throws Exception {
        SearchRequest searchRequest = createSearchRequest();

        SearchRequest clone = (SearchRequest)searchRequest.clone();

        assertThat(clone.getQ(), is(searchRequest.getQ()));
        assertThat(clone.isExplain(), is(searchRequest.isExplain()));
        assertThat(clone.getBoost(), is(searchRequest.getBoost()));
        assertThat(clone.getSearchType(), is(searchRequest.getSearchType()));
    }

    @Test
    public void whenCloningSearchRequestThenNewInstanceShouldNotEffectOriginal() throws Exception {
        SearchRequest searchRequest = createSearchRequest();

        SearchRequest clone = (SearchRequest)searchRequest.clone();
        clone.setQ("test");
        clone.setExplain(false);
        clone.setSearchType(NBSearchType.FIELD_RESTRICTED_SEARCH);
        clone.setBoost(new String[]{"title,5", "mediatype,2"});

        assertThat(clone.getQ(), is(not(searchRequest.getQ())));
        assertThat(clone.isExplain(), is(not(searchRequest.isExplain())));
        assertThat(clone.getBoost(), is(not(searchRequest.getBoost())));
        assertThat(clone.getSearchType(), is(not(searchRequest.getSearchType())));
    }

    private SearchRequest createSearchRequest() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQ("q");
        searchRequest.setExplain(true);
        searchRequest.setBoost(new String[]{"title,1","mediatype,2"});
        return searchRequest;
    }
}
