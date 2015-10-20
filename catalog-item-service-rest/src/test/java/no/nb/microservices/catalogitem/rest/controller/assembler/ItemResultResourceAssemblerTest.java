package no.nb.microservices.catalogitem.rest.controller.assembler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import no.nb.microservices.catalogmetadata.model.mods.v3.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.rest.model.AccessInfo;
import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.catalogmetadata.model.fields.FieldResource;

public class ItemResultResourceAssemblerTest {

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
    public void testSelfLink() {
        ItemResultResourceAssembler resource = new ItemResultResourceAssembler();
        Item item = new Item.ItemBuilder("id1").build();
        ItemResource itemResource = resource.toResource(item );
        assertNotNull("Links should not be null", itemResource.getLinks());
        assertEquals("Should have a self-referential link element", "self", itemResource.getId().getRel());
    }

    @Test
    public void testModsLink() {
        ItemResultResourceAssembler resource = new ItemResultResourceAssembler();
        Item item = new Item.ItemBuilder("id1").build();
        ItemResource itemResource = resource.toResource(item );
        assertEquals("Should have a mods-referential link element", "mods", itemResource.getLink("mods").getRel());
    }

    @Test
    public void testPresentationLink() {
        ItemResultResourceAssembler resource = new ItemResultResourceAssembler();
        Item item = new Item.ItemBuilder("id1").build();
        ItemResource itemResource = resource.toResource(item );
        assertEquals("Should have a presentation-referential link element", "presentation", itemResource.getLink("presentation").getRel());
    }

    @Test
    public void testEnwRefererLink() {
        ItemResultResourceAssembler resource = new ItemResultResourceAssembler();
        Item item = new Item.ItemBuilder("id1").build();
        ItemResource itemResource = resource.toResource(item );
        assertEquals("Should have a enw-referential link element", "enw", itemResource.getLink("enw").getRel());
    }

    @Test
    public void testRisRefererLink() {
        ItemResultResourceAssembler resource = new ItemResultResourceAssembler();
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
    public void testMetadata() {
        ItemResultResourceAssembler resource = new ItemResultResourceAssembler();
        Mods mods = new Mods();
        TitleInfo titleInfo = new TitleInfo();
        titleInfo.setTitle("Supersonic");
        mods.setTitleInfos(Arrays.asList(titleInfo));
        TitleInfo alternativeTitleInfo = new TitleInfo();
        alternativeTitleInfo.setTitle("Supersonic alt");
        alternativeTitleInfo.setType("alternative");
        mods.setTitleInfos(Arrays.asList(titleInfo, alternativeTitleInfo));
        FieldResource fields = new FieldResource();
        fields.setTitle(titleInfo.getTitle() + " ct");
        Item item = new Item.ItemBuilder("id1").mods(mods).fields(fields ).build();
        
        ItemResource itemResource = resource.toResource(item );
        
        assertNotNull("Should not be null", itemResource);
        assertEquals("Title shoud be \"Supersonic\"", "Supersonic", itemResource.getMetadata().getTitleInfo().getTitle());
        assertEquals("AlternativeTitle shoud be \"Supersonic alt\"", "Supersonic alt", itemResource.getMetadata().getAlternativeTitleInfo().getTitle());
        assertEquals("CompositeTitle shoud be \"Supersonic ct\"", "Supersonic ct", itemResource.getMetadata().getCompositeTitle());
        
    }

    @Test
    public void testOriginInfo() {
        ItemResultResourceAssembler resource = new ItemResultResourceAssembler();
        
        Mods mods = new Mods();
        OriginInfo originInfo = new OriginInfo();
        DateMods dateCreated = new DateMods();
        dateCreated.setValue("2001-01-01");
        originInfo.setDateCreated(Arrays.asList(dateCreated));
        originInfo.setPublisher("Banana Airlines");
        mods.setOriginInfo(originInfo);

        Item item = new Item.ItemBuilder("id1").mods(mods).build();
        ItemResource itemResource = resource.toResource(item);

        assertNotNull("Should not be null", itemResource.getMetadata().getOriginInfo());
        assertEquals("Publisher should be Banana Airlines", "Banana Airlines" ,itemResource.getMetadata().getOriginInfo().getPublisher());
    }

    @Test
    public void testRecordInfo() {
        ItemResultResourceAssembler resource = new ItemResultResourceAssembler();

        Mods mods = new Mods();
        RecordInfo recordInfo = new RecordInfo();
        RecordIdentifier recordIdentifier = new RecordIdentifier();
        recordIdentifier.setValue("1888582");
        recordIdentifier.setSource("mavis.nb.no");
        recordInfo.setRecordIdentifier(recordIdentifier);
        mods.setRecordInfo(recordInfo);

        Item item = new Item.ItemBuilder("id1").mods(mods).build();
        ItemResource itemResource = resource.toResource(item);

        assertNotNull("Should not be null", itemResource.getMetadata().getRecordInfo());
        assertEquals("1888582", itemResource.getMetadata().getRecordInfo().getIdentifier());
        assertEquals("mavis.nb.no", itemResource.getMetadata().getRecordInfo().getIdentifierSource());
    }

    @Test
    public void testGeographic() {
        ItemResultResourceAssembler resource = new ItemResultResourceAssembler();

        Mods mods = new Mods();
        OriginInfo originInfo = new OriginInfo();
        Place place = new Place();
        place.setPlaceTerm("Norge;Telemark;Kviteseid;;;;;");
        originInfo.setPlace(place);
        mods.setOriginInfo(originInfo);

        Item item = new Item.ItemBuilder("id1").mods(mods).build();
        ItemResource itemResource = resource.toResource(item);

        assertNotNull("Should not be null", itemResource.getMetadata().getGeographic());
        assertEquals("Publisher should be Banana Airlines", "Norge;Telemark;Kviteseid;;;;;", itemResource.getMetadata().getGeographic().getPlaceString());
    }

    @Test
    public void testAccessInfo() {
        ItemResultResourceAssembler resource = new ItemResultResourceAssembler();
        
        FieldResource fields = new FieldResource();
        fields.setContentClasses(Arrays.asList("restricted", "public"));
        fields.setDigital(true);
        
        Item item = new Item.ItemBuilder("id1").fields(fields).hasAccess(true).build();
        ItemResource itemResource = resource.toResource(item );
        
        assertNotNull("Should not be null", itemResource);
        assertTrue("isDigital should be true", itemResource.getAccessInfo().isDigital());
        assertTrue("isPublicDomain should be true", itemResource.getAccessInfo().isPublicDomain());
        assertEquals("Access should be \"EVERYWHERE\"", "EVERYWHERE", itemResource.getAccessInfo().getAccessAllowedFrom());
        assertEquals("Viewability should be ALL", AccessInfoBuilder.VIEWABILITY_ALL, itemResource.getAccessInfo().getViewability());
        
    }

    @Test
    public void testPeople() {
        ItemResultResourceAssembler resource = new ItemResultResourceAssembler();

        Mods mods = new Mods();
        mods.setNames(Arrays.asList(createName("Bob Roger", "1990-", Arrays.asList("creator")),
                createName("Kurt Josef", null, null)));
        
        Item item = new Item.ItemBuilder("id1").mods(mods).build();        
        ItemResource itemResource = resource.toResource(item);

        assertNotNull("Should not be null", itemResource);
        assertNotNull("Should have list of people", itemResource.getMetadata().getPeople());
        assertEquals("First element should be \"Bob Roger\"", "Bob Roger", itemResource.getMetadata().getPeople().get(0).getName());
        assertEquals("First element should have date \"1990-\"", "1990-", itemResource.getMetadata().getPeople().get(0).getDate());
        assertEquals("First element should have role \"creator\"", "creator", itemResource.getMetadata().getPeople().get(0).getRoles().get(0).getName());
    }

    @Test
    public void testClassification() {
        ItemResultResourceAssembler resource = new ItemResultResourceAssembler();

        Mods mods = new Mods();
        List<no.nb.microservices.catalogmetadata.model.mods.v3.Classification> classifications = new ArrayList<>();
        classifications.add(createDdcClassification("123[S]"));
        classifications.add(createUdcClassification("456[S]"));
        mods.setClassifications(classifications);
        
        Item item = new Item.ItemBuilder("id1").mods(mods).build();        
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

    
    private Name createName(String value, String birthAndDeath,
            List<String> roleTerms) {
        Name name = new Name();
        name.setType("personal");
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
