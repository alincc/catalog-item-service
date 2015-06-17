package no.nb.microservices.catalogitem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import no.nb.commons.web.util.UserUtils;
import no.nb.microservices.catalogitem.config.NiSettings;
import no.nb.microservices.catalogitem.core.item.model.AccessInfo;
import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.sesam.ni.niclient.NiClient;
import no.nb.sesam.ni.niserver.AuthorisationHandler;
import no.nb.sesam.ni.niserver.AuthorisationHandlerResolver;
import no.nb.sesam.ni.niserver.AuthorisationRequest;
import no.nb.sesam.ni.niserver.NiServer;
import no.nb.sesam.ni.niserver.authorisation.AcceptHandler;

import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.SocketUtils;
import org.springframework.web.client.RestTemplate;

import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

/**
 * 
 * @author ronnymikalsen
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { TestConfig.class,
        RibbonClientConfiguration.class })
@WebIntegrationTest("server.port: 0")
public class ItemControllerIT {

    @Value("${local.server.port}")
    int port;

    RestTemplate template = new TestRestTemplate();

    @Autowired
    ILoadBalancer lb;

    private static int TEST_SERVER_PORT;
    public static String TEST_SERVER_ADDR;

    private static NiServer server;

    @BeforeClass
    public static void setUpClass() throws Exception {
        TEST_SERVER_PORT = SocketUtils.findAvailableTcpPort();
        TEST_SERVER_ADDR = "localhost:" + TEST_SERVER_PORT;

        server = new NiServer(TEST_SERVER_PORT,
                new no.nb.sesam.ni.niserver.Cluster(TEST_SERVER_ADDR),
                new MockAuthorisationHandlerResolver(), null, null);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        server.shutdown(500);
    }

    @Test
    public void testGetItem() throws Exception {

        String modsMock = IOUtils.toString(this.getClass().getResourceAsStream(
                "mods.xml"));
        String fieldsMock = IOUtils.toString(this.getClass()
                .getResourceAsStream("fields.json"));

        MockWebServer server = new MockWebServer();
        final Dispatcher dispatcher = new Dispatcher() {

            @Override
            public MockResponse dispatch(RecordedRequest request)
                    throws InterruptedException {
                System.out.println(request.getPath());
                if (request.getPath().equals("/id1/mods")) {
                    return new MockResponse().setBody(modsMock)
                            .setResponseCode(200)
                            .setHeader("Content-Type", "application/xml");
                } else if (request.getPath().equals("/id1/fields")) {
                    return new MockResponse().setBody(fieldsMock)
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

        HttpHeaders headers = new HttpHeaders();
        headers.add(UserUtils.SSO_HEADER, "token");
        headers.add(UserUtils.REAL_IP_HEADER, "123.45.100.1");
        
        ResponseEntity<ItemResource> entity = new TestRestTemplate().exchange(
                "http://localhost:" + port + "/id1", HttpMethod.GET,
                new HttpEntity<Void>(headers), ItemResource.class);
        
        assertTrue("Status code should be 200 ", entity.getStatusCode()
                .is2xxSuccessful());
        assertNotNull("Response should have page element", entity.getBody()
                .getMetadata());
        assertNotNull("Response should have links", entity.getBody().getLinks());
        assertEquals("Title should be \"Født til klovn\"", "Født til klovn",
                entity.getBody().getMetadata().getTitleInfo().getTitle());
        assertTrue("isDigital should be true", entity.getBody().getAccessInfo()
                .isDigital());
        assertTrue("isPublicDomain should be true", entity.getBody()
                .getAccessInfo().isPublicDomain());
        assertEquals("Viewability should be ALL", AccessInfo.VIEWABILITY_ALL, entity.getBody()
                .getAccessInfo().getViewability());

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
