package au.com.tla.forum;

import java.net.URI;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

// URL is comprised of /display-name/url-pattern/path
@Singleton
@Path("/questions")
public class ForumService {

    QuestionManager questionManager;

    public ForumService() {
        System.out.println("ForumServiceTest initialising.");

        // create a default question manager if none injected
        questionManager = new QuestionManager();

        // create a dummy question
        questionManager
                .addQuestion("What is the answer to Life, the Universe and Everything?", "1");

    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addQuestion(@FormParam("question") String text,
            @FormParam("userId") String userId, @Context UriInfo uriInfo) {

        Question question = questionManager.addQuestion(text, userId);
        System.out.println("Created " + question);
        // By convention, return the new resource URI
        URI newQuestionUri = uriInfo.getAbsolutePathBuilder().path(question.getId()).build();
        return Response.created(newQuestionUri).entity(question.toString()).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getAllQuestions() {
        StringBuilder buf = new StringBuilder();
        for (Question question : questionManager.getQuestions()) {
            buf.append(question.toString()).append("\n");
        }

        return Response.ok().entity(buf.toString()).build();
    }

    // TODO: requires authorisation
    @PUT
    @Path("/{questionId}")
    public Response updateQuestion(@PathParam("questionId") String questionId,
            @FormParam("question") String text, @FormParam("userId") String userId) {

        // fetch the question to update
        Question question = questionManager.getQuestion(questionId);
        if (question == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        // Only the owner of the question can update
        if (!question.isOwner(userId)) {
            return Response.status(Status.UNAUTHORIZED).build();
        }

        question.setText(text); // auto sets lastModified
        return Response.ok().entity(question.toString()).build();
    }

    @GET
    @Path("/{questionId}")
    public Response getQuestion(@PathParam("questionId") String questionId) {
        // fetch the question to update
        Question question = questionManager.getQuestion(questionId);
        if (question == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok(question.toString()).build();
    }

    @POST
    @Path("/{questionId}/answers")
    public Response addAnswer(@PathParam("questionId") String questionId,
            @FormParam("answer") String text, @FormParam("userId") String userId,
            @Context UriInfo uriInfo) {

        // fetch the question to update
        Question question = questionManager.getQuestion(questionId);
        if (question == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        // validate the user
        if (!isAuthorised(userId)) {
            return Response.status(Status.UNAUTHORIZED).build();
        }
        // TODO: validate the answer?

        Answer answer = AnswerFactory.createAnswer(text, userId);
        question.addAnswer(answer);

        // By convention, return the new resource URI
        URI newAnswerUri = uriInfo.getAbsolutePathBuilder().path(answer.getId()).build();
        return Response.created(newAnswerUri).entity(answer.toString()).build();
    }

    @GET
    @Path("/{questionId}/answers")
    public Response getAnswers(@PathParam("questionId") String questionId) {
        // fetch the question
        Question question = questionManager.getQuestion(questionId);
        if (question == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        return Response.ok().entity(question.getAnswersAsString()).build();
    }

    // // This method is called if XML is request
    // @GET
    // @Produces(MediaType.TEXT_XML)
    // public String sayXMLHello() {
    // return "<?xml version=\"1.0\"?>" + "<hello> Hello Forum" + "</hello>";
    // }

    // This method is called if HTML is request
    // @GET
    // @Produces(MediaType.TEXT_HTML)
    // public String sayHtmlHello() {
    // return "<html> " + "<title>" + "Hello Forum" + "</title>" + "<body><h1>" + "Hello Jersey"
    // + "</body></h1>" + "</html> ";
    // }

    public QuestionManager getQuestionManager() {
        return questionManager;
    }

    public void setQuestionManager(QuestionManager questionManager) {
        this.questionManager = questionManager;
    }

    /**
     * Placeholder for future proper authorisation. For now, users are authorised if the userId is
     * set.
     * 
     * @param userId
     *        user ID String
     * @return true if the user is authorised, else false
     */
    public boolean isAuthorised(String userId) {
        return userId != null && !userId.isEmpty();
    }
}
