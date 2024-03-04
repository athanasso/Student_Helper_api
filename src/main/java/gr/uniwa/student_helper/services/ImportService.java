package gr.uniwa.student_helper.services;

import gr.uniwa.student_helper.dto.StudentDTO;
import gr.uniwa.student_helper.model.Course;
import gr.uniwa.student_helper.model.FileCourse;
import gr.uniwa.student_helper.model.FileData;
import gr.uniwa.student_helper.model.Grades;
import gr.uniwa.student_helper.model.Info;
import gr.uniwa.student_helper.model.Student;
import gr.uniwa.student_helper.util.UtilFunctions;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ImportService class is responsible for importing student data from a file and generating a StudentDTO object.
 * It provides methods to process the file data and calculate various information such as grades, average grade, and needed courses.
 */
public class ImportService {
    
    private final Logger logger = LoggerFactory.getLogger(ScrapeService.class);

    public Response.ResponseBuilder getStudent(FileData fileData) {
        return getUniwaStudent(fileData);
    }

    private Response.ResponseBuilder getUniwaStudent(FileData fileData) {
        try{
            Student student = new Student();
            Info info = new Info();
            Grades grades = new Grades();
            ArrayList<FileCourse> temp = new ArrayList();
            ArrayList<Course> courses = new ArrayList();
            ArrayList<FileCourse> fileCourses = fileData.getCourses();
            String curriculum = fileData.getCurriculum();
            BigDecimal sum = new BigDecimal("0");
            int ects = 0;
                        
            info.setCurriculum(curriculum);
            info.setRegistrationYear(fileData.getRegistrationYear());
            info.setCurriculumCode(UtilFunctions.calculateCurriculumIdentifier(fileData.getCurriculum()));
            info.setFlow(fileData.getFlow());
            
            temp = UtilFunctions.calculateFileCourses(fileCourses, curriculum);

            for (FileCourse fileCourse : temp) {
                if ( Double.parseDouble(fileCourse.getGrade()) >= 5) {
                    sum = sum.add(BigDecimal.valueOf(Double.parseDouble(fileCourse.getGrade())).multiply(BigDecimal.valueOf(Double.parseDouble(fileCourse.getEcts()))) );
                    ects += Integer.parseInt(fileCourse.getEcts());
                    Course course = new Course(fileCourse.getId(), fileCourse.getName(), fileCourse.getGrade());
                    courses.add(course);
                }
            }

            if (!fileData.getRegistrationYear().equals("")){
                int deletionYear = UtilFunctions.calculateYearOfDeletion(curriculum, Integer.parseInt(fileData.getRegistrationYear()), false);
                info.setDeletionYear(Integer.toString(deletionYear - 1) + "-" + Integer.toString(deletionYear));
            }
            
            grades.setCourses(courses);
            grades.setTotalPassedCourses(String.valueOf(courses.size()));
            grades.setTotalEcts(String.valueOf(ects));
            
            if (ects != 0){
                BigDecimal totalAverageGrade = sum.divide(BigDecimal.valueOf(ects), 2, RoundingMode.HALF_UP);
                grades.setTotalAverageGrade(totalAverageGrade.toString());
            }
            
            grades.setNeededCourses(UtilFunctions.calculateNeededCourses(courses, curriculum, fileData.getFlow()));
            
            student.setInfo(info);
            student.setGrades(grades);

            if (student == null) {
                logger.warn("Internal Server Error");
                return Response.status(500);
            }

            StudentDTO studentDTO = new StudentDTO(student);
            logger.debug("student read");
            
            return Response.ok(studentDTO);
        } catch (NumberFormatException e) {
            logger.error("Invalid number format", e);
            return Response.status(400);
        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return Response.status(500);
        }
    }
}
