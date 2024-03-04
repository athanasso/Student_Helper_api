package gr.uniwa.student_helper.model.neededCourses;

import gr.uniwa.student_helper.model.Course;
import java.util.ArrayList;

public class NeededCoursesPeir {
    
    private int mandatoryCoursesNeeded;
    private ArrayList<Course> mandatoryCoursesLeft;
    private int choiceCoursesNeeded;
    private ArrayList<Course> choiceCoursesLeft;
    private boolean passedAll;

    public NeededCoursesPeir() {
        this.mandatoryCoursesNeeded = 0;
        this.mandatoryCoursesLeft = new ArrayList<>();
        this.choiceCoursesNeeded = 0;
        this.choiceCoursesLeft = new ArrayList<>();
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

    public int getChoiceCoursesNeeded() {
        return choiceCoursesNeeded;
    }

    public void setChoiceCoursesNeeded(int choiceCoursesNeeded) {
        this.choiceCoursesNeeded = choiceCoursesNeeded;
    }

    public ArrayList<Course> getChoiceCoursesLeft() {
        return choiceCoursesLeft;
    }

    public void setChoiceCoursesLeft(ArrayList<Course> choiceCoursesLeft) {
        this.choiceCoursesLeft = choiceCoursesLeft;
    }

    public boolean isPassedAll() {
        return passedAll;
    }

    public void setPassedAll(boolean passedAll) {
        this.passedAll = passedAll;
    }
    
}
