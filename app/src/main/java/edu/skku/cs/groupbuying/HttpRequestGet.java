package edu.skku.cs.groupbuying;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpRequestGet {
    //final private static String server_adrs = "http://10.0.2.2:5000";
    final private static String server_adrs = "http://52.78.137.254:8080";

    private boolean success;

    private OkHttpClient client;
    private HttpUrl.Builder urlBuilder;

    private String response;

    public HttpRequestGet(String route) {
        client = new OkHttpClient();
        urlBuilder = HttpUrl.parse(server_adrs + route).newBuilder();
    }

    public void addQueryParam(String key, String value) {
        urlBuilder.addQueryParameter(key, value);
    }

    public String sendRequest() {
        String url = urlBuilder.build().toString();
        Request req = new Request.Builder().url(url).build();

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