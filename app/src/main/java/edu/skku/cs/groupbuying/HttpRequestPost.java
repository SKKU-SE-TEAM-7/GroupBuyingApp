package edu.skku.cs.groupbuying;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpRequestPost {
    final private static String server_adrs = "http://10.0.2.2:5000";

    private boolean success;

    private OkHttpClient client;
    private HttpUrl.Builder urlBuilder;

    private String response;

    public HttpRequestPost(String route) {
        client = new OkHttpClient();
        urlBuilder = HttpUrl.parse(server_adrs + route).newBuilder();
    }

    public String sendRequest(String jsonString) {
        String url = urlBuilder.build().toString();
        Request req = new Request.Builder().url(url).post(RequestBody.create(jsonString, MediaType.parse("application/json"))).build();

        String response = "";

        CallbackFuture future = new CallbackFuture();
        client.newCall(req).enqueue(future);
        try {
            Response resp = future.get();
            response = resp.body().string();
        } catch (Exception e) {
        }

        return response;
    }
}

class CallbackFuture extends CompletableFuture<Response> implements Callback {
    public void onResponse(Call call, Response response) { super.complete(response); }
    public void onFailure(Call call, IOException e) { super.completeExceptionally(e); }
}