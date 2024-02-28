package gr.uniwa.student_helper.model;

public class FileCourse {
      
    private String id;
    private String name;
    private String grade;
    private String ects;

    public FileCourse() {
    }

    public FileCourse(String id, String name, String grade, String ects) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.ects = ects;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getEcts() {
        return ects;
    }

    public void setEcts(String ects) {
        this.ects = ects;
    }
}
