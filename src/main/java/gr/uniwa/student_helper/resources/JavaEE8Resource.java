package gr.uniwa.student_helper.resources;


import gr.uniwa.student_helper.model.LoginForm;
import gr.uniwa.student_helper.services.ScrapeService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("student")
public class JavaEE8Resource {

    @Inject
    ScrapeService scrapeService;

    private final Logger logger = LoggerFactory.getLogger(JavaEE8Resource.class);

    @Produces("application/json")
    @Consumes("application/json")
    @POST
    public Response getStudent(LoginForm loginForm) {
        try {
            ResponseBuilder responseBuilder = scrapeService.getStudent("uniwa", loginForm);
            return responseBuilder.build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.status(400).build();
        }
    }
}
