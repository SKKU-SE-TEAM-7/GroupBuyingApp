package edu.skku.cs.groupbuying.networkobject;

import android.util.Log;

import com.google.gson.JsonObject;

public class chatList {
    private int chatid;
    private String owner;
    private String participant;
    private int contentid;

    public int getChatid() {
        return chatid;
    }

    public void setChatid(int chatid) {
        this.chatid = chatid;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public int getContentid() { return contentid; }
    public void setContentid(int contentid) { this.contentid = contentid; }

    public chatList(JsonObject jsonObject) {
        setChatid(jsonObject.get("chat-id").getAsInt());
        setOwner(jsonObject.get("owner").getAsString());
        setParticipant(jsonObject.get("participant").getAsString());
        setContentid(jsonObject.get("content-id").getAsInt());
    }
}
