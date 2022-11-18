package edu.skku.cs.groupbuying.networkobject;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

public class ResponseChatGetchat {
    private chatInfo chatinfo;

    public chatInfo getChatinfo() {
        return chatinfo;
    }

    public void setChatinfo(chatInfo chatinfo) {
        this.chatinfo = chatinfo;
    }

    public ResponseChatGetchat(String json) {
        JsonElement element = JsonParser.parseString(json);
        JsonObject object = element.getAsJsonObject();

        JsonObject jsonchatinfo = object.get("chat-info").getAsJsonObject();

        chatinfo = new chatInfo(jsonchatinfo);
    }
}