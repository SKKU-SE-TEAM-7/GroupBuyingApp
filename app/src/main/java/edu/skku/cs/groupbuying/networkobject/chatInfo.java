package edu.skku.cs.groupbuying.networkobject;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class chatInfo {
    private int chatid;
    private ArrayList<chat> chats;
    private String owner;
    private String participant;

    public int getChatid() {
        return chatid;
    }

    public void setChatid(int chatid) {
        this.chatid = chatid;
    }

    public ArrayList<chat> getChats() {
        return chats;
    }

    public void setChats(ArrayList<chat> chats) {
        this.chats = chats;
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

    public chatInfo(JsonObject jsonObject) {
        chatid = jsonObject.get("chat-id").getAsInt();
        chats = new ArrayList<>();
        JsonArray jsonchats = jsonObject.get("chats").getAsJsonArray();
        for (int i = 0; i < jsonchats.size(); i++) {
            JsonObject chatObj = jsonchats.get(i).getAsJsonObject();

            chats.add(new chat(chatObj));
        }
        owner = jsonObject.get("owner").getAsString();
        participant = jsonObject.get("participant").getAsString();

        Log.d("ahoy", "part: " + participant);
    }
}
