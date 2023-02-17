package gr.uniwa.student_helper.services;

import gr.uniwa.student_helper.dto.StudentDTO;
import gr.uniwa.student_helper.model.LoginForm;
import gr.uniwa.student_helper.model.Student;
import gr.uniwa.student_helper.parser.Parser;
import gr.uniwa.student_helper.scraper.Scraper;
import jakarta.ws.rs.core.Response;

public class ScrapeService {

    public StudentDTO getStudent(String university, LoginForm loginForm) {
        return getUniwaStudent(loginForm, university, null, "services.uniwa.gr"); 
    }

    private StudentDTO getUniwaStudent(LoginForm loginForm, String university, String system, String domain) {
        // scrap info page
        Scraper scraper = new Scraper(loginForm, university, system, domain);

        // check for connection errors
        if (!scraper.isConnected()) {
            Response.status(408).build();
            return null;
        }

        // authorization check
        if (!scraper.isAuthorized()) {
            Response.status(401).build();
            return null;
        }

        String infoJSON = scraper.getInfoJSON();
        String gradesJSON = scraper.getGradesJSON();
        String totalAverageGrade = scraper.getTotalAverageGrade();

        // check for internal errors
        if (infoJSON == null || gradesJSON == null || totalAverageGrade == null) {
            Response.status(500).build();
            return null;  
        }

        Parser parser = new Parser(university, system);
        Student student = parser.parseInfoAndGradesJSON(infoJSON, gradesJSON, totalAverageGrade);

        if (student == null) {
            Response.status(500).build();
            return null;
        }

        StudentDTO studentDTO = new StudentDTO(system, scraper.getCookies(), student);

        Response.status(200).build();
        return studentDTO; 
    }
}