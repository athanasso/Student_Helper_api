package gr.uniwa.student_helper.resources;

import gr.uniwa.student_helper.dto.RestApiResult;
import gr.uniwa.student_helper.model.LoginForm;
import gr.uniwa.student_helper.services.ScrapeService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("student")
public class JavaEE8Resource {

    @Inject
    ScrapeService scrapeService;
    
    Response response;

    private final Logger logger = LoggerFactory.getLogger(JavaEE8Resource.class);

    @Produces("application/json")
    @GET
    public RestApiResult getStudent(LoginForm loginForm) {
        try {
            RestApiResult result = scrapeService.getStudent("uniwa", loginForm);
            switch (response.getStatus()) {
                case 408 -> {
                    // handle status code 408
                    return new RestApiResult<>(null,408,"Request Timeout");
                }
                case 401 -> {
                    // handle status code 401
                    return new RestApiResult<>(null,401,"Unauthorized");
                }
                case 500 -> {
                    // handle status code 500
                    return new RestApiResult<>(null,500,"Internal Server Error"); 
                }
                case 400 -> {
                    return new RestApiResult<>(null,400,"Bad Request");
                }
                default -> {
                    return result;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new RestApiResult<>(null,400,"Bad Request");
        }
    }
}
