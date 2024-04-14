package gr.uniwa.student_helper.services;

import gr.uniwa.student_helper.dto.StudentDTO;
import gr.uniwa.student_helper.model.LoginForm;
import gr.uniwa.student_helper.model.Student;
import gr.uniwa.student_helper.parser.Parser;
import gr.uniwa.student_helper.scraper.Scraper;
import jakarta.ws.rs.core.Response;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The LoginService class is responsible for scraping student information from a specific university's website via login username and password.
 * It provides methods to retrieve student data and handle any errors that may occur during the scraping process.
 */
public class LoginService {
    
    private final Logger logger = LoggerFactory.getLogger(LoginService.class);

    public Response.ResponseBuilder getStudent(String university, LoginForm loginForm) {
        return getUniwaStudent(loginForm, university, null, "services.uniwa.gr");
    }

    private Response.ResponseBuilder getUniwaStudent(LoginForm loginForm, String university, String system, String domain) {
        try{
            // scrap info page
            Scraper scraper = new Scraper(loginForm, university, system, domain);

            // check for connection errors
            if (!scraper.isConnected()) {
                logger.warn("scraper isn't connected");
                return Response.status(408);
            }

            // authorization check
            if (!scraper.isAuthorized()) {
                logger.warn("scraper isn't authorized");
                return Response.status(401);
            }

            String infoJSON = scraper.getInfoJSON();
            String gradesJSON = scraper.getGradesJSON();
            String thesisJson = scraper.getThesisJSON();
            String totalAverageGrade = scraper.getTotalAverageGrade();
            Map<String, String> cookies = scraper.getCookies();

            // check for internal errors
            if (infoJSON == null || gradesJSON == null || totalAverageGrade == null) {
                logger.warn("Internal Server Error");
                return Response.status(500);
            }

            Parser parser = new Parser(university, system);
            Student student = parser.parseInfoAndGradesJSON(infoJSON, gradesJSON, thesisJson, totalAverageGrade);

            if (student == null) {
                logger.warn("Internal Server Error");
                return Response.status(500);
            }

            StudentDTO studentDTO = new StudentDTO(student, cookies);
            logger.debug("student read");
            
            return Response.ok(studentDTO);
        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return Response.status(400);
        }
    }
}