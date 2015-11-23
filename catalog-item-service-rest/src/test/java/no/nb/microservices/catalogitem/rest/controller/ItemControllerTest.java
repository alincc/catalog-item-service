package no.nb.microservices.catalogitem.rest.controller;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.item.model.Item.ItemBuilder;
import no.nb.microservices.catalogitem.core.item.service.ItemService;
import no.nb.microservices.catalogitem.core.search.model.SearchAggregated;
import no.nb.microservices.catalogitem.core.search.model.SearchRequest;
import no.nb.microservices.catalogitem.core.search.service.ISearchService;
import no.nb.microservices.catalogitem.rest.model.ItemSearchResource;
import no.nb.microservices.catalogmetadata.test.model.fields.TestFields;
import no.nb.microservices.catalogmetadata.test.mods.v3.TestMods;
import no.nb.microservices.catalogsearchindex.AggregationResource;
import no.nb.microservices.catalogsearchindex.NBSearchType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
    }

    @Test
    public void testItem() throws Exception {
        Item item = new ItemBuilder("id1")
                .mods(TestMods.aDefaultBookMods().build())
                .fields(TestFields.aDefaultBook().build())
                .hasAccess(true)
                .build();

        when(itemService.getItemById("id1", null)).thenReturn(item);

        mockMvc.perform(get("/catalog/items/id1"))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testItemExpandRelatedItems() throws Exception {
        Item item = new ItemBuilder("id1")
                .mods(TestMods.aDefaultMusicAlbum().build())
                .fields(TestFields.aDefaultMusic().build())
                .hasAccess(true)
                .build();

        when(itemService.getItemById("id1", null)).thenReturn(item);

        mockMvc.perform(get("/catalog/items/id1"))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testRelatedItems() throws Exception {
        Item item = new ItemBuilder("id1")
                .mods(TestMods.aDefaultMusicAlbum().build())
                .fields(TestFields.aDefaultMusic().build())
                .hasAccess(true)
                .build();

        when(itemService.getItemById("id1", "relatedItems")).thenReturn(item);

        mockMvc.perform(get("/catalog/items/id1/relatedItems"))
            .andExpect(status().is2xxSuccessful());
    }
}
