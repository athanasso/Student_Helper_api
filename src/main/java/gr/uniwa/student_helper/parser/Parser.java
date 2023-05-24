package gr.uniwa.student_helper.parser;

import gr.uniwa.student_helper.model.Info;
import gr.uniwa.student_helper.model.Course;
import gr.uniwa.student_helper.model.Grades;
import gr.uniwa.student_helper.model.Student;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.uniwa.student_helper.util.UtilFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Parser {

    private Exception exception;
    private String document;
    private final String PRE_LOG;
    private final Logger logger = LoggerFactory.getLogger(Parser.class);
    private String curriculum;

    public Parser(String university, String system) {
        this.PRE_LOG = university + (system == null ? "" : "." + system);
    }

    private Info parseInfoJSON(String infoJSON) {
        Info info = new Info();

        try {
            JsonNode student = new ObjectMapper().readTree(infoJSON);

            String am = student.get("studentNo").asText();
            info.setAm(am);

            String firstName = student.get("firstName").asText();
            info.setFirstName(firstName);

            String lastName = student.get("lastName").asText();
            info.setLastName(lastName);

            String department = student.get("departmentTitle").asText();
            info.setDepartment(department);

            String registrationYear = student.get("insSyllabus").asText();
            info.setRegistrationYear(registrationYear);

            String curriculum = student.get("programTitle").asText();
            info.setCurriculum(curriculum);
            this.setCurriculum(curriculum);
            
            boolean isPartTime = student.get("isPartTime").asBoolean();

            int currentSemester = student.get("lastSemester").asInt();
            info.setCurrentSemester((currentSemester == 0) ? "1" : String.valueOf(currentSemester));

            int deletionYear = UtilFunctions.calculateYearOfDeletion(curriculum, Integer.parseInt(registrationYear), isPartTime);
            info.setDeletionYear(Integer.toString(deletionYear - 1) + "-" + Integer.toString(deletionYear));

            return info;
        } catch (IOException e) {
            logger.error("[" + PRE_LOG + "] Error: {}", e.getMessage(), e);
            setException(e);
            setDocument(infoJSON);
            return null;
        }
    }

    private Grades parseGradesJSON(String gradesJSON, String totalAverageGrade) {
        Grades grades = new Grades();
        ArrayList<Course> courses = new ArrayList<>();
        DecimalFormat df2 = new DecimalFormat("#.##");

        double totalEcts = 0;
        int count = 0;
        try {
            JsonNode node = new ObjectMapper().readTree(gradesJSON);
            JsonNode studentCourses = node.get("studentCourses");
            if (studentCourses == null) {
                studentCourses = node;
            }

            if (studentCourses.size() == 0) {
                grades.setTotalAverageGrade("-");
                grades.setTotalPassedCourses("0");
                grades.setTotalEcts("0");
                grades.setCourses(new ArrayList<>());
                return grades;
            }

            for (JsonNode courseJSON : studentCourses) {
                Course course = new Course();
                String id = courseJSON.get("courseCode").asText();
                course.setId(id);

                String name = courseJSON.get("title").asText();
                course.setName(name);
                double grade = 0;
                if ((courseJSON.get("grade").asDouble() * 10) >= 5) {
                    grade = courseJSON.get("grade").asDouble() * 10;
                    course.setGrade(df2.format(grade));
                    courses.add(course);
                }

                String s = courseJSON.get("idFather").asText();
                if (s.equals("null")) {
                    boolean diploma = courseJSON.get("isCountInDiploma").asBoolean();
                    if (diploma) {
                        String studentGradesId = courseJSON.get("studentGradesId").asText();
                        if (!studentGradesId.equals("null")) {
                            count++;
                            double ects = courseJSON.get("ects").asDouble();
                            totalEcts += ects;
                        }
                    }
                }
            }

            grades.setTotalEcts(String.valueOf(Math.ceil(totalEcts)).replace(".0", "").replace(",0", ""));
            grades.setTotalAverageGrade(totalAverageGrade);
            grades.setTotalPassedCourses(String.valueOf(count));
            grades.setCourses(UtilFunctions.calculateCourses(courses, this.getCurriculum()));
            grades.setNeededCourses(UtilFunctions.calculateNeededCourses(grades.getCourses(), this.getCurriculum()));
            
            return grades;
        } catch (IOException e) {
            logger.error("[" + PRE_LOG + "] Error: {}", e.getMessage(), e);
            setException(e);
            setDocument(gradesJSON);
            return null;
        }
    }

    public Student parseInfoAndGradesJSON(String infoJSON, String gradesJSON, String totalAverageGrade) {
        Student student = new Student();

        try {
            Info info = parseInfoJSON(infoJSON);
            Grades grades = parseGradesJSON(gradesJSON, totalAverageGrade);

            if (info == null || grades == null) {
                return null;
            }

            student.setInfo(info);
            student.setGrades(grades);

            return student;
        } catch (Exception e) {
            logger.error("[" + PRE_LOG + "] Error: {}", e.getMessage(), e);
            setException(e);
            setDocument(gradesJSON + "\n\n======\n\n" + totalAverageGrade);
            return null;
        }
    }

    private void setCurriculum(String curriculum) {
        this.curriculum = curriculum;
    }

    private String getCurriculum() {
        return this.curriculum;
    }

    private void setDocument(String document) {
        this.document = document;
    }

    public String getDocument() {
        return this.document;
    }

    private void setException(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }
}
