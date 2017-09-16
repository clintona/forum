package au.com.tla.forum;

import static org.junit.Assert.assertEquals;

import java.net.InetSocketAddress;
import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.RuntimeDelegate;

import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jersey.test.framework.JerseyTest;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class ForumServiceTest extends JerseyTest {

    private static Client client;

    // @Override
    // protected AppDescriptor configure() {
    //
    // return new ResourceConfig(ForumService.class);
    //
    // // enable(TestProperties.LOG_TRAFFIC);
    // // enable(TestProperties.DUMP_ENTITY);
    //
    // // return cfg.getApplication();
    //
    // }

    @Before
    public void setUp() throws Exception {
    }

    @BeforeClass
    public static void init() throws Exception {

        URI uri = UriBuilder.fromUri("http://localhost").port(8080).build();
        HttpServer server = HttpServer.create(new InetSocketAddress(uri.getPort()), 0);
        // Create a handler wrapping the JAX-RS application
        ResourceConfig cfg = new ResourceConfig(ForumService.class);
        HttpHandler handler = RuntimeDelegate.getInstance().createEndpoint(cfg, HttpHandler.class);
        // Map JAX-RS handler to the server root
        server.createContext(uri.getPath(), handler);
        // Start the server
        server.start();

        // create a client for testing
        client = ClientBuilder.newClient();
    }

    @Test
    public void test() {
        assertEquals(200, client.target("http://localhost:8080/forum/rest/questions").request()
                .get().getStatus());
    }

}
