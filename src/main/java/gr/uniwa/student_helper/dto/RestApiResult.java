package gr.uniwa.student_helper.dto;

public class RestApiResult<T> {
    private T data;
    private int code;
    private String description;

    public RestApiResult(T data, int code, String description) {
        this.data = data;
        this.code = code;
        this.description = description;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}