package no.nb.microservices.catalogitem.core.item.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogsearchindex.ItemResource;

public class ItemTest {

    @Test
    public void testBuild() {
        Mods mods = new Mods();
        ItemResource itemResource = new ItemResource();
        Item item = new Item.ItemBuilder("id1")
            .mods(mods)
            .hasAccess(true)
            .withItemResource(itemResource)
            .build();
        
        assertNotNull("Item should not be null", item);
        assertNotNull("Mods should not be null", item.getMods());
        assertNotNull("Access should be true", item.hasAccess());
        assertNotNull("SearchResource should not be null", item.getItemResource());
        assertEquals("Id should be \"id1\"", "id1", item.getId());
    }

}
