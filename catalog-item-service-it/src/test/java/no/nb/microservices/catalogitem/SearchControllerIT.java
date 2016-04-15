package no.nb.microservices.catalogitem;

import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import no.nb.commons.web.util.UserUtils;
import no.nb.microservices.catalogitem.rest.model.SuperItemSearchResource;
import no.nb.sesam.ni.niclient.NiClient;
import no.nb.sesam.ni.niserver.NiServer;
import org.apache.commons.io.IOUtils;
import org.junit.*;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { TestConfig.class, RibbonClientConfiguration.class, TestNiConfig2.class })
@WebIntegrationTest("server.port: 0")
public class SearchControllerIT {
    public static String TEST_SERVER_ADDR;
    private static int TEST_SERVER_PORT;
    private static NiServer niServer;
    Logger logger = LoggerFactory.getLogger(SearchControllerIT.class);
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
        String searchResultMock = IOUtils.toString(this.getClass().getResourceAsStream("catalog-search-index-service.json"));
        String searchResultMockWithAggragations = IOUtils.toString(this.getClass().getResourceAsStream("catalog-search-index-service-aggregations.json"));
        String contentSearchResultMock = IOUtils.toString(this.getClass().getResourceAsStream("catalog-iiif-content-service.json"));

        server = new MockWebServer();
        final Dispatcher dispatcher = new Dispatcher() {

            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                System.out.println("REQUEST: " + request.getPath());

                if (request.getPath().equals("/catalog/v1/search?q=*&page=0&size=1&grouping=false&aggs=mediatype%3A20&explain=false")) {
                    return new MockResponse().setBody(searchResultMockWithAggragations).setResponseCode(200).setHeader("Content-Type", "application/hal+json");
                } else if (request.getPath().startsWith("/catalog/v1/search?q=*&page=0&size=10&grouping=false&explain=false&filter=mediatype%3A")) {
                    return new MockResponse().setBody(searchResultMock).setResponseCode(200).setHeader("Content-Type", "application/hal+json");
                } else if(request.getPath().startsWith("/catalog/v1/contentsearch/")) {
                    return new MockResponse().setBody(contentSearchResultMock).setResponseCode(200).setHeader("Content-Type", "application/hal+json");
                } else if (request.getPath().equals("/catalog/v1/search?q=*+AND+%28mediatype%3Ab%C3%B8ker+OR+mediatype%3Aaviser%29&page=0&size=10&grouping=false&explain=false")) {
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
    public void testSuperSearch() throws Exception {
        String url = "http://localhost:" + port + "/catalog/v1/search?q=*";
        ResponseEntity<SuperItemSearchResource> entity = getEntity(url, SuperItemSearchResource.class);

        assertThat(entity.getStatusCode().value(), is(200));
        assertThat(entity.getBody().getId().getHref(), is(url));
        assertThat(entity.getBody().getEmbedded().getBooks().getEmbedded().getItems(), hasSize(4));
    }

    @Test
    public void whenSuperSearchWithMediaTypesThenReturnOtherMediaType() throws Exception {
        String url = "http://localhost:" + port + "/catalog/v1/search?q=*&mediaTypes=radio";
        ResponseEntity<SuperItemSearchResource> entity = getEntity(url, SuperItemSearchResource.class);

        assertThat(entity.getStatusCode().value(), is(200));
        assertThat(entity.getBody().getId().getHref(), is(url));
        assertThat(entity.getBody().getEmbedded().getOthers().getEmbedded().getItems(), hasSize(4));
    }

    @Test
    public void whenSuperSearchThenReturnTextAroundSearchString() throws Exception {
        String url = "http://localhost:" + port + "/catalog/v1/search?q=*";
        ResponseEntity<SuperItemSearchResource> entity = getEntity(url, SuperItemSearchResource.class);

        assertThat(entity.getStatusCode().value(), is(200));
        assertThat(entity.getBody().getId().getHref(), is(url));
        assertThat(entity.getBody().getEmbedded().getNewspapers().getEmbedded().getContentSearch(), hasSize(4));
    }

    private <T> ResponseEntity<T> getEntity(String url, Class<T> type) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(UserUtils.SSO_HEADER, "token");
        headers.add(UserUtils.REAL_IP_HEADER, "123.45.100.1");
        return new TestRestTemplate().exchange(url, HttpMethod.GET, new HttpEntity<Void>(headers), type);
    }

    @After
    public void tearDown() throws IOException {
        server.shutdown();
        RequestContextHolder.resetRequestAttributes();
    }
}

@Configuration
class TestNiConfig2 {

    @Bean
    public NiClient getNiClient() throws Exception {
        return new NiClient(SearchControllerIT.TEST_SERVER_ADDR);
    }
}
