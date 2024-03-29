package gr.uniwa.student_helper.model;

public class Info {

    private String am;
    private String firstName;
    private String lastName;
    private String department;
    private String currentSemester;
    private String registrationYear;
    private String curriculum;
    private String flow;
    private String curriculumCode;
    private String deletionYear;
    private Thesis thesis;

    public Info() {
        this.am = "";
        this.firstName = "";
        this.lastName = "";
        this.department = "";
        this.currentSemester = "";
        this.registrationYear = "";
        this.curriculum = "";
        this.flow = "";
        this.curriculumCode = "";
        this.deletionYear = "";
        this.thesis = new Thesis();
    }

    public String getAm() {
        return am;
    }

    public void setAm(String am) {
        this.am = am;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCurrentSemester() {
        return currentSemester;
    }

    public void setCurrentSemester(String semester) {
        this.currentSemester = semester;
    }

    public String getRegistrationYear() {
        return registrationYear;
    }

    public void setRegistrationYear(String registrationYear) {
        this.registrationYear = registrationYear;
    }

    public String getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(String curriculum) {
        this.curriculum = curriculum;
    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public String getCurriculumCode() {
        return curriculumCode;
    }

    public void setCurriculumCode(String curriculumCode) {
        this.curriculumCode = curriculumCode;
    }

    public String getDeletionYear() {
        return deletionYear;
    }

    public void setDeletionYear(String deletionYear) {
        this.deletionYear = deletionYear;
    }

    public Thesis getThesis() {
        return thesis;
    }

    public void setThesis(Thesis thesis) {
        this.thesis = thesis;
    }
}