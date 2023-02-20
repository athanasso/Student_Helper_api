package gr.uniwa.student_helper.resources;

import gr.uniwa.student_helper.dto.RestApiResult;
import gr.uniwa.student_helper.model.LoginForm;
import gr.uniwa.student_helper.services.ScrapeService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("student")
public class JavaEE8Resource {

    @Inject
    ScrapeService scrapeService;

    private final Logger logger = LoggerFactory.getLogger(JavaEE8Resource.class);

    @Produces("application/json")
    @GET
    public RestApiResult getStudent(LoginForm loginForm) {
        try {
            return scrapeService.getStudent("uniwa", loginForm);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new RestApiResult<>(new ArrayList<>(),400,"Bad Request");
        }
    }
}
