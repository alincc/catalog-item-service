package no.nb.microservices.catalogitem.rest.controller;

import static org.junit.Assert.*;

import java.util.Arrays;

import no.nb.microservices.catalogitem.core.item.model.AccessInfo;
import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.item.model.Origin;
import no.nb.microservices.catalogitem.core.item.model.Person;
import no.nb.microservices.catalogitem.rest.model.ItemResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 
 * @author ronnymikalsen
 * @author rolfmathisen
 *
 */
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
    public void testLinks() {
        ItemResultResourceAssembler resource = new ItemResultResourceAssembler();
        Item item = new Item();
        item.setId("id1");
        ItemResource itemResource = resource.toResource(item );
        assertNotNull("Links should not be null", itemResource.getLinks());
        assertEquals("Should have a self-referential link element", "self", itemResource.getId().getRel());
    }
    
    @Test
    public void testMetadata() {
        ItemResultResourceAssembler resource = new ItemResultResourceAssembler();
        Item item = new Item();
        String title = "Supersonic";
        item.setTitle(title);
        ItemResource itemResource = resource.toResource(item );
        
        assertNotNull("Should not be null", itemResource);
        assertEquals("Title shoud be \"Supersonic\"", title, itemResource.getMetadata().getTitleInfo().getTitle());
        
    }

    @Test
    public void testOriginInfo() {
        ItemResultResourceAssembler resource = new ItemResultResourceAssembler();
        Item item = new Item();
        Origin origin = new Origin();
        origin.setDateCreated("2001-01-01");
        origin.setPublisher("Banana Airlines");
        item.setOrigin(origin);
        ItemResource itemResource = resource.toResource(item);

        assertNotNull("Should not be null", itemResource.getMetadata().getOriginInfo());
        assertEquals("Publisher should be Banana Airlines", "Banana Airlines" ,itemResource.getMetadata().getOriginInfo().getPublisher());
    }

    @Test
    public void testAccessInfo() {
        ItemResultResourceAssembler resource = new ItemResultResourceAssembler();
        Item item = new Item();
        AccessInfo accessInfo = new AccessInfo();
        accessInfo.setDigital(true);
        accessInfo.setContentClasses(Arrays.asList("restricted", "public"));
        accessInfo.setHasAccess(true);
        item.setAccessInfo(accessInfo);
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
        Item item = new Item();
        Person person1 = new Person();
        person1.setName("Bob Roger");
        person1.setDate("1990-");
        person1.setRoles(Arrays.asList("creator"));

        Person person2 = new Person();
        person2.setName("Kurt Josef");
        item.setPersons(Arrays.asList(person1));
        ItemResource itemResource = resource.toResource(item);

        assertNotNull("Should not be null", itemResource);
        assertNotNull("Should have list of people", itemResource.getMetadata().getPeople());
    }

}
