package gr.uniwa.student_helper.model;

import java.util.ArrayList;

public class Semester {

    private int id;
    private ArrayList<Course> courses;

    public Semester() {
        this.courses = new ArrayList<>();
    }

    public Semester(int id) {
        this.id = id;
        this.courses = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }
}