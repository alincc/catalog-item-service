package no.nb.microservices.catalogitem.rest.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import no.nb.microservices.catalogitem.core.item.model.AccessInfo;
import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.catalogmetadata.model.fields.Fields;
import no.nb.microservices.catalogmetadata.model.mods.v3.Classification;
import no.nb.microservices.catalogmetadata.model.mods.v3.DateMods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Name;
import no.nb.microservices.catalogmetadata.model.mods.v3.Namepart;
import no.nb.microservices.catalogmetadata.model.mods.v3.OriginInfo;
import no.nb.microservices.catalogmetadata.model.mods.v3.Role;
import no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo;

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
    public void testMetadata() {
        ItemResultResourceAssembler resource = new ItemResultResourceAssembler();
        Mods mods = new Mods();
        TitleInfo titleInfo = new TitleInfo();
        titleInfo.setTitle("Supersonic");
        mods.setTitleInfos(Arrays.asList(titleInfo));
        Item item = new Item.ItemBuilder("id1").mods(mods).build();
        ItemResource itemResource = resource.toResource(item );
        
        assertNotNull("Should not be null", itemResource);
        assertEquals("Title shoud be \"Supersonic\"", "Supersonic", itemResource.getMetadata().getTitleInfo().getTitle());
        
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
    public void testAccessInfo() {
        ItemResultResourceAssembler resource = new ItemResultResourceAssembler();
        
        Fields fields = new Fields();
        fields.setContentClasses(Arrays.asList("restricted", "public"));
        fields.setDigital(true);
        
        Item item = new Item.ItemBuilder("id1").fields(fields).hasAccess(true).build();
        ItemResource itemResource = resource.toResource(item );
        
        assertNotNull("Should not be null", itemResource);
        assertTrue("isDigital should be true", itemResource.getAccessInfo().isDigital());
        assertTrue("isPublicDomain should be true", itemResource.getAccessInfo().isPublicDomain());
        assertEquals("Access should be \"EVERYWHERE\"", "EVERYWHERE", item.getAccessInfo().accessAllowedFrom());
        assertEquals("Viewability should be ALL", AccessInfo.VIEWABILITY_ALL, itemResource.getAccessInfo().getViewability());
        
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
