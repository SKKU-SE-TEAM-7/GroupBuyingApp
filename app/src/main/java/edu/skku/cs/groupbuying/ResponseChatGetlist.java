package edu.skku.cs.groupbuying;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ResponseChatGetlist {
    private ArrayList<chatList> chatlist;

    public ArrayList<edu.skku.cs.groupbuying.chatList> getChatlist() {
        return chatlist;
    }

    public void setChatlist(ArrayList<edu.skku.cs.groupbuying.chatList> chatlist) {
        this.chatlist = chatlist;
    }

    public ResponseChatGetlist() {}

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