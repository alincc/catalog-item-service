package no.nb.microservices.catalogitem.core.item.model;

import static org.junit.Assert.*;

import org.junit.Test;

import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

public class ItemTest {

    @Test
    public void testBuild() {
        Mods mods = new Mods();
        Item item = new Item.ItemBuilder("id1")
            .mods(mods)
            .build();
        
        assertNotNull("item should not be null", item);
        assertEquals("Id should be \"id1\"", "id1", item.getId());
    }

    @Test
    public void testClassificationBuilder() {
        Mods mods = new Mods();
        Item item = new Item.ItemBuilder("id1")
            .mods(mods)
            .build();
        
        assertNotNull("Item should have classification", item.getClassification());
        assertNotNull("classification should have ddc", item.getClassification().getDdc());
        assertNotNull("classification should have udc", item.getClassification().getUdc());
    }

}
