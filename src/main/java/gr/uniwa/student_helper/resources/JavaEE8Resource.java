package gr.uniwa.student_helper.resources;

import gr.uniwa.student_helper.dto.StudentDTO;
import gr.uniwa.student_helper.model.LoginForm;
import gr.uniwa.student_helper.services.ScrapeService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("student")
public class JavaEE8Resource {
    
    @Inject
    ScrapeService scrapeService;
    
    @Produces("application/json")
    @GET
    public StudentDTO getStudent(LoginForm loginForm){
        return scrapeService.getStudent("uniwa", loginForm);
    }
}
