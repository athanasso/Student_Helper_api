package gr.uniwa.student_helper.resources;


import gr.uniwa.student_helper.model.FileData;
import gr.uniwa.student_helper.model.LoginForm;
import gr.uniwa.student_helper.services.ImportService;
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

/**
 * Represents a resource for handling student-related operations in a Java EE 8 application.
 */
@Path("student")
public class JavaEE8Resource {

    @Inject
    ScrapeService scrapeService;
     
    @Inject
    ImportService importService;

    private final Logger logger = LoggerFactory.getLogger(JavaEE8Resource.class);

    /**
     * Retrieves student information based on the provided login form.
     * 
     * @param loginForm The login form containing the necessary credentials.
     * @return A response containing the student information in JSON format.
     */
    @Path("login")
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

    /**
     * Imports student data from the provided file.
     * 
     * @param fileData The file containing the student data.
     * @return A response indicating the success or failure of the import operation.
     */
    @Path("import")
    @Produces("application/json")
    @Consumes("application/json")
    @POST
    public Response getStudent(FileData fileData) {
        try {
            ResponseBuilder responseBuilder = importService.getStudent(fileData);
            return responseBuilder.build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.status(400).build();
        }
    }
}
