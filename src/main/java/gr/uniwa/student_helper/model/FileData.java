package gr.uniwa.student_helper.model;

import java.util.ArrayList;

public class FileData {
    private String curriculum;
    private String registrationYear;
    private ArrayList<FileCourse> courses;

    public FileData(String curriculum, String registrationYear, ArrayList<FileCourse> courses) {
        this.curriculum = curriculum;
        this.registrationYear = registrationYear;
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

    public ArrayList<FileCourse> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<FileCourse> courses) {
        this.courses = courses;
    }
}
