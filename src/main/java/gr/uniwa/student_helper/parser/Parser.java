package gr.uniwa.student_helper.parser;

import gr.uniwa.student_helper.model.Info;
import gr.uniwa.student_helper.model.Course;
import gr.uniwa.student_helper.model.Grades;
import gr.uniwa.student_helper.model.Semester;
import gr.uniwa.student_helper.model.Student;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Parser {
    private final int SEMESTER = 15;
    private Exception exception;
    private String document;
    private final String PRE_LOG;
    private final Logger logger = LoggerFactory.getLogger(Parser.class);

    public Parser(String university, String system) {
        this.PRE_LOG = university + (system == null ? "" : "." + system);
    }

    private Info parseInfoJSON(String infoJSON) {
        Info info = new Info();

        try {
            JsonNode student = new ObjectMapper().readTree(infoJSON);

            String aem = student.get("studentNo").asText();
            info.setAem(aem);

            String firstName = student.get("lastName").asText();
            info.setFirstName(firstName);

            String lastName = student.get("firstName").asText();
            info.setLastName(lastName);

            String department = student.get("departmentTitle").asText();
            info.setDepartment(department);

            String registrationYear = student.get("insSyllabus").asText();
            info.setRegistrationYear(registrationYear);
            
            String programTitle = student.get("programTitle").asText();
            info.setProgramTitle(programTitle);

            int currentSemester = student.get("lastSemester").asInt();
            info.setCurrentSemester((currentSemester == 0) ? "1" : String.valueOf(currentSemester));

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
        ArrayList<Semester> semesters = initSemesters();
        DecimalFormat df2 = new DecimalFormat("#.##");

        double totalEcts = 0;
        int count = 0;
        int[] semesterCount = new int[SEMESTER];
        try {
            JsonNode node = new ObjectMapper().readTree(gradesJSON);
            JsonNode studentCourses = node.get("studentCourses");
            if (studentCourses == null) studentCourses = node;

            if (studentCourses.size() == 0) {
                grades.setTotalAverageGrade("-");
                grades.setTotalPassedCourses("0");
                grades.setTotalEcts("0");
                grades.setSemesters(new ArrayList<>());
                return grades;
            }

            for (JsonNode courseJSON: studentCourses)  {
                JsonNode semesterId = courseJSON.get("semesterId");
                int studentSemester = semesterId.get("sortOrder").asInt();
                if (studentSemester == 253 || studentSemester == 254)
                    studentSemester = 7;
                if (studentSemester == 255)
                    studentSemester = 13;
                if (studentSemester == 251)
                    studentSemester = 14;
                if (studentSemester == 252)
                    studentSemester = 15;
                Semester semester = semesters.get(studentSemester-1);
                semester.setId(studentSemester);

                Course course = new Course();
                String id = courseJSON.get("courseCode").asText();
                course.setId(id);

                String name = courseJSON.get("title").asText();
                course.setName(name);

                double grade = 0;
                if (courseJSON.get("grade").isNull()) {
                    course.setGrade("-");
                } else {
                    grade = courseJSON.get("grade").asDouble() * 10;
                    course.setGrade(df2.format(grade));
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

                semester.getCourses().add(course);
            }

            ArrayList<Semester> found = new ArrayList<>();
            for (Semester semester : semesters) {
                if (semester.getId() == 0) {
                    found.add(semester);
                }
            }
            semesters.removeAll(found);

            grades.setTotalEcts(String.valueOf(Math.ceil(totalEcts)).replace(".0", "").replace(",0", ""));
            grades.setTotalAverageGrade(totalAverageGrade);
            grades.setTotalPassedCourses(String.valueOf(count));
            grades.setSemesters(semesters);
            return grades;
        } catch (IOException e) {
            logger.error("[" + PRE_LOG + "] Error: {}", e.getMessage(), e);
            setException(e);
            setDocument(gradesJSON);
            return null;
        }
    }

    private ArrayList<Semester> initSemesters() {
        ArrayList<Semester> semesters = new ArrayList<>();
        for (int i = 0; i <= SEMESTER; i++) {
            Semester semester = new Semester();
            ArrayList<Course> courses = new ArrayList<>();
            semester.setCourses(courses);
            semesters.add(semester);
        }
        return semesters;
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