package gr.uniwa.student_helper.dto;

import gr.uniwa.student_helper.model.Student;
import java.util.Map;

public class StudentDTO {
    private Map<String, String> cookies;
    private Student student;

    public StudentDTO() {
    }

    public StudentDTO(Map<String, String> cookies, Student student) {
        this.cookies = cookies;
        this.student = student;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}