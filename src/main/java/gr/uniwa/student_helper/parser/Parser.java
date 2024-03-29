package gr.uniwa.student_helper.parser;

import gr.uniwa.student_helper.model.Info;
import gr.uniwa.student_helper.model.Course;
import gr.uniwa.student_helper.model.Grades;
import gr.uniwa.student_helper.model.Student;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.uniwa.student_helper.model.Thesis;
import gr.uniwa.student_helper.util.UtilFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Parser class is responsible for parsing JSON data and extracting relevant information about a student.
 * It provides methods to parse student information, grades, and thesis data from JSON strings.
 * The parsed data is used to create a Student object that encapsulates the student's information, grades, and thesis details.
 */
public class Parser {

    private Exception exception;
    private String document;
    private final String PRE_LOG;
    private final Logger logger = LoggerFactory.getLogger(Parser.class);
    private String curriculum;
    private String flow;

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
            
            String flow = student.get("programSubjectTitle").asText();
            
            info.setCurriculum(curriculum);
            this.setCurriculum(curriculum);
            info.setFlow(flow);
            this.setFlow(flow);
            
            info.setCurriculumCode(UtilFunctions.calculateCurriculumIdentifier(curriculum));

            boolean isPartTime = student.get("isPartTime").asBoolean();

            int currentSemester = student.get("lastSemester").asInt();
            info.setCurrentSemester((currentSemester == 0) ? "1" : String.valueOf(currentSemester));

            int deletionYear = UtilFunctions.calculateYearOfDeletion(this.getCurriculum(), Integer.parseInt(registrationYear), isPartTime);
            info.setDeletionYear(Integer.toString(deletionYear - 1) + "-" + Integer.toString(deletionYear));

            return info;
        } catch (IOException e) {
            logger.error("[" + PRE_LOG + "] Error: {}", e.getMessage(), e);
            setException(e);
            setDocument(infoJSON);
            return null;
        }
    }

    /**
     * Represents the grades of a student.
     */
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

            Pattern greekPattern = Pattern.compile("[Α-Ωα-ω]");//greek letters

            for (JsonNode courseJSON : studentCourses) {
                Course course = new Course();
                String id = courseJSON.get("courseCode").asText();
                Matcher matcher = greekPattern.matcher(id);
                if (matcher.find()) {
                    id = id.replace("Ν", "N").replace("Ρ", "P");//replace necessary greek letters
                }
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
            grades.setNeededCourses(UtilFunctions.calculateNeededCourses(grades.getCourses(), this.getCurriculum(), this.getFlow()));
            
            return grades;
        } catch (IOException e) {
            logger.error("[" + PRE_LOG + "] Error: {}", e.getMessage(), e);
            setException(e);
            setDocument(gradesJSON);
            return null;
        }
    }

    /**
     * Represents a thesis.
     */
    private Thesis parseThesissJSON(String thesisJSON) {
        Thesis thesis = new Thesis();
        try {
            JsonNode node = new ObjectMapper().readTree(thesisJSON);

            for (JsonNode jsonNode : node) {
                // Extract the desired values
                JsonNode thesisDTO = jsonNode.get("thesisDTO");
                JsonNode title = thesisDTO.get("title");
                JsonNode code = thesisDTO.get("code");
                JsonNode assignmentDate = thesisDTO.get("dateStart");
                JsonNode completionDate = thesisDTO.get("dateEnd");
                JsonNode examDate = thesisDTO.get("dateExam");
                JsonNode status = thesisDTO.get("thesisStatusTitle");

                thesis.setTitle(title.asText());
                thesis.setCode(code.asText());
                thesis.setAssignmentDate(UtilFunctions.convertTimestampToString(assignmentDate.asText()));
                thesis.setCompletionDate(UtilFunctions.convertTimestampToString(completionDate.asText()));
                thesis.setExamDate(UtilFunctions.convertTimestampToString(examDate.asText()));
                thesis.setStatus(status.asText());
            }
            return thesis;
        } catch (IOException e) {
            logger.error("[" + PRE_LOG + "] Error: {}", e.getMessage(), e);
            setException(e);
            setDocument(thesisJSON);
            return null;
        }
    }

    /**
     * Parses the information and grades JSON data and returns a Student object.
     *
     * @param infoJSON The JSON string containing the student's information.
     * @param gradesJSON The JSON string containing the student's grades.
     * @param thesisJSON The JSON string containing the student's thesis information.
     * @param totalAverageGrade The total average grade of the student.
     * @return A Student object representing the parsed data, or null if there was an error during parsing.
     */
    public Student parseInfoAndGradesJSON(String infoJSON, String gradesJSON, String thesisJSON, String totalAverageGrade) {
        Student student = new Student();

        try {
            Info info = parseInfoJSON(infoJSON);
            Grades grades = parseGradesJSON(gradesJSON, totalAverageGrade);
            Thesis thesis = parseThesissJSON(thesisJSON);

            if (info == null || grades == null) {
                return null;
            }
            
            info.setThesis(thesis);
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

    private void setFlow(String flow) {
        this.flow = flow;
    }
    
    private String getFlow(){
        return this.flow;
    }
}
