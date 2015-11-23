package no.nb.microservices.catalogitem;

import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import no.nb.commons.web.util.UserUtils;
import no.nb.microservices.catalogitem.rest.model.ItemSearchResource;
import no.nb.microservices.catalogmetadata.test.model.fields.TestFields;
import no.nb.microservices.catalogmetadata.test.mods.v3.TestMods;
import no.nb.microservices.catalogsearchindex.SearchResource;
import no.nb.sesam.ni.niclient.NiClient;
import no.nb.sesam.ni.niserver.NiServer;
import org.apache.commons.io.IOUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.SocketUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { TestConfig.class, RibbonClientConfiguration.class, TestNiConfig2.class })
@WebIntegrationTest("server.port: 0")
public class SearchControllerIT {

    @Value("${local.server.port}")
    int port;

    RestTemplate template = new TestRestTemplate();

    @Autowired
    ILoadBalancer lb;

    private static int TEST_SERVER_PORT;
    public static String TEST_SERVER_ADDR;

    private static NiServer niServer;

    MockWebServer server;

    @BeforeClass
    public static void setUpClass() throws Exception {
        TEST_SERVER_PORT = SocketUtils.findAvailableTcpPort();
        TEST_SERVER_ADDR = "localhost:" + TEST_SERVER_PORT;

        niServer = new NiServer(TEST_SERVER_PORT,
                new no.nb.sesam.ni.niserver.Cluster(TEST_SERVER_ADDR),
                new MockAuthorisationHandlerResolver(), null, null);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        niServer.shutdown(500);
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
                System.out.println(request.getPath());
                if (request.getPath().equals("/search?q=Ola&fields=-title&page=0&size=10&sort=title%2Cdesc")) {
                    return new MockResponse().setBody(searchResultMock).setResponseCode(200).setHeader("Content-Type", "application/hal+json");
                } else if (request.getPath().contains("mods")) {
                    return new MockResponse().setBody(TestMods.aDefaultBookModsXml())
                            .setResponseCode(200)
                            .setHeader("Content-Type", "application/xml");
                } else if (request.getPath().contains("fields")) {
                    return new MockResponse().setBody(TestFields.aDefaultBookJson())
                            .setResponseCode(200)
                            .setHeader("Content-Type", "application/json");
                } else if (request.getPath().startsWith("/search?q=sesamid%3Aid")) {
                    return new MockResponse().setBody(searchResource1)
                            .setResponseCode(200)
                            .setHeader("Content-Type", "application/json");
                } else if (request.getPath().equals("/search?q=*&page=0&size=10&aggs=ddc1%2Cmediatype")) {
                    return new MockResponse().setBody(searchResultMockWithAggragations).setResponseCode(200).setHeader("Content-Type", "application/hal+json");
                }

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
        HttpHeaders headers = new HttpHeaders();
        headers.add(UserUtils.SSO_HEADER, "token");
        headers.add(UserUtils.REAL_IP_HEADER, "123.45.100.1");

        ResponseEntity<SearchResource> entity = new TestRestTemplate().exchange(
                "http://localhost:" + port + "/catalog/items?q=Ola&fields=-title&size=10&sort=title,desc", HttpMethod.GET,
                new HttpEntity<Void>(headers), SearchResource.class);

        assertTrue("Status code should be 200 ", entity.getStatusCode().is2xxSuccessful());
        assertNotNull("Response should have page element", entity.getBody().getMetadata());
        assertNotNull("Response should have _embedded element", entity.getBody().getEmbedded());
        assertNotNull("Response should have links", entity.getBody().getLinks());
        assertEquals("It should be 4 elements in items array", 4, entity.getBody().getEmbedded().getItems().size());
    }

    @Test
    public void testSearchWithAggregations() throws Exception{
        HttpHeaders headers = new HttpHeaders();
        headers.add(UserUtils.SSO_HEADER, "token");
        headers.add(UserUtils.REAL_IP_HEADER, "123.45.100.1");
        String url = "http://localhost:" + port + "/catalog/items?q=*&aggs=ddc1,mediatype";
        ResponseEntity<ItemSearchResource> entity = new TestRestTemplate().exchange(url, HttpMethod.GET, new HttpEntity<Void>(headers), ItemSearchResource.class);

        assertTrue("Status code should be 200 ", entity.getStatusCode().is2xxSuccessful());
        assertNotNull("Response should contain aggregations", entity.getBody().getEmbedded().getAggregations());
        assertEquals("Should contain 2 aggragations", 2, entity.getBody().getEmbedded().getAggregations().size());
    }

    @After
    public void tearDown() throws IOException {
        server.shutdown();
        RequestContextHolder.resetRequestAttributes();
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(UserUtils.SSO_HEADER, "token");
        headers.add(UserUtils.REAL_IP_HEADER, "123.45.100.1");
        return headers;
    }
}

@Configuration
class TestNiConfig2 {

    @Bean
    public NiClient getNiClient() throws Exception {
        return new NiClient(SearchControllerIT.TEST_SERVER_ADDR);
    }
}

