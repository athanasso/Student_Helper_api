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

            String aem = student.get("username") != null
                    ? student.get("username").asText()
                    : student.get("studentNo").asText();
            info.setAem(aem);

            String firstName = student.get("lastName").asText();
            info.setFirstName(firstName);

            String lastName = student.get("firstName").asText();
            info.setLastName(lastName);

            String department = student.get("departmentTitle").asText();
            info.setDepartment(department);

            String registrationYear = student.get("programTitle").asText();
            info.setRegistrationYear(registrationYear);

            int semester = student.get("lastSemester").asInt();
            info.setSemester((semester == 0) ? "1" : String.valueOf(semester));

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

                            int semesterEcts = Integer.parseInt(semester.getEcts());
                            semesterEcts += ects;
                            semester.setEcts(String.valueOf(semesterEcts));

                            if (semester.getGradeAverage().equals("-")) {
                                semester.setGradeAverage(String.valueOf(grade));
                                semesterCount[studentSemester-1] = 1;
                            } else {
                                double semesterAverageGrade = Double.parseDouble(semester.getGradeAverage());
                                semesterAverageGrade += grade;
                                semester.setGradeAverage(String.valueOf(semesterAverageGrade));
                                semesterCount[studentSemester-1]++;
                            }
                        }
                    }
                }

                semester.getCourses().add(course);
            }

            for (int i = 0; i < SEMESTER; i++)
                semesters.get(i).setPassedCourses(semesterCount[i]);

            ArrayList<Semester> found = new ArrayList<>();
            for (Semester semester : semesters) {
                if (semester.getId() == 0) {
                    found.add(semester);
                }
            }
            semesters.removeAll(found);

            for (int i = 0; i < semesters.size(); i++) {
                if (semesters.get(i).getGradeAverage().equals("-")) {
                    semesters.get(i).setPassedCourses(0);
                    continue;
                }
                double semesterSum = Double.parseDouble(semesters.get(i).getGradeAverage());
                int semesterPassedCourses = semesters.get(i).getPassedCourses();
                semesters.get(i).setGradeAverage(df2.format(semesterSum/semesterPassedCourses));
            }

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
            semester.setEcts("0");
            semester.setGradeAverage("-");
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