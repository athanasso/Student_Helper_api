package gr.uniwa.student_helper.resources;


import gr.uniwa.student_helper.model.FileData;
import gr.uniwa.student_helper.model.LoginForm;
import gr.uniwa.student_helper.services.ImportService;
import gr.uniwa.student_helper.services.LoginService;
import gr.uniwa.student_helper.services.RephreshService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import java.util.Base64;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a resource for handling student-related operations in a Java EE 8 application.
 */
@Path("student")
public class JavaEE8Resource {

    @Inject
    LoginService loginService;
     
    @Inject
    ImportService importService;
    
    @Inject
    RephreshService rephreshService;

    private final Logger logger = LoggerFactory.getLogger(JavaEE8Resource.class);

    /**
     * Retrieves student information based on the provided login form.
     * 
     * @param loginForm The login form containing the necessary credentials.
     * @param headers
     * @return A response containing the student information in JSON format.
     */
    @Path("login")
    @Produces("application/json")
    @Consumes("application/json")
    @POST
    public Response getStudent(LoginForm loginForm, @Context HttpHeaders headers) {
        try {
            // Get the Authorization header
            String authorizationHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);

            if(authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
                // Remove the "Basic " prefix
                String credentialsBase64 = authorizationHeader.substring(6);
                // Decode the Base64 encoded string
                String credentials = new String(Base64.getDecoder().decode(credentialsBase64));
                // Split the username and password
                String[] parts = credentials.split(":", 2);
                String username = parts[0];
                String password = parts[1];

                ResponseBuilder responseBuilder = loginService.getStudent("uniwa", loginForm);
                return responseBuilder.build();
            } else {
                // Handle unauthorized request
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
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
    
    /**
     * Retrieves student information based on cookies.
     * 
     * @return A response containing the student information in JSON format.
     */
    @Path("rephresh")
    @Produces("application/json")
    @Consumes("application/json")
    @POST
    public Response getStudent(Map<String, String> cookies) {
        try {
            if(!cookies.isEmpty()) {
                
                ResponseBuilder responseBuilder = rephreshService.getStudent("uniwa", cookies);
                return responseBuilder.build();
            } else {
                // Handle unauthorized request
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.status(400).build();
        }
    }
}
