package gr.uniwa.student_helper.dto;

import gr.uniwa.student_helper.model.Student;
import java.util.Map;

public class StudentDTO {
    private Student student;
    private Map<String, String> cookies;

    public StudentDTO() {
    }

    public StudentDTO(Student student, Map<String, String> cookies) {
        this.student = student;
        this.cookies = cookies;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
    
    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }
}