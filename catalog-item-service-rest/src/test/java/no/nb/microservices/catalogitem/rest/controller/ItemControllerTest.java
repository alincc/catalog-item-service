package no.nb.microservices.catalogitem.rest.controller;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.item.model.Item.ItemBuilder;
import no.nb.microservices.catalogitem.core.item.service.ItemService;
import no.nb.microservices.catalogmetadata.model.fields.TestFields;
import no.nb.microservices.catalogmetadata.test.mods.v3.TestMods;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {
    
    @InjectMocks
    private ItemController controller;
    @Mock
    ItemService itemService;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void test() throws Exception {
        Item item = new ItemBuilder("id1")
                .mods(TestMods.aDefaultBookMods().build())
                .fields(TestFields.aDefaultBookFields().build())
                .hasAccess(true)
                .build();

        when(itemService.getItemById("id1")).thenReturn(item);

        mockMvc.perform(get("/catalog/items/id1"))
            .andExpect(status().is2xxSuccessful());

    }

}
