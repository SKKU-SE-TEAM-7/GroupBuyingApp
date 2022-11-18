package edu.skku.cs.groupbuying.ui.notifications;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class usermodel {
    private String user_email;

    @SerializedName("accumulate-star")
    private int accumulate_star;

    @SerializedName("join-content")
    private ArrayList<String> join_content;

    private String nickname;
    private float star;

    @SerializedName("start-count")
    private int star_count;

    public String getUser_email() {
        return user_email;
    }

    public int getAccumulate_star() {
        return accumulate_star;
    }

    public ArrayList<String> getJoin_content() {
        return join_content;
    }

    public String getNickname() {
        return nickname;
    }

    public float getStar() {
        return star;
    }

    public int getStar_count() {
        return star_count;
    }


}




