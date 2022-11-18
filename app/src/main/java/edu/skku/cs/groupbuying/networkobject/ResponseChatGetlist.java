package edu.skku.cs.groupbuying.networkobject;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

public class ResponseChatGetlist {
    private ArrayList<chatList> chatlist;

    public ArrayList<chatList> getChatlist() {
        return chatlist;
    }

    public void setChatlist(ArrayList<chatList> chatlist) {
        this.chatlist = chatlist;
    }

    public ResponseChatGetlist(String json) {
        chatlist = new ArrayList<>();

        JsonElement element = JsonParser.parseString(json);
        JsonObject object = element.getAsJsonObject();

        JsonArray jsonchatlist = object.get("chatList").getAsJsonArray();

        for (int i = 0; i < jsonchatlist.size(); i++) {
            JsonObject chatlistObj = jsonchatlist.get(i).getAsJsonObject();

            chatlist.add(new chatList(chatlistObj));
        }
    }
}