package gr.uniwa.student_helper.model.neededCourses;

import gr.uniwa.student_helper.model.Course;
import java.util.ArrayList;

public class NeededCoursesN2 {
    
    private int mandatoryCoursesNeeded;
    private ArrayList<Course> mandatoryCoursesLeft;
    private int basicCoursesNeeded;
    private ArrayList<Course> basicCoursesLeft;
    private int choice1CoursesNeeded;
    private ArrayList<Course> choice1CoursesLeft;
    private int choice2CoursesNeeded;
    private ArrayList<Course> choice2CoursesLeft;
    private int choiceFromOthersBasicPassed;
    private ArrayList<Course> choiceFromOthersBasicAvailable;
    private boolean passedAll;

    public NeededCoursesN2() {
        this.mandatoryCoursesNeeded = 0;
        this.mandatoryCoursesLeft = new ArrayList<>();
        this.basicCoursesNeeded = 0;
        this.basicCoursesLeft = new ArrayList<>();
        this.choice1CoursesNeeded = 0;        
        this.choice1CoursesLeft = new ArrayList<>();
        this.choice2CoursesNeeded = 0;
        this.choice2CoursesLeft = new ArrayList<>();
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

    public int getChoice1CoursesNeeded() {
        return choice1CoursesNeeded;
    }

    public void setChoice1CoursesNeeded(int choice1CoursesNeeded) {
        this.choice1CoursesNeeded = choice1CoursesNeeded;
    }

    public ArrayList<Course> getChoice1CoursesLeft() {
        return choice1CoursesLeft;
    }

    public void setChoice1CoursesLeft(ArrayList<Course> choice1CoursesLeft) {
        this.choice1CoursesLeft = choice1CoursesLeft;
    }

    public int getChoice2CoursesNeeded() {
        return choice2CoursesNeeded;
    }

    public void setChoice2CoursesNeeded(int choice2CoursesNeeded) {
        this.choice2CoursesNeeded = choice2CoursesNeeded;
    }

    public ArrayList<Course> getChoice2CoursesLeft() {
        return choice2CoursesLeft;
    }

    public void setChoice2CoursesLeft(ArrayList<Course> choice2CoursesLeft) {
        this.choice2CoursesLeft = choice2CoursesLeft;
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
