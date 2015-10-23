package no.nb.microservices.catalogitem.core.item.service;


import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import no.nb.commons.web.util.UserUtils;
import no.nb.microservices.catalogitem.core.index.model.SearchResult;
import no.nb.microservices.catalogitem.core.index.service.IndexService;
import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.metadata.repository.MetadataRepository;
import no.nb.microservices.catalogitem.core.security.repository.SecurityRepository;
import no.nb.microservices.catalogmetadata.model.fields.FieldResource;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.test.model.fields.TestFields;
import no.nb.microservices.catalogmetadata.test.mods.v3.TestMods;

@RunWith(MockitoJUnitRunner.class)
public class ItemServiceImplTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    MetadataRepository metadataRepository;
    
    @Mock
    SecurityRepository securityRepository;

    @Mock
    IndexService indexService;

    @Before
    public void setup() {
        mockRequest();
    }

    @Test
    public void testGetItem() {
        String id = "id1";
        Mods mods = TestMods.aDefaultBookMods().build();
        FieldResource fields = TestFields.aDefaultBook().build();
        when(metadataRepository.getModsById(eq(id), anyString(), anyString(), anyString(), anyString())).thenReturn(mods);
        when(metadataRepository.getFieldsById(eq(id), anyString(), anyString(), anyString(), anyString())).thenReturn(fields);
        when(securityRepository.hasAccess(eq(id), anyString(), anyString())).thenReturn(true);
        
        Item item = itemService.getItemById(id, null);
        
        assertNotNull("Item should not be null", item);
        assertNotNull("Item should have mods", item.getMods());
        assertNotNull("Item should have field", item.getField());
        assertNotNull("Item should have access", item.hasAccess());
    }

    @Test
    public void testExpandRelatedItems() {
        String id = "id1";
        Mods mods = TestMods.aDefaultMusicAlbum().build();
        FieldResource fields = TestFields.aDefaultMusic().build();
        SearchResult searchResult = new SearchResult(Arrays.asList("id1"), 1, null);
        when(metadataRepository.getModsById(eq(id), anyString(), anyString(), anyString(), anyString())).thenReturn(mods);
        when(metadataRepository.getFieldsById(eq(id), anyString(), anyString(), anyString(), anyString())).thenReturn(fields);
        when(securityRepository.hasAccess(eq(id), anyString(), anyString())).thenReturn(true);
        when(indexService.search(anyString(), anyObject())).thenReturn(searchResult);

        Item item = itemService.getItemById(id, "relatedItems");
        
        assertNotNull("Item should have consitutents in relatedItems", item.getRelatedItems().getConstituents());
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
