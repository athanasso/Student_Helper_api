package gr.uniwa.student_helper.services;

import gr.uniwa.student_helper.dto.RestApiResult;
import gr.uniwa.student_helper.dto.StudentDTO;
import gr.uniwa.student_helper.model.LoginForm;
import gr.uniwa.student_helper.model.Student;
import gr.uniwa.student_helper.parser.Parser;
import gr.uniwa.student_helper.scraper.Scraper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScrapeService {
    
    private final Logger logger = LoggerFactory.getLogger(ScrapeService.class);

    public RestApiResult getStudent(String university, LoginForm loginForm) {
        return getUniwaStudent(loginForm, university, null, "services.uniwa.gr");
    }

    private RestApiResult getUniwaStudent(LoginForm loginForm, String university, String system, String domain) {
        try{
            // scrap info page
            Scraper scraper = new Scraper(loginForm, university, system, domain);

            // check for connection errors
            if (!scraper.isConnected()) {
                logger.warn("scraper isn't connected");
                return new RestApiResult<>(null,408,"Request Timeout");
            }

            // authorization check
            if (!scraper.isAuthorized()) {
                logger.warn("scraper isn't authorized");
                return new RestApiResult<>(null,401,"Unauthorized");
            }

            String infoJSON = scraper.getInfoJSON();
            String gradesJSON = scraper.getGradesJSON();
            String totalAverageGrade = scraper.getTotalAverageGrade();

            // check for internal errors
            if (infoJSON == null || gradesJSON == null || totalAverageGrade == null) {
                logger.warn("Internal Server Error");
                return new RestApiResult<>(null,500,"Internal Server Error"); 
            }

            Parser parser = new Parser(university, system);
            Student student = parser.parseInfoAndGradesJSON(infoJSON, gradesJSON, totalAverageGrade);

            if (student == null) {
                logger.warn("Internal Server Error");
                return new RestApiResult<>(null,500,"Internal Server Error");
            }

            StudentDTO studentDTO = new StudentDTO(scraper.getCookies(), student);

            return new RestApiResult<>(studentDTO,200,"Successful");
        } catch (Exception e){
            return new RestApiResult<>(null,400,"Bad Request");
        }
    }
}