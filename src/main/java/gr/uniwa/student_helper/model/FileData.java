package gr.uniwa.student_helper.model;

import java.util.ArrayList;

public class FileData {
    private String curriculum;
    private String registrationYear;
    private String flow;
    private ArrayList<FileCourse> courses;

    public FileData(String curriculum, String registrationYear, String flow, ArrayList<FileCourse> courses) {
        this.curriculum = curriculum;
        this.registrationYear = registrationYear;
        this.flow = flow;
        this.courses = courses;
    }

    public FileData() {
    }

    public String getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(String curriculum) {
        this.curriculum = curriculum;
    }

    public String getRegistrationYear() {
        return registrationYear;
    }

    public void setRegistrationYear(String registrationYear) {
        this.registrationYear = registrationYear;
    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public ArrayList<FileCourse> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<FileCourse> courses) {
        this.courses = courses;
    }
}
