package gr.uniwa.student_helper.model.neededCourses;

import gr.uniwa.student_helper.model.Course;
import java.util.ArrayList;

public class NeededCoursesICE1 {

    private int mandatoryCoursesNeeded;
    private ArrayList<Course> mandatoryCoursesLeft;
    private int basicCoursesNeededForSoftware;
    private ArrayList<Course> basicCoursesLeftForSoftware;
    private int choiceCoursesFromSameBasicNeededForSoftware;
    private ArrayList<Course> choiceCoursesFromSameBasicLeftForSoftware;
    private int choiceCoursesFromOtherBasicAvailableForSoftware;
    private ArrayList<Course> choiceCoursesFromOtherBasicLeftForSoftware;
    private int choiceCoursesFromSameBasicPassedForSoftware;
    private int basicCoursesNeededForHardware;
    private ArrayList<Course> basicCoursesLeftForHardware;
    private int choiceCoursesFromSameBasicNeededForHardware;
    private ArrayList<Course> choiceCoursesFromSameBasicLeftForHardware;
    private int choiceCoursesFromOtherBasicAvailableForHardware;
    private ArrayList<Course> choiceCoursesFromOtherBasicLeftForHardware;
    private int choiceCoursesFromSameBasicPassedForHardware;
    private int basicCoursesNeededForNetwork;
    private ArrayList<Course> basicCoursesLeftForNetwork;
    private int choiceCoursesFromSameBasicNeededForNetwork;
    private ArrayList<Course> choiceCoursesFromSameBasicLeftForNetwork;
    private int choiceCoursesFromOtherBasicAvailableForNetwork;
    private ArrayList<Course> choiceCoursesFromOtherBasicLeftForNetwork;
    private int choiceCoursesFromSameBasicPassedForNetwork;
    private int generalCoursesPassed;
    private ArrayList<Course> generalCoursesLeft;
    private boolean passedAll;

    public NeededCoursesICE1() {
        this.mandatoryCoursesNeeded = 0;
        this.mandatoryCoursesLeft = new ArrayList<>();
        this.basicCoursesNeededForSoftware = 0;
        this.basicCoursesLeftForSoftware = new ArrayList<>();
        this.choiceCoursesFromSameBasicNeededForSoftware = 0;
        this.choiceCoursesFromSameBasicLeftForSoftware = new ArrayList<>();
        this.choiceCoursesFromOtherBasicAvailableForSoftware = 0;
        this.choiceCoursesFromOtherBasicLeftForSoftware = new ArrayList<>();
        this.choiceCoursesFromSameBasicPassedForSoftware = 0;
        this.basicCoursesNeededForHardware = 0;
        this.basicCoursesLeftForHardware = new ArrayList<>();
        this.choiceCoursesFromSameBasicNeededForHardware = 0;
        this.choiceCoursesFromSameBasicLeftForHardware = new ArrayList<>();
        this.choiceCoursesFromOtherBasicAvailableForHardware = 0;
        this.choiceCoursesFromOtherBasicLeftForHardware = new ArrayList<>();
        this.choiceCoursesFromSameBasicPassedForHardware = 0;
        this.basicCoursesNeededForNetwork = 0;
        this.basicCoursesLeftForNetwork = new ArrayList<>();
        this.choiceCoursesFromSameBasicNeededForNetwork = 0;
        this.choiceCoursesFromSameBasicLeftForNetwork = new ArrayList<>();
        this.choiceCoursesFromOtherBasicAvailableForNetwork = 0;
        this.choiceCoursesFromOtherBasicLeftForNetwork = new ArrayList<>();
        this.choiceCoursesFromSameBasicPassedForNetwork = 0;
        this.generalCoursesPassed = 0;
        this.generalCoursesLeft = new ArrayList<>();
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

    public int getBasicCoursesNeededForSoftware() {
        return basicCoursesNeededForSoftware;
    }

    public void setBasicCoursesNeededForSoftware(int basicCoursesNeededForSoftware) {
        this.basicCoursesNeededForSoftware = basicCoursesNeededForSoftware;
    }

    public ArrayList<Course> getBasicCoursesLeftForSoftware() {
        return basicCoursesLeftForSoftware;
    }

    public void setBasicCoursesLeftForSoftware(ArrayList<Course> basicCoursesLeftForSoftware) {
        this.basicCoursesLeftForSoftware = basicCoursesLeftForSoftware;
    }

    public int getChoiceCoursesFromSameBasicNeededForSoftware() {
        return choiceCoursesFromSameBasicNeededForSoftware;
    }

    public void setChoiceCoursesFromSameBasicNeededForSoftware(int choiceCoursesFromSameBasicNeededForSoftware) {
        this.choiceCoursesFromSameBasicNeededForSoftware = choiceCoursesFromSameBasicNeededForSoftware;
    }

    public ArrayList<Course> getChoiceCoursesFromSameBasicLeftForSoftware() {
        return choiceCoursesFromSameBasicLeftForSoftware;
    }

    public void setChoiceCoursesFromSameBasicLeftForSoftware(ArrayList<Course> choiceCoursesFromSameBasicLeftForSoftware) {
        this.choiceCoursesFromSameBasicLeftForSoftware = choiceCoursesFromSameBasicLeftForSoftware;
    }

    public int getChoiceCoursesFromOtherBasicAvailableForSoftware() {
        return choiceCoursesFromOtherBasicAvailableForSoftware;
    }

    public void setChoiceCoursesFromOtherBasicAvailableForSoftware(int choiceCoursesFromOtherBasicAvailableForSoftware) {
        this.choiceCoursesFromOtherBasicAvailableForSoftware = choiceCoursesFromOtherBasicAvailableForSoftware;
    }

    public ArrayList<Course> getChoiceCoursesFromOtherBasicLeftForSoftware() {
        return choiceCoursesFromOtherBasicLeftForSoftware;
    }

    public void setChoiceCoursesFromOtherBasicLeftForSoftware(ArrayList<Course> choiceCoursesFromOtherBasicLeftForSoftware) {
        this.choiceCoursesFromOtherBasicLeftForSoftware = choiceCoursesFromOtherBasicLeftForSoftware;
    }

    public int getChoiceCoursesFromSameBasicPassedForSoftware() {
        return choiceCoursesFromSameBasicPassedForSoftware;
    }

    public void setChoiceCoursesFromSameBasicPassedForSoftware(int choiceCoursesFromSameBasicPassedForSoftware) {
        this.choiceCoursesFromSameBasicPassedForSoftware = choiceCoursesFromSameBasicPassedForSoftware;
    }
    
    public int getBasicCoursesNeededForHardware() {
        return basicCoursesNeededForHardware;
    }

    public void setBasicCoursesNeededForHardware(int basicCoursesNeededForHardware) {
        this.basicCoursesNeededForHardware = basicCoursesNeededForHardware;
    }

    public ArrayList<Course> getBasicCoursesLeftForHardware() {
        return basicCoursesLeftForHardware;
    }

    public void setBasicCoursesLeftForHardware(ArrayList<Course> basicCoursesLeftForHardware) {
        this.basicCoursesLeftForHardware = basicCoursesLeftForHardware;
    }

    public int getChoiceCoursesFromSameBasicNeededForHardware() {
        return choiceCoursesFromSameBasicNeededForHardware;
    }

    public void setChoiceCoursesFromSameBasicNeededForHardware(int choiceCoursesFromSameBasicNeededForHardware) {
        this.choiceCoursesFromSameBasicNeededForHardware = choiceCoursesFromSameBasicNeededForHardware;
    }

    public ArrayList<Course> getChoiceCoursesFromSameBasicLeftForHardware() {
        return choiceCoursesFromSameBasicLeftForHardware;
    }

    public void setChoiceCoursesFromSameBasicLeftForHardware(ArrayList<Course> choiceCoursesFromSameBasicLeftForHardware) {
        this.choiceCoursesFromSameBasicLeftForHardware = choiceCoursesFromSameBasicLeftForHardware;
    }

    public int getChoiceCoursesFromOtherBasicAvailableForHardware() {
        return choiceCoursesFromOtherBasicAvailableForHardware;
    }

    public void setChoiceCoursesFromOtherBasicAvailableForHardware(int choiceCoursesFromOtherBasicAvailableForHardware) {
        this.choiceCoursesFromOtherBasicAvailableForHardware = choiceCoursesFromOtherBasicAvailableForHardware;
    }

    public ArrayList<Course> getChoiceCoursesFromOtherBasicLeftForHardware() {
        return choiceCoursesFromOtherBasicLeftForHardware;
    }

    public void setChoiceCoursesFromOtherBasicLeftForHardware(ArrayList<Course> choiceCoursesFromOtherBasicLeftForHardware) {
        this.choiceCoursesFromOtherBasicLeftForHardware = choiceCoursesFromOtherBasicLeftForHardware;
    }

    public int getChoiceCoursesFromSameBasicPassedForHardware() {
        return choiceCoursesFromSameBasicPassedForHardware;
    }

    public void setChoiceCoursesFromSameBasicPassedForHardware(int choiceCoursesFromSameBasicPassedForHardware) {
        this.choiceCoursesFromSameBasicPassedForHardware = choiceCoursesFromSameBasicPassedForHardware;
    }

    public int getBasicCoursesNeededForNetwork() {
        return basicCoursesNeededForNetwork;
    }

    public void setBasicCoursesNeededForNetwork(int basicCoursesNeededForNetwork) {
        this.basicCoursesNeededForNetwork = basicCoursesNeededForNetwork;
    }

    public ArrayList<Course> getBasicCoursesLeftForNetwork() {
        return basicCoursesLeftForNetwork;
    }

    public void setBasicCoursesLeftForNetwork(ArrayList<Course> basicCoursesLeftForNetwork) {
        this.basicCoursesLeftForNetwork = basicCoursesLeftForNetwork;
    }

    public int getChoiceCoursesFromSameBasicNeededForNetwork() {
        return choiceCoursesFromSameBasicNeededForNetwork;
    }

    public void setChoiceCoursesFromSameBasicNeededForNetwork(int choiceCoursesFromSameBasicNeededForNetwork) {
        this.choiceCoursesFromSameBasicNeededForNetwork = choiceCoursesFromSameBasicNeededForNetwork;
    }

    public ArrayList<Course> getChoiceCoursesFromSameBasicLeftForNetwork() {
        return choiceCoursesFromSameBasicLeftForNetwork;
    }

    public void setChoiceCoursesFromSameBasicLeftForNetwork(ArrayList<Course> choiceCoursesFromSameBasicLeftForNetwork) {
        this.choiceCoursesFromSameBasicLeftForNetwork = choiceCoursesFromSameBasicLeftForNetwork;
    }

    public int getChoiceCoursesFromOtherBasicAvailableForNetwork() {
        return choiceCoursesFromOtherBasicAvailableForNetwork;
    }

    public void setChoiceCoursesFromOtherBasicAvailableForNetwork(int choiceCoursesFromOtherBasicAvailableForNetwork) {
        this.choiceCoursesFromOtherBasicAvailableForNetwork = choiceCoursesFromOtherBasicAvailableForNetwork;
    }

    public ArrayList<Course> getChoiceCoursesFromOtherBasicLeftForNetwork() {
        return choiceCoursesFromOtherBasicLeftForNetwork;
    }

    public void setChoiceCoursesFromOtherBasicLeftForNetwork(ArrayList<Course> choiceCoursesFromOtherBasicLeftForNetwork) {
        this.choiceCoursesFromOtherBasicLeftForNetwork = choiceCoursesFromOtherBasicLeftForNetwork;
    }

    public int getChoiceCoursesFromSameBasicPassedForNetwork() {
        return choiceCoursesFromSameBasicPassedForNetwork;
    }

    public void setChoiceCoursesFromSameBasicPassedForNetwork(int choiceCoursesFromSameBasicPassedForNetwork) {
        this.choiceCoursesFromSameBasicPassedForNetwork = choiceCoursesFromSameBasicPassedForNetwork;
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
