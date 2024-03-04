package gr.uniwa.student_helper.model.neededCourses;

import gr.uniwa.student_helper.model.Course;
import java.util.ArrayList;

public class NeededCoursesN2 {
    
    private int mandatoryCoursesNeeded;
    private ArrayList<Course> mandatoryCoursesLeft;
    private int basicCoursesNeeded;
    private ArrayList<Course> basicCoursesLeft;
    private int choiceCoursesFromSameBasicNeeded;
    private ArrayList<Course> choiceCoursesFromSameBasicLeft;
    private int choiceCoursesFromOtherBasicAvailable;
    private ArrayList<Course> choiceCoursesFromOtherBasicLeft;
    private int choiceCoursesFromSameBasicPassed;
    private int choiceCoursesPassed;
    private boolean passedAll;

    public NeededCoursesN2() {
        this.mandatoryCoursesNeeded = 0;
        this.mandatoryCoursesLeft = new ArrayList<>();
        this.basicCoursesNeeded = 0;
        this.basicCoursesLeft = new ArrayList<>();
        this.choiceCoursesFromSameBasicNeeded = 0;
        this.choiceCoursesFromSameBasicLeft = new ArrayList<>();
        this.choiceCoursesFromOtherBasicAvailable = 0;
        this.choiceCoursesFromOtherBasicLeft= new ArrayList<>();
        this.choiceCoursesFromSameBasicPassed = 0;
        this.choiceCoursesPassed = 0;
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

    public int getBasicCoursesNeeded() {
        return basicCoursesNeeded;
    }

    public void setBasicCoursesNeeded(int basicCoursesNeeded) {
        this.basicCoursesNeeded = basicCoursesNeeded;
    }

    public ArrayList<Course> getBasicCoursesLeft() {
        return basicCoursesLeft;
    }

    public void setBasicCoursesLeft(ArrayList<Course> basicCoursesLeft) {
        this.basicCoursesLeft = basicCoursesLeft;
    }

    public int getChoiceCoursesFromSameBasicNeeded() {
        return choiceCoursesFromSameBasicNeeded;
    }

    public void setChoiceCoursesFromSameBasicNeeded(int choiceCoursesFromSameBasicNeeded) {
        this.choiceCoursesFromSameBasicNeeded = choiceCoursesFromSameBasicNeeded;
    }

    public ArrayList<Course> getChoiceCoursesFromSameBasicLeft() {
        return choiceCoursesFromSameBasicLeft;
    }

    public void setChoiceCoursesFromSameBasicLeft(ArrayList<Course> choiceCoursesFromSameBasicLeft) {
        this.choiceCoursesFromSameBasicLeft = choiceCoursesFromSameBasicLeft;
    }

    public int getChoiceCoursesFromOtherBasicAvailable() {
        return choiceCoursesFromOtherBasicAvailable;
    }

    public void setChoiceCoursesFromOtherBasicAvailable(int choiceCoursesFromOtherBasicAvailable) {
        this.choiceCoursesFromOtherBasicAvailable = choiceCoursesFromOtherBasicAvailable;
    }

    public ArrayList<Course> getChoiceCoursesFromOtherBasicLeft() {
        return choiceCoursesFromOtherBasicLeft;
    }

    public void setChoiceCoursesFromOtherBasicLeft(ArrayList<Course> choiceCoursesFromOtherBasicLeft) {
        this.choiceCoursesFromOtherBasicLeft = choiceCoursesFromOtherBasicLeft;
    }

    public int getChoiceCoursesFromSameBasicPassed() {
        return choiceCoursesFromSameBasicPassed;
    }

    public void setChoiceCoursesFromSameBasicPassed(int choiceCoursesFromSameBasicPassed) {
        this.choiceCoursesFromSameBasicPassed = choiceCoursesFromSameBasicPassed;
    }

    public int getChoiceCoursesPassed() {
        return choiceCoursesPassed;
    }

    public void setChoiceCoursesPassed(int choiceCoursesPassed) {
        this.choiceCoursesPassed = choiceCoursesPassed;
    }

    public boolean isPassedAll() {
        return passedAll;
    }

    public void setPassedAll(boolean passedAll) {
        this.passedAll = passedAll;
    }
}
