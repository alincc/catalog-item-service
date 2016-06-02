package no.nb.microservices.catalogitem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import no.nb.commons.web.util.UserUtils;
import no.nb.microservices.catalogitem.rest.controller.assembler.AccessInfoBuilder;
import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.catalogitem.rest.model.ItemSearchResource;
import no.nb.microservices.catalogitem.rest.model.Metadata;
import no.nb.microservices.catalogmetadata.test.exception.TestDataException;
import no.nb.microservices.catalogmetadata.test.model.fields.TestFields;
import no.nb.microservices.catalogmetadata.test.mods.v3.TestMods;
import no.nb.microservices.catalogsearchindex.EmbeddedWrapper;
import no.nb.microservices.catalogsearchindex.SearchResource;
import no.nb.sesam.ni.niclient.NiClient;
import no.nb.sesam.ni.niserver.AuthorisationHandler;
import no.nb.sesam.ni.niserver.AuthorisationHandlerResolver;
import no.nb.sesam.ni.niserver.AuthorisationRequest;
import no.nb.sesam.ni.niserver.NiServer;
import no.nb.sesam.ni.niserver.authorisation.AcceptHandler;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.SocketUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { TestConfig.class,
        RibbonClientConfiguration.class, TestNiConfig.class })
@WebIntegrationTest("server.port: 0")
public class ItemControllerIT {
    public static String TEST_SERVER_ADDR;
    private static int TEST_SERVER_PORT;
    private static NiServer ni;
    Logger logger = LoggerFactory.getLogger(ItemControllerIT.class);
    @Value("${local.server.port}")
    int port;
    RestTemplate template = new TestRestTemplate();
    @Autowired
    ILoadBalancer lb;
    MockWebServer server;

    @BeforeClass
    public static void setUpClass() throws Exception {
        TEST_SERVER_PORT = SocketUtils.findAvailableTcpPort();
        TEST_SERVER_ADDR = "localhost:" + TEST_SERVER_PORT;

        ni = new NiServer(TEST_SERVER_PORT,
                new no.nb.sesam.ni.niserver.Cluster(TEST_SERVER_ADDR),
                new MockAuthorisationHandlerResolver(), null, null);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        ni.shutdown(500);
    }

    private static String objectToJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            throw new TestDataException(ex);
        }
    }
    
    @Before
    public void setup() throws Exception {
        String searchResource1 = IOUtils.toString(getClass().getResourceAsStream("/no/nb/microservices/catalogitem/searchResource.json"));
        String searchResultMock = IOUtils.toString(this.getClass().getResourceAsStream("catalog-search-index-service.json"));
        String searchResultMockWithAggragations = IOUtils.toString(this.getClass().getResourceAsStream("catalog-search-index-service-aggregations.json"));

        server = new MockWebServer();
        final Dispatcher dispatcher = new Dispatcher() {

            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                System.out.println("REQUEST: " + request.getPath());

                if (request.getPath().equals("/catalog/v1/search?q=Ola&page=0&size=10&sort=title&sort=desc&grouping=false&explain=false")) {
                    return new MockResponse().setBody(searchResultMock).setResponseCode(200).setHeader("Content-Type", "application/hal+json");
                } else if (request.getPath().contains("/mods")) {
                    return new MockResponse().setBody(TestMods.aDefaultBookModsXml())
                            .setResponseCode(200)
                            .setHeader("Content-Type", "application/xml");
                } else if (request.getPath().contains("/fields")) {
                    return new MockResponse().setBody(TestFields.aDefaultBookJson())
                            .setResponseCode(200)
                            .setHeader("Content-Type", "application/json");
                } else if (request.getPath().startsWith("/catalog/v1/search?q=sesamid%3Aid&explain=false")) {
                    return new MockResponse().setBody(searchResource1)
                            .setResponseCode(200)
                            .setHeader("Content-Type", "application/json");
                } else if (request.getPath().equals("/catalog/v1/search?q=*&page=0&size=10&grouping=false&aggs=ddc1%2Cmediatype&explain=false")) {
                    return new MockResponse().setBody(searchResultMockWithAggragations).setResponseCode(200).setHeader("Content-Type", "application/hal+json");
                } else if (request.getPath().equals("/catalog/v1/search?q=Ola&page=0&size=10&sort=title%2Cdesc&grouping=false&explain=false")) {
                    return new MockResponse().setBody(searchResultMock).setResponseCode(200).setHeader("Content-Type", "application/hal+json");
                } else if (request.getPath().equals("/catalog/v1/search?q=*&page=0&size=10&grouping=false&boost=title%2C10&boost=name%2C4&explain=false")) {
                    return new MockResponse().setBody(searchResultMock).setResponseCode(200).setHeader("Content-Type", "application/hal+json");
                } else if (request.getPath().equals("/catalog/v1/search?q=*&page=0&size=10&grouping=false&should=title&should=peter&explain=false")) {
                    return new MockResponse().setBody(searchResultMock).setResponseCode(200).setHeader("Content-Type", "application/hal+json");
                }
                logger.error("Request \"" + request.getPath() +"\"not found");
                return new MockResponse().setResponseCode(404);
            }
        };
        server.setDispatcher(dispatcher);
        server.start();

        BaseLoadBalancer blb = (BaseLoadBalancer) lb;
        blb.setServersList(Arrays.asList(new Server(server.getHostName(), server.getPort())));
    }

    @Test
    public void testSearch() throws Exception {
        String url = "http://localhost:" + port + "/catalog/v1/items?q=Ola&size=10&sort=title,desc";
        ResponseEntity<SearchResource> entity = getEntity(url, SearchResource.class);

        assertThat("Status code should be 200 ", entity.getStatusCode().value(), is(200));
        assertNotNull("Response should have page element", entity.getBody().getMetadata());
        assertNotNull("Response should have _embedded element", entity.getBody().getEmbedded());
        assertNotNull("Response should have links", entity.getBody().getLinks());
        assertEquals("It should be 4 elements in items array", 4, entity.getBody().getEmbedded().getItems().size());
    }

    @Test
    public void testSearchWithAggregations() throws Exception{
        String url = "http://localhost:" + port + "/catalog/v1/items?q=*&aggs=ddc1,mediatype";
        ResponseEntity<ItemSearchResource> entity = getEntity(url, ItemSearchResource.class);

        assertThat("Status code should be 200 ", entity.getStatusCode().value(), is(200));
        assertNotNull("Response should contain aggregations", entity.getBody().getEmbedded().getAggregations());
        assertEquals("Should contain 2 aggragations", 2, entity.getBody().getEmbedded().getAggregations().size());
    }

    @Test
    public void testSearchWithBoost() throws Exception{
        String url = "http://localhost:" + port + "/catalog/v1/items?q=*&boost=title,10&boost=name,4";
        ResponseEntity<ItemSearchResource> entity = getEntity(url, ItemSearchResource.class);

        assertThat("Status code should be 200 ", entity.getStatusCode().value(), is(200));
    }

    @Test
    public void testSearchWithShould() throws Exception {
        String url = "http://localhost:" + port + "/catalog/v1/items?q=*&should=title,peter";
        ResponseEntity<ItemSearchResource> entity = getEntity(url, ItemSearchResource.class);

        assertThat("Status code should be 200 ", entity.getStatusCode().value(), is(200));
    }

    @Test
    public void testGetItem() throws Exception {
        String searchResource = IOUtils.toString(getClass().getResourceAsStream("/no/nb/microservices/catalogitem/searchResource.json"));
        MockWebServer server = new MockWebServer();
        final Dispatcher dispatcher = new Dispatcher() {

            @Override
            public MockResponse dispatch(RecordedRequest request)
                    throws InterruptedException {
                System.out.println(request.getPath());
                if (request.getPath().equals("/catalog/v1/metadata/id1/mods?X-Original-IP-Fra-Frontend=123.45.100.1&amsso=token")) {
                    return new MockResponse().setBody(TestMods.aDefaultBookModsXml())
                            .setResponseCode(200)
                            .setHeader("Content-Type", "application/xml");
                } else if (request.getPath().equals("/catalog/v1/metadata/id1/fields?X-Original-IP-Fra-Frontend=123.45.100.1&amsso=token")) {
                    return new MockResponse().setBody(TestFields.aDefaultBookJson())
                            .setResponseCode(200)
                            .setHeader("Content-Type", "application/json");
                } else if (request.getPath().equals("/catalog/v1/search?q=sesamid%3Aid1&page=0&size=1&grouping=false&explain=false&X-Original-IP-Fra-Frontend=123.45.100.1&amsso=token")) {
                    return new MockResponse().setBody(searchResource)
                            .setResponseCode(200)
                            .setHeader("Content-Type", "application/json");
                }
                System.out.println("ERRRORRRR: " + request.getPath());
                return new MockResponse().setResponseCode(404);
            }
        };
        server.setDispatcher(dispatcher);
        server.start();

        BaseLoadBalancer blb = (BaseLoadBalancer) lb;
        blb.setServersList(Arrays.asList(new Server(server.getHostName(),
                server.getPort())));

        HttpHeaders headers = defaultHeaders();

        ResponseEntity<ItemResource> entity = new TestRestTemplate().exchange(
                "http://localhost:" + port + "/catalog/v1/items/id1", HttpMethod.GET,
                new HttpEntity<Void>(headers), ItemResource.class);

        assertTrue("Status code should be 200 ", entity.getStatusCode()
                .is2xxSuccessful());
        assertNotNull("Response should have page element", entity.getBody()
                .getMetadata());
        assertNotNull("Response should have links", entity.getBody().getLinks());
        assertEquals("Title should be \"Villanden\"", "Villanden",
                entity.getBody().getMetadata().getTitleInfos().get(0).getTitle());
        assertTrue("isDigital should be true", entity.getBody().getAccessInfo()
                .isDigital());
        assertTrue("isPublicDomain should be true", entity.getBody()
                .getAccessInfo().isPublicDomain());
        assertEquals("Viewability should be ALL", AccessInfoBuilder.VIEWABILITY_ALL, entity.getBody()
                .getAccessInfo().getViewability());
        assertEquals("Access should be \"EVERYWHERE\"", "EVERYWHERE", entity.getBody()
        		.getAccessInfo().getAccessAllowedFrom());
        assertEquals("Response should have 2 people", 2, entity.getBody().getMetadata().getPeople().size());
    }

    @Test
    public void testExpandrelatedItems() throws Exception {
        String searchResource = IOUtils.toString(getClass().getResourceAsStream("/no/nb/microservices/catalogitem/searchResource.json"));
        MockWebServer server = new MockWebServer();
        final Dispatcher dispatcher = new Dispatcher() {

            @Override
            public MockResponse dispatch(RecordedRequest request)
                    throws InterruptedException {
                if (request.getPath().contains("/mods")) {
                    return new MockResponse().setBody(TestMods.aDefaultMusicAlbumXml())
                            .setResponseCode(200)
                            .setHeader("Content-Type", "application/xml");
                } else if (request.getPath().contains("/fields")) {
                    return new MockResponse().setBody(TestFields.aDefaultMusicJson())
                            .setResponseCode(200)
                            .setHeader("Content-Type", "application/json");
                } else if (request.getPath().contains("/catalog/v1/search?q=sesamid")) {
                    return new MockResponse().setBody(searchResource)
                            .setResponseCode(200)
                            .setHeader("Content-Type", "application/json");
                } else if (request.getPath().contains("/catalog/v1/search?q=oaiid")) {
                    PageMetadata pageMetadata = new PageMetadata(1, 0, 1);
                    EmbeddedWrapper wrapper = new EmbeddedWrapper();
                    List<no.nb.microservices.catalogsearchindex.ItemResource> items = new ArrayList<>();
                    no.nb.microservices.catalogsearchindex.ItemResource item = new no.nb.microservices.catalogsearchindex.ItemResource();
                    item.setItemId("12345");
                    items.add(item);
                    wrapper.setItems(items);
                    SearchResource searchResource = new SearchResource(pageMetadata, wrapper);

                    return new MockResponse().setBody(objectToJson(searchResource))
                            .setResponseCode(200)
                            .setHeader("Content-Type", "application/json");
                }


                return new MockResponse().setResponseCode(404);
            }
        };
        server.setDispatcher(dispatcher);
        server.start();

        BaseLoadBalancer blb = (BaseLoadBalancer) lb;
        blb.setServersList(Arrays.asList(new Server(server.getHostName(),
                server.getPort())));

        HttpHeaders headers = defaultHeaders();

        ResponseEntity<ItemResource> entity = new TestRestTemplate().exchange(
                "http://localhost:" + port + "/catalog/v1/items/id1?expand=relatedItems", HttpMethod.GET,
                new HttpEntity<Void>(headers), ItemResource.class);

        Metadata metadata = entity.getBody().getMetadata();
        assertTrue("Status code should be 200 ", entity.getStatusCode()
                .is2xxSuccessful());
        assertNotNull("Should have relateditems", entity.getBody().getRelatedItems());
    }
    
    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(UserUtils.SSO_HEADER, "token");
        headers.add(UserUtils.REAL_IP_HEADER, "123.45.100.1");
        return headers;
    }
    
    private <T> ResponseEntity<T> getEntity(String url, Class<T> type) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(UserUtils.SSO_HEADER, "token");
        headers.add(UserUtils.REAL_IP_HEADER, "123.45.100.1");
        return new TestRestTemplate().exchange(url, HttpMethod.GET, new HttpEntity<Void>(headers), type);
    }
}

@Configuration
class RibbonClientConfiguration {

    @Bean
    public ILoadBalancer ribbonLoadBalancer() {
        return new BaseLoadBalancer();
    }
}

@Configuration
class TestNiConfig {

    @Bean
    public NiClient getNiClient() throws Exception {
        return new NiClient(ItemControllerIT.TEST_SERVER_ADDR);
    }
}

class MockAuthorisationHandlerResolver implements AuthorisationHandlerResolver {

    public AuthorisationHandler resolve(AuthorisationRequest request) {
            return new AcceptHandler();
    }

}