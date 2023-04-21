package gr.uniwa.student_helper.dto;

import gr.uniwa.student_helper.model.Student;
import java.util.Map;

public class StudentDTO {
    private Student student;

    public StudentDTO() {
    }

    public StudentDTO(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}