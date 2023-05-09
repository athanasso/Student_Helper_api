package gr.uniwa.student_helper.model;

import java.util.ArrayList;

public class NeededCourses {

    private int choiceCoursesNeeded;
    private int mandatoryCoursesNeeded;
    private ArrayList<Course> mandatoryCoursesLeft;
    private int basicCoursesNeeded;
    private ArrayList<Course> basicCoursesLeft;
    private int choiceCoursesFromSameBasicNeeded;
    private ArrayList<Course> choiceCoursesFromSameBasicLeft;
    private int choiceCoursesFromOtherBasicAvailable;
    private ArrayList<Course> choiceCoursesFromOtherBasicLeft;
    private int generalCoursesPassed;
    private ArrayList<Course> generalCoursesLeft;
    private boolean passedAll;

    public NeededCourses() {
        this.choiceCoursesNeeded = 0;
         this.mandatoryCoursesNeeded = 0;
        this.mandatoryCoursesLeft = new ArrayList<>();
        this.choiceCoursesFromSameBasicNeeded = 0;
        this.choiceCoursesFromSameBasicLeft =  new ArrayList<>();
        this.choiceCoursesFromOtherBasicAvailable = 0;
        this.choiceCoursesFromOtherBasicLeft =  new ArrayList<>();
        this.generalCoursesPassed =  0;
        this.generalCoursesLeft =  new ArrayList<>();
        this.basicCoursesNeeded = 0;
        this.basicCoursesLeft = new ArrayList<>();
        this.passedAll = false;
    }

    public int getChoiceCoursesNeeded() {
        return choiceCoursesNeeded;
    }

    public void setChoiceCoursesNeeded(int choiceCoursesNeeded) {
        this.choiceCoursesNeeded = choiceCoursesNeeded;
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

    public int getGeneralCoursesPassed() {
        return generalCoursesPassed;
    }

    public void setGeneralCoursesPassed(int generalCoursesPassed) {
        this.generalCoursesPassed = generalCoursesPassed;
    }

    public ArrayList<Course> getGeneralCoursesLeft() {
        return generalCoursesLeft;
    }

    public void setGeneralCoursesLeft(ArrayList<Course> generalCoursesLeft) {
        this.generalCoursesLeft = generalCoursesLeft;
    }

    public boolean isPassedAll() {
        return passedAll;
    }

    public void setPassedAll(boolean passedAll) {
        this.passedAll = passedAll;
    }
}
