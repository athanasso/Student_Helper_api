package gr.uniwa.student_helper.model.neededCourses;

import gr.uniwa.student_helper.model.Course;
import java.util.ArrayList;

public class NeededCoursesN2 {
    
    private int mandatoryCoursesNeeded;
    private ArrayList<Course> mandatoryCoursesLeft;
    private int basicCoursesNeeded;
    private ArrayList<Course> basicCoursesLeft;
    private int choiceCoursesFromSameNeeded;
    private ArrayList<Course> choiceCoursesFromSameLeft;
    private int choiceFromOthersBasicPassed;
    private ArrayList<Course> choiceFromOthersBasicAvailable;
    private boolean passedAll;

    public NeededCoursesN2() {
        this.mandatoryCoursesNeeded = 0;
        this.mandatoryCoursesLeft = new ArrayList<>();
        this.basicCoursesNeeded = 0;
        this.basicCoursesLeft = new ArrayList<>();
        this.choiceCoursesFromSameNeeded = 0;        
        this.choiceCoursesFromSameLeft = new ArrayList<>();
        this.choiceFromOthersBasicPassed = 0;
        this.choiceFromOthersBasicAvailable = new ArrayList<>();
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

    public void setBasicCoursesNeeded(int basicCourses1Needed) {
        this.basicCoursesNeeded = basicCourses1Needed;
    }

    public ArrayList<Course> getBasicCoursesLeft() {
        return basicCoursesLeft;
    }

    public void setBasicCoursesLeft(ArrayList<Course> basicCourses1Left) {
        this.basicCoursesLeft = basicCourses1Left;
    }
    public int getChoiceCoursesFromSameNeeded() {
        return choiceCoursesFromSameNeeded;
    }

    public void setChoiceCoursesFromSameNeeded(int choiceCoursesFromSameNeeded) {
        this.choiceCoursesFromSameNeeded = choiceCoursesFromSameNeeded;
    }

    public ArrayList<Course> getChoiceCoursesFromSameLeft() {
        return choiceCoursesFromSameLeft;
    }

    public void setChoiceCoursesFromSameLeft(ArrayList<Course> choiceCoursesFromSameLeft) {
        this.choiceCoursesFromSameLeft = choiceCoursesFromSameLeft;
    }
    
    public int getChoiceFromOthersBasicPassed() {
        return choiceFromOthersBasicPassed;
    }

    public void setChoiceFromOthersBasicPassed(int choiceFromOthersBasicPassed) {
        this.choiceFromOthersBasicPassed = choiceFromOthersBasicPassed;
    }

    public ArrayList<Course> getChoiceFromOthersBasicAvailable() {
        return choiceFromOthersBasicAvailable;
    }

    public void setChoiceFromOthersBasicAvailable(ArrayList<Course> choiceFromOthersBasicAvailable) {
        this.choiceFromOthersBasicAvailable = choiceFromOthersBasicAvailable;
    }

    public boolean isPassedAll() {
        return passedAll;
    }

    public void setPassedAll(boolean passedAll) {
        this.passedAll = passedAll;
    }
    
}
