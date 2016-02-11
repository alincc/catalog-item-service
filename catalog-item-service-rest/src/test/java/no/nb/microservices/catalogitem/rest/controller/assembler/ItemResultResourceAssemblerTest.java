package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.item.model.RelatedItems;
import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.catalogmetadata.model.fields.FieldResource;
import no.nb.microservices.catalogmetadata.model.mods.v3.*;
import no.nb.microservices.catalogmetadata.test.model.fields.TestFields;
import no.nb.microservices.catalogmetadata.test.mods.v3.TestMods;
import no.nb.microservices.catalogsearchindex.SearchResource;
import no.nb.microservices.catalogsearchindex.TestItemResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ItemResultResourceAssemblerTest {

    private ItemResultResourceAssembler resource = null;
    
    @Before
    public void init() {
        resource = new ItemResultResourceAssembler();

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/catalog/v1/id1");
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @After
    public void cleanUp() {
        RequestContextHolder.resetRequestAttributes();
    }
    
    @Test
    public void testSelfLink() {
        Item item = new Item.ItemBuilder("id1").build();
        ItemResource itemResource = resource.toResource(item );
        assertNotNull("Links should not be null", itemResource.getLinks());
        assertEquals("Should have a self-referential link element", "self", itemResource.getId().getRel());
    }

    @Test
    public void testModsLink() {
        Item item = new Item.ItemBuilder("id1").build();
        ItemResource itemResource = resource.toResource(item );
        assertEquals("Should have a mods-referential link element", "mods", itemResource.getLink("mods").getRel());
    }

    @Test
    public void testPresentationLink() {
        Item item = new Item.ItemBuilder("id1").build();
        ItemResource itemResource = resource.toResource(item );
        assertEquals("Should have a presentation-referential link element", "presentation", itemResource.getLink("presentation").getRel());
    }

    @Test
    public void testEnwRefererLink() {
        Item item = new Item.ItemBuilder("id1").build();
        ItemResource itemResource = resource.toResource(item );
        assertEquals("Should have a enw-referential link element", "enw", itemResource.getLink("enw").getRel());
    }

    @Test
    public void testRisRefererLink() {
        Item item = new Item.ItemBuilder("id1").build();
        ItemResource itemResource = resource.toResource(item );
        assertEquals("Should have a ris-referential link element", "ris", itemResource.getLink("ris").getRel());
    }

    @Test
    public void testWikiRefererLink() {
        ItemResultResourceAssembler resource = new ItemResultResourceAssembler();
        Item item = new Item.ItemBuilder("id1").build();
        ItemResource itemResource = resource.toResource(item );
        assertEquals("Should have a wiki-referential link element", "wiki", itemResource.getLink("wiki").getRel());
    }

    @Test
    public void testPlaylistLink() {
        Item item = new Item.ItemBuilder("id1")
                .mods(TestMods.aDefaultMusicTrack().build())
                .withItemResource(TestItemResource.aDefaultMusic().build())
                .build();
        ItemResource itemResource = resource.toResource(item);

        assertEquals("Should have a playlist link element", "playlist", itemResource.getLink("playlist").getRel());
    }

    @Test
    public void testThumbnailLinks() {
        no.nb.microservices.catalogsearchindex.ItemResource itemIndexResource = new no.nb.microservices.catalogsearchindex.ItemResource();
        itemIndexResource.setThumbnailUrn("URN:NBN:no-nb_digibok_2014062307158_C1");
        Item item = new Item.ItemBuilder("id1")
                .withItemResource(itemIndexResource)
                .build();
        
        ItemResource itemResource = resource.toResource(item );

        assertEquals("Should have a thumbnail_small link element", "thumbnail_small", itemResource.getLink("thumbnail_small").getRel());
        assertEquals("Should have a thumbnail_medium link element", "thumbnail_medium", itemResource.getLink("thumbnail_medium").getRel());
        assertEquals("Should have a thumbnail_large link element", "thumbnail_large", itemResource.getLink("thumbnail_large").getRel());    
    }
    
    @Test
    public void testRelatedItemsLink() {
        Item host = new Item.ItemBuilder("id1")
                .mods(TestMods.aDefaultMusicAlbum().build())
                .build();
        RelatedItems relatedItems = new RelatedItems(null, Arrays.asList(host));
        Item item = new Item.ItemBuilder("id1")
                .mods(TestMods.aDefaultMusicTrack().build())
                .withRelatedItems(relatedItems)
                .build();

        
        ItemResource itemResource = resource.toResource(item );
        
        assertThat(itemResource.getLink("relatedItems").getHref(), is("http://localhost/catalog/v1/items/id1/relatedItems"));
    }

    @Test
    public void testNoRelatedItemsLink() {
        Item item = new Item.ItemBuilder("id1")
                .mods(TestMods.aDefaultBookMods().build())
                .build();
        
        ItemResource itemResource = resource.toResource(item );
        
        assertNull(itemResource.getLink("relatedItems"));
    }

    @Test
    public void testMetadata() {
        Mods mods = new Mods();
        TitleInfo titleInfo = new TitleInfo();
        titleInfo.setTitle("Supersonic");
        mods.setTitleInfos(Arrays.asList(titleInfo));
        TitleInfo alternativeTitleInfo = new TitleInfo();
        alternativeTitleInfo.setTitle("Supersonic alt");
        alternativeTitleInfo.setType("alternative");
        mods.setTitleInfos(Arrays.asList(titleInfo, alternativeTitleInfo));
        no.nb.microservices.catalogsearchindex.ItemResource searchIndexResource = new no.nb.microservices.catalogsearchindex.ItemResource();
        searchIndexResource.setTitle(titleInfo.getTitle() + " ct");
        Item item = new Item.ItemBuilder("id1")
                .mods(mods)
                .withItemResource(searchIndexResource)
                .withExpand("metadata")
                .build();
        
        ItemResource itemResource = resource.toResource(item );
        
        assertNotNull("Should not be null", itemResource);
        assertTrue("Title shoud be \"Supersonic\"", !itemResource.getMetadata().getTitleInfos().isEmpty());
        
    }
    
    @Test
    public void testTitle() {
        no.nb.microservices.catalogsearchindex.ItemResource searchIndexResource = new no.nb.microservices.catalogsearchindex.ItemResource();
        searchIndexResource.setTitle("Supersonic");

        Item item = new Item.ItemBuilder("id1")
                .withItemResource(searchIndexResource)
                .build();
        
        ItemResource itemResource = resource.toResource(item);
        
        assertEquals("Supersonic", itemResource.getTitle());
    }
    
    @Test
    public void testHosts() {
        Item host = new Item.ItemBuilder("id1")
                .mods(TestMods.aDefaultMusicAlbum().build())
                .build();
        RelatedItems relatedItems = new RelatedItems(null, Arrays.asList(host));
        Item item = new Item.ItemBuilder("id1")
                .mods(TestMods.aDefaultMusicTrack().build())
                .withRelatedItems(relatedItems)
                .build();

        ItemResource itemResource = resource.toResource(item );
        
        assertTrue("Should have hosts", !itemResource.getRelatedItems().getHosts().isEmpty());
    }
    
    @Test
    public void testConstitutent() {
        Item track = new Item.ItemBuilder("id1")
                .mods(TestMods.aDefaultMusicTrack().build())
                .build();
        RelatedItems relatedItems = new RelatedItems(Arrays.asList(track), null);
        Item item = new Item.ItemBuilder("id1")
                .mods(TestMods.aDefaultMusicAlbum().build())
                .withRelatedItems(relatedItems)
                .build();

        ItemResource itemResource = resource.toResource(item );
        
        assertTrue("Should have constituents", !itemResource.getRelatedItems().getConstituents().isEmpty());
    }


    @Test
    public void testOriginInfo() {
        Mods mods = new Mods();
        OriginInfo originInfo = new OriginInfo();
        DateMods dateCreated = new DateMods();
        dateCreated.setValue("2001-01-01");
        originInfo.setDateCreated(Arrays.asList(dateCreated));
        originInfo.setPublisher("Banana Airlines");
        mods.setOriginInfo(originInfo);

        Item item = new Item.ItemBuilder("id1")
                .mods(mods)
                .withExpand("metadata")
                .build();
        ItemResource itemResource = resource.toResource(item);

        assertNotNull("Metadata should not be null", itemResource.getMetadata());
        assertNotNull("OriginInfo should not be null", itemResource.getMetadata().getOriginInfo());
        assertEquals("Publisher should be Banana Airlines", "Banana Airlines" ,itemResource.getMetadata().getOriginInfo().getPublisher());
    }

    @Test
    public void testRecordInfo() {
        Mods mods = new Mods();
        RecordInfo recordInfo = new RecordInfo();
        RecordIdentifier recordIdentifier = new RecordIdentifier();
        recordIdentifier.setValue("1888582");
        recordIdentifier.setSource("mavis.nb.no");
        recordInfo.setRecordIdentifier(recordIdentifier);
        mods.setRecordInfo(recordInfo);

        Item item = new Item.ItemBuilder("id1").mods(mods).withExpand("metadata").build();
        ItemResource itemResource = resource.toResource(item);

        assertNotNull("Should not be null", itemResource.getMetadata().getRecordInfo());
        assertEquals("1888582", itemResource.getMetadata().getRecordInfo().getIdentifier());
        assertEquals("mavis.nb.no", itemResource.getMetadata().getRecordInfo().getIdentifierSource());
    }

    @Test
    public void testGeographic() {
        Mods mods = new Mods();
        OriginInfo originInfo = new OriginInfo();
        Place place = new Place();
        place.setPlaceTerm("Norge;Telemark;Kviteseid;;;;;");
        originInfo.setPlace(place);
        mods.setOriginInfo(originInfo);

        Item item = new Item.ItemBuilder("id1").mods(mods).withExpand("metadata").build();
        ItemResource itemResource = resource.toResource(item);

        assertNotNull("Should not be null", itemResource.getMetadata().getGeographic());
        assertEquals("Publisher should be Banana Airlines", "Norge;Telemark;Kviteseid;;;;;", itemResource.getMetadata().getGeographic().getPlaceString());
    }

    @Test
    public void testAccessInfo() {
        no.nb.microservices.catalogsearchindex.ItemResource searchIndexResource = new no.nb.microservices.catalogsearchindex.ItemResource();
        searchIndexResource.setDigital(true);
        searchIndexResource.setContentClasses(Arrays.asList("restricted", "public"));
        Item item = new Item.ItemBuilder("id1")
                .withItemResource(searchIndexResource)
                .hasAccess(true)
                .build();
        ItemResource itemResource = resource.toResource(item );
        
        assertNotNull("Should not be null", itemResource);
        assertTrue("isDigital should be true", itemResource.getAccessInfo().isDigital());
        assertTrue("isPublicDomain should be true", itemResource.getAccessInfo().isPublicDomain());
        assertEquals("Access should be \"EVERYWHERE\"", "EVERYWHERE", itemResource.getAccessInfo().getAccessAllowedFrom());
        assertEquals("Viewability should be ALL", AccessInfoBuilder.VIEWABILITY_ALL, itemResource.getAccessInfo().getViewability());
        
    }

    @Test
    public void testPeople() {
        Mods mods = new Mods();
        RoleTerm creator = new RoleTerm();
        creator.setValue("creator");
        mods.setNames(Arrays.asList(
                createName("Bob Roger", "1990-", Arrays.asList(creator), "personal"),
                createName("Kurt Josef", null, null, "personal")));
        
        Item item = new Item.ItemBuilder("id1")
                .mods(mods)
                .withExpand("metadata")
                .build();
        ItemResource itemResource = resource.toResource(item);

        assertNotNull("Should not be null", itemResource);
        assertNotNull("Should have list of people", itemResource.getMetadata().getPeople());
        assertEquals("First element should be \"Bob Roger\"", "Bob Roger", itemResource.getMetadata().getPeople().get(0).getName());
        assertEquals("First element should have date \"1990-\"", "1990-", itemResource.getMetadata().getPeople().get(0).getDate());
        assertEquals("First element should have role \"creator\"", "creator", itemResource.getMetadata().getPeople().get(0).getRoles().get(0).getName());
    }

    @Test
    public void testCorporate() {
        List<Namepart> nameparts = new ArrayList<>();
        nameparts.add(createNamePart("Det Norske teatret", null));
        nameparts.add(createNamePart("Centralteatret", null));

        Name name = new Name();
        name.setNameParts(nameparts);
        name.setType("corporate");

        RoleTerm roleTerm = new RoleTerm();
        roleTerm.setValue("Rettighetshaver");
        Mods mods = new Mods();
        mods.setNames(Arrays.asList(
                name,
                createName("Nordland Teater", null, Arrays.asList(roleTerm), "corporate")));
        Item item = new Item.ItemBuilder("id1").mods(mods).withExpand("metadata").build();
        ItemResource itemResource = resource.toResource(item);

        assertNotNull("Should not be null", itemResource);
        assertEquals("List of corporates should have 3 elements", 3, itemResource.getMetadata().getCorporates().size());
        assertEquals("First element should be \"Det Norske teatret\"", "Det Norske teatret", itemResource.getMetadata().getCorporates().get(0).getName());
        assertEquals("Second element should be \"Centralteatret\"", "Centralteatret", itemResource.getMetadata().getCorporates().get(1).getName());
        assertEquals("Third element should be \"Nordland Teater\"", "Nordland Teater", itemResource.getMetadata().getCorporates().get(2).getName());
        assertEquals("Third element should have role \"Rettighetshaver\"", "Rettighetshaver", itemResource.getMetadata().getCorporates().get(2).getRoles().get(0).getName());
    }

    @Test
    public void testClassification() {
        Mods mods = new Mods();
        List<no.nb.microservices.catalogmetadata.model.mods.v3.Classification> classifications = new ArrayList<>();
        classifications.add(createDdcClassification("123[S]"));
        classifications.add(createUdcClassification("456[S]"));
        mods.setClassifications(classifications);
        
        Item item = new Item.ItemBuilder("id1").mods(mods).withExpand("metadata").build();
        ItemResource itemResource = resource.toResource(item);

        assertNotNull("Should not be null", itemResource.getMetadata().getClassification());
        assertNotNull("Should have ddc", itemResource.getMetadata().getClassification().getDdc());
        assertNotNull("Should have udc", itemResource.getMetadata().getClassification().getUdc());
    }

    private Classification createDdcClassification(
            String value) {
        Classification classification = new Classification();
        classification.setAuthority("ddc");
        classification.setValue(value);
        return classification;
    }

    private Classification createUdcClassification(
            String value) {
        Classification classification = new Classification();
        classification.setAuthority("udc");
        classification.setValue(value);
        return classification;
    }

    private Namepart createNamePart(String value, String type) {
        Namepart namepart = new Namepart();
        namepart.setValue(value);
        namepart.setType(type);
        return namepart;
    }

    
    private Name createName(String value, String birthAndDeath, List<RoleTerm> roleTerms, String type) {
        Name name = new Name();
        name.setType(type);
        List<Namepart> nameParts = new ArrayList<>();
        
        Namepart namepart = new Namepart();
        namepart.setValue(value);
        nameParts.add(namepart);

        Namepart date = new Namepart();
        date.setValue(birthAndDeath);
        date.setType("date");
        nameParts.add(date);

        name.setNameParts(nameParts);
        
        List<Role> roles = new ArrayList<>();
        Role role = new Role();
        role.setRoleTerms(roleTerms);
        roles.add(role );
        name.setRole(roles);
        
        return name;
    }
}
