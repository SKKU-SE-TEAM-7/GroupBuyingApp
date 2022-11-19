package edu.skku.cs.groupbuying.networkobject;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkSnip {
    static String nickname;

    public static String getNicknamebyEmail(String email) {
        final String server_adrs = "http://52.78.137.254:8080";
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(server_adrs + "/user/getinfo").newBuilder();
        urlBuilder.addQueryParameter("email", email);

        String url = urlBuilder.build().toString();
        Request req = new Request.Builder().url(url).build();

        Log.d("ahoy", "url: " + url);

        CountDownLatch countDownLatch = new CountDownLatch(1);

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) { countDownLatch.countDown(); }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response resp) throws IOException {
                String responseStr = resp.body().string();

                JsonElement element = JsonParser.parseString(responseStr);
                JsonObject object = element.getAsJsonObject();

                Log.d("ahoy", "netsnip: " + responseStr);

                nickname = object.get("user_info").getAsJsonObject().get("nickname").getAsString();

                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return nickname;
    }
}
