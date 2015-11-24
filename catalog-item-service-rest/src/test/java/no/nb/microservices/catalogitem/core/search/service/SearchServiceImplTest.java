package no.nb.microservices.catalogitem.core.search.service;

import no.nb.commons.web.util.UserUtils;
import no.nb.microservices.catalogitem.core.index.model.SearchResult;
import no.nb.microservices.catalogitem.core.index.service.IndexService;
import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.item.service.ItemWrapperService;
import no.nb.microservices.catalogitem.core.search.model.ItemWrapper;
import no.nb.microservices.catalogitem.core.search.model.SearchAggregated;
import no.nb.microservices.catalogitem.core.search.model.SearchRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchServiceImplTest {
    private SearchServiceImpl searchService;

    @Mock
    IndexService indexService;

    @Before
    public void setup() {
        searchService = new SearchServiceImpl(new ItemWrapperServiceStub(), indexService);
        MockHttpServletRequest request = new MockHttpServletRequest("GET","/v1/search?q=Junit");

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
        SearchResult searchResult = new SearchResult(Arrays.asList("1","2"), 100, null);
        when(indexService.search(anyObject(), anyObject(), anyObject())).thenReturn(searchResult);

        SearchAggregated result = searchService.search(searchRequest, pageable);
        assertNotNull("The result should not be null", result);
        assertEquals("The result size should be 2", 2, result.getPage().getContent().size());
    }

}

class ItemWrapperServiceStub implements ItemWrapperService {
    @Override
    public Future<Item> getById(ItemWrapper itemWrapper) {
        Item item = new Item.ItemBuilder("123").build();
        try {
            return new AsyncResult<Item>(item);
        } finally {
            itemWrapper.getLatch().countDown();
        }
    }
}

