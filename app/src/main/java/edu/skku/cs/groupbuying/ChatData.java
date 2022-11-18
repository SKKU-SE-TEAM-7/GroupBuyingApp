package edu.skku.cs.groupbuying;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import edu.skku.cs.groupbuying.networkobject.ResponseChatGetlist;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChatData {
    public String item_img;
    public String title;
    public int chatid;
    public int contentid;

    public ChatData(String item_title, int chatid, int contentid) {
        //this.item_img = R.drawable.ic_baseline_person_24;
        this.title = item_title;
        this.chatid = chatid;
        this.contentid = contentid;

        final String server_adrs = "http://52.78.137.254:8080";
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(server_adrs + "/content/get").newBuilder();
        urlBuilder.addQueryParameter("token", Integer.toString(GlobalObject.getToken()));
        urlBuilder.addQueryParameter("content-id", Integer.toString(contentid));
        String url = urlBuilder.build().toString();
        Request req = new Request.Builder().url(url).build();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) { countDownLatch.countDown(); }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response resp) throws IOException {
                String responseStr = resp.body().string();

                JsonObject jsonObject = JsonParser.parseString(responseStr).getAsJsonObject();

                item_img = jsonObject.get("content").getAsJsonObject().get("image-url").getAsString();
                title = jsonObject.get("content").getAsJsonObject().get("title").getAsString();

                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
