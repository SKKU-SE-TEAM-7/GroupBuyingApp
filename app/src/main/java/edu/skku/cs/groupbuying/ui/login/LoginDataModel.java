package edu.skku.cs.groupbuying.ui.login;

public class LoginDataModel {
    private String message;
    private Integer token;

    public String getMessage() {
        return message;
    }

    public Integer getToken() {
        return token;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setToken(Integer token) {
        this.token = token;
    }
}
