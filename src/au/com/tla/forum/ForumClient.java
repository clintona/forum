package au.com.tla.forum;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * A stand alone Java application acting as a text based Jersey (JAX-RS) client, that exercises the
 * "Forum" RESTful web service.
 * <p/>
 * Callers can supply the web service context root URL as a command line argument, else the web
 * service URL defaults to http://localhost:8080/forum.
 * 
 * @author Clinton
 * 
 */
public class ForumClient {

    public static final String DEFAULT_BASE_URL = "http://localhost:8080/forum/rest";
    public static final String USER_ID = "me";

    private Client client;
    private WebTarget baseTarget;

    public ForumClient(String url) {
        client = ClientBuilder.newClient();
        baseTarget = client.target(url).path("questions");
    }

    public static void main(String[] args) {
        String url = args.length > 0 ? args[0] : DEFAULT_BASE_URL;
        ForumClient forumClient = new ForumClient(url);
        forumClient.run();
    }

    public void run() {
        URI questionUri = addQuestion("To be, or not to be?");
        getAllQuestions();
        getOneQuestion(questionUri);
        delay(1000); // allow 1s for lastModifiedTime stamp to alter
        editQuestion(questionUri, "That is the question", USER_ID);

        // attempt to edit a question when not the original owner
        editQuestion(questionUri, "this should fail", "hacker");

        // attempt to edit a non-existent question
        editQuestion(baseTarget.path("99999").getUri(), "That is the question", USER_ID);

        addAnswer(questionUri, "Whether 'tis nobler in the mind to suffer");

        getAnswers(questionUri);
    }

    public URI addQuestion(String text) {
        Invocation.Builder builder = this.baseTarget.request(MediaType.TEXT_PLAIN_TYPE);

        Form form = new Form();
        form.param("question", text);
        form.param("userId", USER_ID);

        Response response = builder.post(Entity.entity(form,
                MediaType.APPLICATION_FORM_URLENCODED_TYPE));

        System.out.println("addQuestion " + response.getStatus() + " " + response.getLocation());
        System.out.println(response.readEntity(String.class));
        return response.getLocation();
    }

    public void getAllQuestions() {
        Invocation.Builder builder = this.baseTarget.request(MediaType.TEXT_PLAIN_TYPE);

        Response response = builder.get();
        System.out.println();
        System.out.println("getAllQuestions " + response.getStatus());
        System.out.println(response.readEntity(String.class));
    }

    public void getOneQuestion(URI questionUri) {
        WebTarget target = this.client.target(questionUri);
        Invocation.Builder builder = target.request(MediaType.TEXT_PLAIN_TYPE);

        Response response = builder.get();

        System.out.println();
        System.out.println("getOneQuestion " + target.getUri() + " " + response.getStatus());
        System.out.println(response.readEntity(String.class));
    }

    public void editQuestion(URI questionUri, String text, String userId) {
        WebTarget target = this.client.target(questionUri);
        Invocation.Builder builder = target.request(MediaType.TEXT_PLAIN_TYPE);

        Form form = new Form();
        form.param("question", text);
        form.param("userId", userId);

        Response response = builder.put(Entity.entity(form,
                MediaType.APPLICATION_FORM_URLENCODED_TYPE));

        System.out.println();
        System.out.println("editQuestion " + response.getStatus());
        System.out.println(response.readEntity(String.class));

    }

    public void addAnswer(URI questionUri, String text) {
        WebTarget target = this.client.target(questionUri);
        Invocation.Builder builder = target.path("answers").request(MediaType.TEXT_PLAIN_TYPE);

        Form form = new Form();
        form.param("answer", text);
        form.param("userId", USER_ID);

        Response response = builder.post(Entity.entity(form,
                MediaType.APPLICATION_FORM_URLENCODED_TYPE));

        System.out.println();
        System.out.println("addAnswer " + response.getStatus() + " " + response.getLocation());
        System.out.println(response.readEntity(String.class));

    }

    public void getAnswers(URI questionUri) {
        WebTarget target = this.client.target(questionUri);
        Invocation.Builder builder = target.path("answers").request(MediaType.TEXT_PLAIN_TYPE);

        Response response = builder.get();
        System.out.println();
        System.out.println("getAnswers " + response.getStatus());
        System.out.println(response.readEntity(String.class));
    }

    /**
     * Sleep the current thread for the given no. of millis
     * 
     * @param milliseconds
     */
    private void delay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
