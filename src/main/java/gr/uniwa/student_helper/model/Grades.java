package gr.uniwa.student_helper.model;

import java.util.ArrayList;

public class Grades {

    private String totalPassedCourses;
    private String totalAverageGrade;
    private String totalEcts;
    private ArrayList<Course> courses;
    private NeededCourses neededCourses;
            
    public Grades() {
        this.courses = new ArrayList<>();
    }

    public Grades(String totalPassedCourses, String totalAverageGrade, String totalEcts) {
        this.totalPassedCourses = totalPassedCourses;
        this.totalAverageGrade = totalAverageGrade;
        this.totalEcts = totalEcts;
        this.courses = new ArrayList<>();
    }

    public String getTotalPassedCourses() {
        return totalPassedCourses;
    }

    public void setTotalPassedCourses(String totalPassedCourses) {
        this.totalPassedCourses = totalPassedCourses;
    }

    public String getTotalAverageGrade() {
        return totalAverageGrade;
    }

    public void setTotalAverageGrade(String totalAverageGrade) {
        this.totalAverageGrade = totalAverageGrade;
    }

    public String getTotalEcts() {
        return totalEcts;
    }

    public void setTotalEcts(String totalEcts) {
        this.totalEcts = totalEcts;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> semesters) {
        this.courses = semesters;
    }

    public NeededCourses getNeededCourses() {
        return neededCourses;
    }

    public void setNeededCourses(NeededCourses neededCourses) {
        this.neededCourses = neededCourses;
    }    
}