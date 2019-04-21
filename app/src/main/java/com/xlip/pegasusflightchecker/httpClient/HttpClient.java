package com.xlip.pegasusflightchecker.httpClient;

import android.content.Context;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpClient {
    private static HttpClient instance = new HttpClient();
    private Gson gson;

    public static HttpClient getInstance() {
        return instance;
    }

    private static AsyncHttpClient client;
    private static OkHttpClient oClient;


    public HttpClient() {
        client = new AsyncHttpClient();
        client.setTimeout(20 * 1000);
        client.addHeader("Content-Type", "application/json");
        client.addHeader("x-version", "1.7.0");
        client.addHeader("x-platform", "web");

        oClient = new OkHttpClient();
        gson = new Gson();
    }

    public void post(Context context, String url, String payload, final HttpClientCallbacks clientCallbacks) {
        StringEntity entity = null;
        try {
            entity = new StringEntity(payload);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        client.post(context, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                clientCallbacks.responseReceived(response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {

            }
        });

    }

    public void postO(String uri, String payload, HttpClientCallbacks clientCallbacks) {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, payload);
        Request request = new Request.Builder()
                .url(uri)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("x-version", "1.7.0")
                .addHeader("x-platform", "web")
                .addHeader("Cache-Control", "no-cache")
                .build();

        try {
            Response response = oClient.newCall(request).execute();
            try {
                clientCallbacks.responseReceived(new JSONObject(response.body().string()));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public interface HttpClientCallbacks {
        void responseReceived(JSONObject response);
    }
}
