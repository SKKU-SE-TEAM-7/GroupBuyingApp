package edu.skku.cs.groupbuying.networkobject;

import com.google.gson.JsonObject;

public class chat {
    private String sender;
    private String text;
    private String time;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public chat(JsonObject jsonObject) {
        sender = jsonObject.get("sender").getAsString();
        text = jsonObject.get("text").getAsString();
        time = jsonObject.get("time").getAsString();
    }
}
