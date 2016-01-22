package no.nb.microservices.catalogitem.rest.controller.assembler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.hateoas.Link;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ResourceLinkBuilderTest {

    private MockHttpServletRequest request;

    @Before
    public void init() {
        request = new MockHttpServletRequest("GET", "/catalog/v1/id1");
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @After
    public void cleanUp() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void whenHostInHeaderThenUseItInHref() {
        request.addHeader("X-Forwarded-Host", "api.nb.no");
        
        Link modsLink = ResourceLinkBuilder.linkTo(ResourceTemplateLink.MODS, "id1").withRel("mods");

        assertTrue("Should have api.nb.no as host", modsLink.getHref().startsWith("http://api.nb.no/"));
    }

    @Test
    public void whenForwardedHostInHeaderThenUseItInHref() {
        request.addHeader("Forwarded", "host=api.nb.no");
        
        Link modsLink = ResourceLinkBuilder.linkTo(ResourceTemplateLink.MODS, "id1").withRel("mods");

        assertTrue("Should have api.nb.no as host", modsLink.getHref().startsWith("http://api.nb.no/"));
    }
    
    @Test
    public void whenHostHasPortInHeaderThenUseItInHref() {
        request.addHeader("X-Forwarded-Host", "api.nb.no:8080");
        
        Link modsLink = ResourceLinkBuilder.linkTo(ResourceTemplateLink.MODS, "id1").withRel("mods");

        assertTrue("Port should be 8080", modsLink.getHref().startsWith("http://api.nb.no:8080/"));
    }

    @Test
    public void whenProtoIsHttpsThenSchemeShouldBeHttps() {
        request.addHeader("X-Forwarded-proto", "https");
        
        Link modsLink = ResourceLinkBuilder.linkTo(ResourceTemplateLink.MODS, "id1").withRel("mods");

        assertTrue("Should have https as scheme", modsLink.getHref().startsWith("https"));
    }

    @Test
    public void whenSslIsOnThenSchemeShouldBeHttps() {
        request.addHeader("X-Forwarded-Ssl", "on");
        
        Link modsLink = ResourceLinkBuilder.linkTo(ResourceTemplateLink.MODS, "id1").withRel("mods");

        assertTrue("Should have https as scheme", modsLink.getHref().startsWith("https"));
    }

    @Test
    public void whenPortThenPortShouldBeUsed() {
        request.addHeader("X-Forwarded-Host", "api.nb.no");
        request.addHeader("X-Forwarded-Port", "8443");
        
        Link modsLink = ResourceLinkBuilder.linkTo(ResourceTemplateLink.MODS, "id1").withRel("mods");

        assertTrue("Port should be 8443", modsLink.getHref().startsWith("http://api.nb.no:8443"));
    }

    @Test
    public void whenModsThenReturnModsLink() {
        Link modsLink = ResourceLinkBuilder.linkTo(ResourceTemplateLink.MODS, "id1").withRel("mods");

        assertEquals("should have \"mods\" rel", "mods", modsLink.getRel());
        assertEquals("Should have mods href", "http://localhost/catalog/v1/metadata/id1/mods", modsLink.getHref());
    }

    @Test
    public void whenPresentationThenReturnPresentationLink() {
        Link presentationLink = ResourceLinkBuilder.linkTo(ResourceTemplateLink.PRESENTATION, "id1").withRel("presentation");

        assertEquals("should have \"presentation\" rel", "presentation", presentationLink.getRel());
        assertEquals("Should have mods href", "http://localhost/catalog/v1/iiif/id1/manifest", presentationLink.getHref());
    }

}