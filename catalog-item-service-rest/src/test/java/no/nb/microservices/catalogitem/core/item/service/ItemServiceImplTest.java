package no.nb.microservices.catalogitem.core.item.service;


import no.nb.commons.web.util.UserUtils;
import no.nb.microservices.catalogitem.core.index.model.SearchResult;
import no.nb.microservices.catalogitem.core.index.service.IndexService;
import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.metadata.service.MetadataService;
import no.nb.microservices.catalogitem.core.security.service.SecurityService;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.test.mods.v3.TestMods;
import no.nb.microservices.catalogsearchindex.EmbeddedWrapper;
import no.nb.microservices.catalogsearchindex.ItemResource;
import no.nb.microservices.catalogsearchindex.SearchResource;
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

import java.util.Arrays;
import java.util.concurrent.Future;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ItemServiceImplTest {

    @Mock
    MetadataService metadataService;
    @Mock
    SecurityService securityService;
    @Mock
    IndexService indexService;
    @InjectMocks
    private ItemServiceImpl itemService;

    @Before
    public void setup() {
        mockRequest();
    }

    @Test
    public void testGetItem() {
        String id = "id1";
        Future<Mods> mods = new AsyncResult<>(TestMods.aDefaultBookMods().build());
        Future<Boolean> hasAccess = new AsyncResult<>(true);
        when(metadataService.getModsById(anyObject())).thenReturn(mods);
        when(securityService.hasAccess(anyObject())).thenReturn(hasAccess);
        
        Item item = itemService.getItemById(id, null, "metadata");
        
        assertNotNull("Item should not be null", item);
        assertNotNull("Item should have mods", item.getMods());
        assertNotNull("Item should have access", item.hasAccess());
    }

    @Test
    public void testExpandRelatedItems() {
        String id = "id1";
        ItemResource resource = new ItemResource();
        SearchResult searchResult = new SearchResult(Arrays.asList(resource), 1, null, null);
        Future<Mods> mods = new AsyncResult<>(TestMods.aDefaultMusicAlbum().build());
        Future<Boolean> hasAccess = new AsyncResult<>(true);
        Future<SearchResource> searchResource = new AsyncResult<>(new SearchResource(new PagedResources.PageMetadata(1, 1, 1, 1), new EmbeddedWrapper()));
        
        when(metadataService.getModsById(anyObject())).thenReturn(mods);
        when(securityService.hasAccess(anyObject())).thenReturn(hasAccess);
        when(indexService.getSearchResource(anyObject())).thenReturn(searchResource);
        when(indexService.search(anyObject(), anyObject(), anyObject())).thenReturn(searchResult);

        Item item = itemService.getItemById(id, null, "relatedItems");
        
        assertNotNull("Item should have consitutents in relatedItems", item.getRelatedItems().getConstituents());
        assertNotNull("Item should have preceding in relatedItems", item.getRelatedItems().getPreceding());
        assertNotNull("Item should have succeeding in relatedItems", item.getRelatedItems().getSucceding());
    }

    @Test
    public void whenSearchReturnItemsFromOutsideOfNbThenGetModsToGetThumbnailUrl() throws Exception {
        ItemResource resource = new ItemResource();
        resource.setContentClasses(Arrays.asList("public", "jpeg"));
        resource.setMediaTypes(Arrays.asList("bilder"));
        Mods mods = TestMods.aDefaultMods().build();
        when(metadataService.getModsById(anyObject())).thenReturn(new AsyncResult<>(mods));
        when(securityService.hasAccess(anyObject())).thenReturn(new AsyncResult<>(true));

        itemService.getItemWithResource(resource, null, null, null);

        verify(metadataService, times(1)).getModsById(anyObject());
        verifyNoMoreInteractions(metadataService);
    }

    @Test
    public void whenSearchReturnItemsFromInsideOfNbThenDoNotGetModsToGetThumbnailUrl() throws Exception {
        ItemResource resource = new ItemResource();
        resource.setContentClasses(Arrays.asList("public", "jp2"));
        resource.setMediaTypes(Arrays.asList("bilder"));
        when(securityService.hasAccess(anyObject())).thenReturn(new AsyncResult<>(true));

        itemService.getItemWithResource(resource, null, null, null);

        verifyNoMoreInteractions(metadataService);
    }

    private void mockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/catalog/v1/search?q=Junit");
        String ip = "123.45.123.123";
        request.addHeader(UserUtils.REAL_IP_HEADER, ip);
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
        RequestContextHolder.setRequestAttributes(attributes);
    }


}
