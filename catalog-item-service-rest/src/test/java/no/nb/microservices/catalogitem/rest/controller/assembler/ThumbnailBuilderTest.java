package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogmetadata.model.fields.FieldResource;
import no.nb.microservices.catalogmetadata.model.mods.v3.Location;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Url;
import no.nb.microservices.catalogsearchindex.ItemResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.hateoas.Link;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ThumbnailBuilderTest {

    @Before
    public void init() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/catalog/v1/id1");
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @After
    public void cleanUp() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void buildAmundsenThumbnailsTest() {
        Url url = new Url();
        url.setValue("http://www.nb.no/baser/amundsen/bilder/JFIFSURA_P/301-350/SURA327.jpg");
        Location location = new Location();
        location.setUrls(Arrays.asList(url));
        Mods mods = new Mods();
        mods.setLocation(location);

        List<Link> links = new ThumbnailBuilder()
                .withMods(mods)
                .build();
        assertEquals(1, links.size());
        assertEquals("thumbnail_large", links.get(0).getRel());
        assertEquals("http://www.nb.no/baser/amundsen/bilder/JFIFSURA_P/301-350/SURA327.jpg", links.get(0).getHref());
    }

    @Test
    public void buildStatfjordThumbnailsTest() {
        Url url1 = new Url();
        url1.setValue("http://media31.dimu.no/media/image/NOM/NOMF-02772.109/0?byIndex=true&height=400&width=400");
        url1.setAccess("preview");
        Url url2 = new Url();
        url2.setValue("http://media31.dimu.no/media/image/NOM/NOMF-02772.109/0?byIndex=true&height=800&width=800");
        url2.setAccess("raw object");
        Url url3 = new Url();
        url3.setValue("http://digitaltmuseum.no/things/thing/NOM/NOMF-02772.109");
        url3.setAccess("object in context");
        Location location = new Location();
        location.setUrls(Arrays.asList(url1, url2, url3));
        Mods mods = new Mods();
        mods.setLocation(location);

        List<Link> links = new ThumbnailBuilder()
                .withMods(mods)
                .build();
        assertEquals(1, links.size());
        assertEquals("thumbnail_large", links.get(0).getRel());
        assertEquals("http://media31.dimu.no/media/image/NOM/NOMF-02772.109/0?byIndex=true&height=400&width=400", links.get(0).getHref());
    }

    @Test
    public void buildGalleriNorThumbnailsTest() {
        Url url1 = new Url();
        url1.setValue("http://www.nb.no/gallerinor/hent_bilde.php?id=51689&size=1");
        Url url2 = new Url();
        url2.setValue("http://www.nb.no/gallerinor/hent_bilde.php?id=51689&size=4");
        Url url3 = new Url();
        url3.setValue("http://www.nb.no/gallerinor/hent_bilde.php?id=51689&size=16");
        Location location = new Location();
        location.setUrls(Arrays.asList(url1, url2, url3));
        Mods mods = new Mods();
        mods.setLocation(location);
        Item item = new Item.ItemBuilder("id1").mods(mods).build();

        List<Link> links = new ThumbnailBuilder()
                .withMods(mods)
                .build();
        
        assertEquals(2, links.size());

        Link large = links.stream().filter(q -> q.getRel().equalsIgnoreCase("thumbnail_large")).findAny().get();
        assertEquals("thumbnail_large", large.getRel());
        assertEquals("http://www.nb.no/gallerinor/hent_bilde.php?id=51689&size=4", large.getHref());

        Link small = links.stream().filter(q -> q.getRel().equalsIgnoreCase("thumbnail_small")).findAny().get();
        assertEquals("thumbnail_small", small.getRel());
        assertEquals("http://www.nb.no/gallerinor/hent_bilde.php?id=51689&size=16", small.getHref());
    }

    @Test
    public void buildStandardThumbnailsTest() {
        ItemResource itemResource = new ItemResource();
        itemResource.setThumbnailUrn("URN:NBN:no-nb_digibok_2014062307158_C1");
        
        List<Link> links = new ThumbnailBuilder()
                .withItemResource(itemResource )
                .build();

        assertEquals(3, links.size());

        Link large = links.stream().filter(q -> q.getRel().equalsIgnoreCase("thumbnail_large")).findAny().get();
        assertEquals("thumbnail_large", large.getRel());

        Link medium = links.stream().filter(q -> q.getRel().equalsIgnoreCase("thumbnail_medium")).findAny().get();
        assertEquals("thumbnail_medium", medium.getRel());

        Link small = links.stream().filter(q -> q.getRel().equalsIgnoreCase("thumbnail_small")).findAny().get();
        assertEquals("thumbnail_small", small.getRel());
    }

    @Test
    public void buildEmptyThumbnailsTest() {

        List<Link> links = new ThumbnailBuilder()
                .build();
        assertEquals(0, links.size());
    }
}