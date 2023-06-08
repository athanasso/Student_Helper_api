package gr.uniwa.student_helper.model;

public class Thesis {
    private String title;
    private String code;
    private String assignmentDate;
    private String completionDate;
    private String examDate;
    private String status;
    private String lastDueDate;

    public Thesis() {
        this.title = "";
        this.code = "";
        this.assignmentDate = "";
        this.completionDate = "";
        this.examDate = "";
        this.status = "";
        this.lastDueDate = "";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAssignmentDate() {
        return assignmentDate;
    }

    public void setAssignmentDate(String assignmentDate) {
        this.assignmentDate = assignmentDate;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String dateFinished) {
        this.completionDate = dateFinished;
    }

    public String getExamDate() {
        return examDate;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String state) {
        this.status = state;
    }

    public String getLastDueDate() {
        return lastDueDate;
    }

    public void setLastDueDate(String lastDueDate) {
        this.lastDueDate = lastDueDate;
    }
    
}
