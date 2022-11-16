package edu.skku.cs.groupbuying.ui.home;

import com.google.gson.annotations.SerializedName;

public class ContentModel {

    @SerializedName("content-id")
    private int content_id;

    private int currentMember;
    private String detail;
    private String dueDate;

    @SerializedName("image-url")
    private String image_url;

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
        return dueDate;
    }

    public String getOwner() {
        return owner;
    }

    public String getImage_url() {
        return image_url;
    }
}
