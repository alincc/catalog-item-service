package no.nb.microservices.catalogitem.core.item.model;

import static org.junit.Assert.*;

import org.junit.Test;

import no.nb.microservices.catalogmetadata.model.fields.FieldResource;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogsearchindex.SearchResource;

public class ItemTest {

    @Test
    public void testBuild() {
        Mods mods = new Mods();
        SearchResource searchResource = new SearchResource();
        Item item = new Item.ItemBuilder("id1")
            .mods(mods)
            .hasAccess(true)
            .withSearchResource(searchResource)
            .build();
        
        assertNotNull("Item should not be null", item);
        assertNotNull("Mods should not be null", item.getMods());
        assertNotNull("Access should be true", item.hasAccess());
        assertNotNull("SearchResource should not be null", item.getSearchResource());
        assertEquals("Id should be \"id1\"", "id1", item.getId());
    }

}
