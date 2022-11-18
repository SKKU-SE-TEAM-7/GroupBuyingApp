package edu.skku.cs.groupbuying.ui.detail;

import com.google.gson.annotations.SerializedName;

public class DetailDataModel {

    @SerializedName("chat-id")
    private int chat_id;

    @SerializedName("content-id")
    private int content_id;

    private int currentMember;
    private String detail;
    private String duedate;

    @SerializedName("image-url")
    private String image_url;

    private boolean is_joined;
    private String owner;
    private int targetMember;
    private String title;

    public String getTitle() {
        return title;
    }

    public int getContent_id() {
        return content_id;
    }

    public int getCurrentMember() {
        return currentMember;
    }

    public int getTargetMember() {
        return targetMember;
    }

    public String getDetail() {
        return detail;
    }

    public String getDueDate() {
        return duedate;
    }

    public String getOwner() {
        return owner;
    }

    public String getImage_url() {
        return image_url;
    }

    public int getChat_id() {
        return chat_id;
    }

    public boolean isIs_joined() {
        return is_joined;
    }
}
