package no.nb.microservices.catalogitem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import no.nb.microservices.catalogitem.rest.model.ItemResource;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
@SpringApplicationConfiguration(classes = {Application.class, RibbonClientConfiguration.class})
@WebIntegrationTest("server.port: 0")
public class ItemControllerIT {

    @Value("${local.server.port}")
    int port;
    
    RestTemplate template = new TestRestTemplate();

    @Autowired
    ILoadBalancer lb;
    
	@Test
	public void testGetItem() throws Exception {

	    String modsMock = IOUtils.toString(this.getClass().getResourceAsStream("mods.xml"));
	    String fieldsMock = IOUtils.toString(this.getClass().getResourceAsStream("fields.json"));

	    MockWebServer server = new MockWebServer();
	    final Dispatcher dispatcher = new Dispatcher() {

	        @Override
	        public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
	            System.out.println(request.getPath());
	            if (request.getPath().equals("/id1/mods")){
	                return new MockResponse().setBody(modsMock).setResponseCode(200).setHeader("Content-Type", "application/xml");
	            } else if( request.getPath().equals("/id1/fields")){
                    return new MockResponse().setBody(fieldsMock).setResponseCode(200).setHeader("Content-Type", "application/json");
                }
	            
	            
	            return new MockResponse().setResponseCode(404);
	        }
	    };
	    server.setDispatcher(dispatcher);
	    server.start();
	    
	    BaseLoadBalancer blb = (BaseLoadBalancer) lb;
	    blb.setServersList(Arrays.asList(new Server(server.getHostName(), server.getPort())));
	    
	    ResponseEntity<ItemResource> result = template.getForEntity("http://localhost:" + port + "/item/id1", ItemResource.class);

	    assertTrue("Status code should be 200 ", result.getStatusCode().is2xxSuccessful());
	    assertNotNull("Response should have page element", result.getBody().getMetadata());
	    assertNotNull("Response should have links", result.getBody().getLinks());
        assertEquals("Title should be \"Født til klovn\"", "Født til klovn", result.getBody().getMetadata().getTitleInfo().getTitle());
        assertTrue("isDigital should be true", result.getBody().getAccessInfo().isDigital());
        assertTrue("isPublicDomain should be true", result.getBody().getAccessInfo().isPublicDomain());

	}

}

@Configuration
class RibbonClientConfiguration {

    @Bean
    public ILoadBalancer ribbonLoadBalancer() {
        return new BaseLoadBalancer();
    }
}
