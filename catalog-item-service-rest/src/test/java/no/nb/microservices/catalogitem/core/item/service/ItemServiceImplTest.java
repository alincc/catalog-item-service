package no.nb.microservices.catalogitem.core.item.service;


import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.concurrent.Future;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.hateoas.PagedResources;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import no.nb.commons.web.util.UserUtils;
import no.nb.microservices.catalogitem.core.index.model.SearchResult;
import no.nb.microservices.catalogitem.core.index.service.IndexService;
import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.metadata.service.MetadataService;
import no.nb.microservices.catalogitem.core.security.service.SecurityService;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.test.mods.v3.TestMods;
import no.nb.microservices.catalogsearchindex.SearchResource;

@RunWith(MockitoJUnitRunner.class)
public class ItemServiceImplTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    MetadataService metadataService;
    
    @Mock
    SecurityService securityService;

    @Mock
    IndexService indexService;

    @Before
    public void setup() {
        mockRequest();
    }

    @Test
    public void testGetItem() {
        String id = "id1";
        Future<Mods> mods = new AsyncResult<Mods>(TestMods.aDefaultBookMods().build());
        Future<Boolean> hasAccess = new AsyncResult<Boolean>(true);
        when(metadataService.getModsById(anyObject())).thenReturn(mods);
        when(securityService.hasAccess(anyObject())).thenReturn(hasAccess);
        
        Item item = itemService.getItemById(id, null);
        
        assertNotNull("Item should not be null", item);
        assertNotNull("Item should have mods", item.getMods());
        assertNotNull("Item should have access", item.hasAccess());
    }

    @Test
    public void testExpandRelatedItems() {
        String id = "id1";
        SearchResult searchResult = new SearchResult(Arrays.asList("id1"), 1, null);
        Future<Mods> mods = new AsyncResult<Mods>(TestMods.aDefaultMusicAlbum().build());
        Future<Boolean> hasAccess = new AsyncResult<Boolean>(true);
        Future<SearchResource> searchResource = new AsyncResult<>(new SearchResource(new PagedResources.PageMetadata(1,1,1,1)));
        
        when(metadataService.getModsById(anyObject())).thenReturn(mods);
        when(securityService.hasAccess(anyObject())).thenReturn(hasAccess);
        when(indexService.getSearchResource(anyObject())).thenReturn(searchResource);
        when(indexService.search(anyObject(), anyObject(), anyObject())).thenReturn(searchResult);

        Item item = itemService.getItemById(id, "relatedItems");
        
        assertNotNull("Item should have consitutents in relatedItems", item.getRelatedItems().getConstituents());
        assertNotNull("Item should have preceding in relatedItems", item.getRelatedItems().getPreceding());
        assertNotNull("Item should have succeeding in relatedItems", item.getRelatedItems().getSucceding());
    }
    
    private void mockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/v1/search?q=Junit");
        String ip = "123.45.123.123";
        request.addHeader(UserUtils.REAL_IP_HEADER, ip);
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
        RequestContextHolder.setRequestAttributes(attributes);
    }


}
