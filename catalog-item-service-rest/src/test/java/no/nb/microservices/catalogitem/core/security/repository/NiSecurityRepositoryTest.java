package no.nb.microservices.catalogitem.core.security.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import no.nb.commons.web.util.UserUtils;
import no.nb.sesam.ni.niclient.NiClient;
import no.nb.sesam.ni.niserver.AuthorisationHandler;
import no.nb.sesam.ni.niserver.AuthorisationHandlerResolver;
import no.nb.sesam.ni.niserver.AuthorisationRequest;
import no.nb.sesam.ni.niserver.NiServer;
import no.nb.sesam.ni.niserver.authorisation.AcceptHandler;
import no.nb.sesam.ni.niserver.authorisation.DenyHandler;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.SocketUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class NiSecurityRepositoryTest {

    private static int TEST_SERVER_PORT;
    private static String TEST_SERVER_ADDR;

    private static NiServer server;

    @BeforeClass
    public static void setUpClass() throws Exception {
        TEST_SERVER_PORT = SocketUtils.findAvailableTcpPort();
        TEST_SERVER_ADDR = "localhost:" + TEST_SERVER_PORT;

        server = new NiServer(TEST_SERVER_PORT,
                new no.nb.sesam.ni.niserver.Cluster(TEST_SERVER_ADDR),
                new MockAuthorisationHandlerResolver(),
                null, null);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        server.shutdown(500);
    }

    @Test
    public void whenUserHasAccessThenReturnTrue() throws Exception {
        
        NiClient niClient = new NiClient(TEST_SERVER_ADDR);
        NiSecurityRepository niSecurityRepository = new NiSecurityRepository(niClient);
        assertTrue(niSecurityRepository.hasAccess("URN:NBN:no-nb_accept", "123.45.123.123", "amsso1"));
    }

    @Test
    public void whenUserIsDeniedAccessThenReturnFalse() throws Exception {
        
        NiClient niClient = new NiClient(TEST_SERVER_ADDR);
        NiSecurityRepository niSecurityRepository = new NiSecurityRepository(niClient);
        assertFalse(niSecurityRepository.hasAccess("URN:NBN:no-nb_deny", "123.45.123.123", "amsso1"));
    }

    static class MockAuthorisationHandlerResolver implements
            AuthorisationHandlerResolver {

        public AuthorisationHandler resolve(AuthorisationRequest request) {
            System.out.println(request.id);
            if (request.id != null
                    && request.id.startsWith("URN:NBN:no-nb_accept")) {
                return new AcceptHandler();
            } else if (request.id != null
                    && request.id.startsWith("URN:NBN:no-nb_deny")) {
                return new DenyHandler();
            } else {
                return new DenyHandler();
            }
        }

    }

}
