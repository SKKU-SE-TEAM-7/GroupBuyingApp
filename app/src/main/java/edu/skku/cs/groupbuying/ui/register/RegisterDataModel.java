package edu.skku.cs.groupbuying.ui.register;

public class RegisterDataModel {
    private String user_email;
    private String user_password;
    private String nickname;

    public String getUser_email() {
        return user_email;
    }

    public String getUser_password() {
        return user_password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
