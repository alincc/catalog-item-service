package no.nb.microservices.catalogitem.rest.controller;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.item.model.Item.ItemBuilder;
import no.nb.microservices.catalogitem.core.item.service.ItemService;
import no.nb.microservices.catalogitem.core.search.service.ISearchService;
import no.nb.microservices.catalogmetadata.test.mods.v3.TestMods;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {
    
    @InjectMocks
    private ItemController controller;

    @Mock
    ItemService itemService;

    @Mock
    ISearchService searchService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/catalog/v1/items");
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
    }
    

    @Test
    public void testItem() throws Exception {
        Item item = new ItemBuilder("id1")
                .mods(TestMods.aDefaultBookMods().build())
                .hasAccess(true)
                .build();

        when(itemService.getItemById("id1", null, "metadata")).thenReturn(item);

        mockMvc.perform(get("/catalog/v1/items/id1"))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testItemExpandRelatedItems() throws Exception {
        Item item = new ItemBuilder("id1")
                .mods(TestMods.aDefaultMusicAlbum().build())
                .hasAccess(true)
                .build();

        when(itemService.getItemById("id1", null, "metadata")).thenReturn(item);

        mockMvc.perform(get("/catalog/v1/items/id1"))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testRelatedItems() throws Exception {
        Item item = new ItemBuilder("id1")
                .mods(TestMods.aDefaultMusicAlbum().build())
                .hasAccess(true)
                .build();

        when(itemService.getItemById("id1", null, "relatedItems")).thenReturn(item);

        mockMvc.perform(get("/catalog/v1/items/id1/relatedItems"))
            .andExpect(status().is2xxSuccessful());
    }
}
