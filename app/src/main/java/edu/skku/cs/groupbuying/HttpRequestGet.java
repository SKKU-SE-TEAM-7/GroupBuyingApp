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

    public synchronized String sendRequest() {
        boolean isFinished = false;

        String url = urlBuilder.build().toString();
        Request req = new Request.Builder().url(url).build();

        Log.d("ahoy", "get url: " + url);
        response = "";

        CallbackFuture future = new CallbackFuture();
        client.newCall(req).enqueue(future);
        try {
            Response resp = future.get();
            response = resp.body().string();
        //    isFinished = true;
        } catch (Exception e) {
            response = "non";
        }

        Log.d("ahoy", "response: " + response);
        return response;

        /*
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response resp) throws IOException {
                response = resp.body().string();
            }
        });*/

        /*
        try {
            Log.d("ahoy", "bfor call wait");
            call.wait();
            Log.d("ahoy", "call wait");
        } catch (InterruptedException e) {

            e.printStackTrace();
        }*/

        /*
        try {
            Response resp = client.newCall(req).execute();
            Log.d("ahoy", "after resp");

            response = resp.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        /*
        Log.d("ahoy", "in get: " + response);
        Log.d("ahoy", "bfor while");
        //while (response == "") {}
        Log.d("ahoy", "in get: " + response);

        return response;*/
    }
}