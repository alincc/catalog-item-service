package no.nb.microservices.catalogitem.rest.controller.assembler;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.item.model.Item.ItemBuilder;
import no.nb.microservices.catalogitem.core.item.model.RelatedItems;
import no.nb.microservices.catalogitem.rest.model.RelatedItem;
import no.nb.microservices.catalogmetadata.test.model.fields.TestFields;
import no.nb.microservices.catalogmetadata.test.mods.v3.TestMods;

public class RelatedItemsBuilderTest {

    @Before
    public void init() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/v1/id1");
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @After
    public void cleanUp() {
        RequestContextHolder.resetRequestAttributes();
    }
    
    @Test
    public void testConsititutens() {
        Item constituten = new ItemBuilder("id")
                .mods(TestMods.aDefaultMusicAlbum().build())
                .fields(TestFields.aDefaultMusic().build())
                .hasAccess(true)
                .build();
        List<Item> constitutens = Arrays.asList(constituten);
        RelatedItems items = new RelatedItems(constitutens, null);
        
        RelatedItem relatedItems = new RelatedItemsBuilder()
            .withRelatedItems(items)
            .build();

        assertNotNull("RelatedItems should have consititutens", relatedItems.getConstituents());
    }

    @Test
    public void testHosts() {
        Item host = new ItemBuilder("id")
                .mods(TestMods.aDefaultMusicTrack().build())
                .fields(TestFields.aDefaultMusic().build())
                .hasAccess(true)
                .build();
        List<Item> hosts = Arrays.asList(host);
        RelatedItems items = new RelatedItems(hosts, null);
        
        RelatedItem relatedItems = new RelatedItemsBuilder()
            .withRelatedItems(items)
            .build();

        assertNotNull("RelatedItems should have consititutens", relatedItems.getConstituents());
    }

    @Test
    public void testPreceding() {
        Item item = new ItemBuilder("id")
                .mods(TestMods.aDefaultMusicAlbum().build())
                .fields(TestFields.aDefaultMusic().build())
                .hasAccess(true)
                .build();
        RelatedItems relatedItems = new RelatedItems(null, null, item, null);

        RelatedItem relatedItem = new RelatedItemsBuilder()
                .withRelatedItems(relatedItems)
                .build();

        assertNotNull("RelatedItems should have preceding", relatedItem.getPreceding());
    }

    @Test
    public void testSucceeding() {
        Item item = new ItemBuilder("id")
                .mods(TestMods.aDefaultMusicAlbum().build())
                .fields(TestFields.aDefaultMusic().build())
                .hasAccess(true)
                .build();
        RelatedItems relatedItems = new RelatedItems(null, null, null, item);

        RelatedItem relatedItem = new RelatedItemsBuilder()
                .withRelatedItems(relatedItems)
                .build();

        assertNotNull("RelatedItems should have succeeding", relatedItem.getSucceding());;
    }
    
}
