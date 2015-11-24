package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.item.model.RelatedItems;
import no.nb.microservices.catalogitem.rest.model.RelatedItemResource;
import no.nb.microservices.catalogmetadata.test.mods.v3.TestMods;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RelatedItemsResourceAssemblerTest {

    private RelatedItemsResourceAssembler assembler;
    
    @Before
    public void init() {
        assembler = new RelatedItemsResourceAssembler();

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/v1/id1/relatedItems");
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @After
    public void cleanUp() {
        RequestContextHolder.resetRequestAttributes();
    }
    
    @Test
    public void test() {
        
        Item host = new Item.ItemBuilder("id1")
                .mods(TestMods.aDefaultMusicAlbum().build())
                .build();
        RelatedItems relatedItems = new RelatedItems(null, Arrays.asList(host));
        Item item = new Item.ItemBuilder("id1")
                .mods(TestMods.aDefaultMusicTrack().build())
                .withRelatedItems(relatedItems)
                .build();

        RelatedItemResource relatedItemsResource = assembler.toResource(item );
        
        assertThat(relatedItemsResource.getId().getHref(), is("http://localhost/v1/catalog/items/id1/relatedItems"));
        
    }

}
