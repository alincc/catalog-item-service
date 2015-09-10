package no.nb.microservices.catalogitem.core.item.model;

import static org.junit.Assert.*;

import org.junit.Test;

import no.nb.microservices.catalogmetadata.model.fields.FieldResource;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

public class ItemTest {

    @Test
    public void testBuild() {
        Mods mods = new Mods();
        FieldResource fields = new FieldResource();
        Item item = new Item.ItemBuilder("id1")
            .mods(mods)
            .fields(fields)
            .hasAccess(true)
            .build();
        
        assertNotNull("Item should not be null", item);
        assertNotNull("Mods should not be null", item.getMods());
        assertNotNull("Field should not be null", item.getField());
        assertNotNull("Access should be true", item.hasAccess());
        assertEquals("Id should be \"id1\"", "id1", item.getId());
    }

}
