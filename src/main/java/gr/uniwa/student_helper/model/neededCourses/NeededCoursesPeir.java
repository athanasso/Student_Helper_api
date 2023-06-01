package gr.uniwa.student_helper.model.neededCourses;

import gr.uniwa.student_helper.model.Course;
import java.util.ArrayList;

public class NeededCoursesPeir {
    
    private int mandatoryCoursesNeeded;
    private ArrayList<Course> mandatoryCoursesLeft;
    private int choiceCourses1Needed;
    private ArrayList<Course> choiceCourses1Left;
    private int choiceCourses2Needed;
    private ArrayList<Course> choiceCourses2Left;
    private int choiceCourses3Needed;
    private ArrayList<Course> choiceCourses3Left;
    private boolean passedAll;

    public NeededCoursesPeir() {
        this.mandatoryCoursesNeeded = 0;
        this.mandatoryCoursesLeft = new ArrayList<>();
        this.choiceCourses1Needed = 0;
        this.choiceCourses1Left = new ArrayList<>();
        this.choiceCourses2Needed = 0;
        this.choiceCourses2Left = new ArrayList<>();
        this.choiceCourses3Needed = 0;
        this.choiceCourses3Left = new ArrayList<>();
        this.passedAll = false;
    }

    public int getMandatoryCoursesNeeded() {
        return mandatoryCoursesNeeded;
    }

    public void setMandatoryCoursesNeeded(int mandatoryCoursesNeeded) {
        this.mandatoryCoursesNeeded = mandatoryCoursesNeeded;
    }

    public ArrayList<Course> getMandatoryCoursesLeft() {
        return mandatoryCoursesLeft;
    }

    public void setMandatoryCoursesLeft(ArrayList<Course> mandatoryCoursesLeft) {
        this.mandatoryCoursesLeft = mandatoryCoursesLeft;
    }

    public int getChoiceCourses1Needed() {
        return choiceCourses1Needed;
    }

    public void setChoiceCourses1Needed(int choiceCourses1Needed) {
        this.choiceCourses1Needed = choiceCourses1Needed;
    }

    public ArrayList<Course> getChoiceCourses1Left() {
        return choiceCourses1Left;
    }

    public void setChoiceCourses1Left(ArrayList<Course> choiceCourses1Left) {
        this.choiceCourses1Left = choiceCourses1Left;
    }

    public int getChoiceCourses2Needed() {
        return choiceCourses2Needed;
    }

    public void setChoiceCourses2Needed(int choiceCourses2Needed) {
        this.choiceCourses2Needed = choiceCourses2Needed;
    }

    public ArrayList<Course> getChoiceCourses2Left() {
        return choiceCourses2Left;
    }

    public void setChoiceCourses2Left(ArrayList<Course> choiceCourses2Left) {
        this.choiceCourses2Left = choiceCourses2Left;
    }

    public int getChoiceCourses3Needed() {
        return choiceCourses3Needed;
    }

    public void setChoiceCourses3Needed(int choiceCourses3Needed) {
        this.choiceCourses3Needed = choiceCourses3Needed;
    }

    public ArrayList<Course> getChoiceCourses3Left() {
        return choiceCourses3Left;
    }

    public void setChoiceCourses3Left(ArrayList<Course> choiceCourses3Left) {
        this.choiceCourses3Left = choiceCourses3Left;
    }

    public boolean isPassedAll() {
        return passedAll;
    }

    public void setPassedAll(boolean passedAll) {
        this.passedAll = passedAll;
    }
    
}
