package com.xlip.pegasusflightchecker.httpClient;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpClient {
    private Gson gson;


    private static AsyncHttpClient client;
    private static OkHttpClient oClient;
    private static List<Cookie> cookies = new ArrayList<>();


    public HttpClient(Context context) {
        client = new AsyncHttpClient();
        client.setTimeout(20 * 1000);
        client.addHeader("Content-Type", "application/json");
        client.addHeader("x-version", "1.7.0");
        client.addHeader("x-platform", "web");

        PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
        client.setCookieStore(myCookieStore);


        oClient = new OkHttpClient().newBuilder()
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        HttpClient.cookies.addAll(cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        return cookies;
                    }
                })
                .build();
        gson = new Gson();
    }

    public Cookie createNonPersistentCookie(HttpUrl url) {
        return new Cookie.Builder()
                .domain(url.uri().getHost())
                .path("/")
                .name("cookie-name")
                .value("cookie-value")
                .httpOnly()
                .secure()
                .build();
    }

    public void post(Context context, String url, String payload, String operation, final HttpClientCallbacks clientCallbacks) {
        StringEntity entity = null;
        try {
            entity = new StringEntity(payload);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        client.post(context, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                clientCallbacks.responseReceived(response, operation);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("HTTPCLIENT", "code: " + statusCode);
                Log.d("HTTPCLIENT", "code: " + statusCode);
            }
        });

    }

    public void postForm(String url, HashMap<String, String> params, String operation, final HttpClientCallbacks clientCallbacks) {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        for (String key : params.keySet()) {
            if (params.get(key) != null) {
                requestBodyBuilder.addFormDataPart(key, params.get(key));

            }

        }
        RequestBody requestBody = requestBodyBuilder.build();


        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("x-version", "1.7.0")
                .addHeader("x-platform", "web")
                .addHeader("Cache-Control", "no-cache")
                .build();

        try {
            Response response = oClient.newCall(request).execute();
            try {
                clientCallbacks.responseReceived(new JSONObject(response.body().string()), operation);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void postJson(String uri, String payload, String operation, HttpClientCallbacks clientCallbacks) {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, payload);
        Request.Builder requestBuilder = new Request.Builder()
                .url(uri)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("x-version", "1.9.1")
                .addHeader("x-platform", "web")
                .addHeader("Cache-Control", "no-cache");

        if (operation.equals("pay")) {
            requestBuilder.addHeader("origin", "https://web.flypgs.com/q+M4ho5tymm/UX6+h7eDgPxhw3fLY71KipfdzHPzTfw7pEPdfGO0sv782RXjn8a7iUgSvY9kt8UojsXIz34JPv0o+v+naYwfs4N3RI5EEGHt86WWtN9uLRNQPKMnKP/iXGvL8qceOzk=; _abck=0D0C6E89C1AC82467303A7F21FF0DD6F~-1~YAAQ4neUUpRiAdhrAQAA3sgpBgKkLeIh5l6r0h34zy9OEBUIOgE3+ntNrwAH+OF5KOwoKmRuHvVQZduhStAhEPYdenRyNihxttgqVDA/cCXaZDH7P5a52F4/jym6HjWjVX5ffhxtzTpcILGjlnXokRj+bXBCXNWeuKOTRyDZwwpCl3FyGYhQdAlaxcNP50hy3RR4mo95pHKdPsyJ26RqJ/QCpPpAddIJgwwMv4ZKZB/+nb6vWFXNbMgNLYCmUqW54TjFSwO8OlrghURNjVwYTIsiZYhpom5+vQ3F/A==~-1~-1~-1; dtSa=-; dtLatC=80; ak_bmsc=08F18FE695B383525CB095A9F32930AA529477E2231E000089AE305D7D42D07B~plzvxitN/HL/g67gWWMMyY5sR5pQbgViJ2rHw/w1H/km1YwqkbIMalKA+l/fCT6K2WcAWdpUm5MkLMQYGNo0gbmDYYWs1PVByPUcjrcNvaZtBFMYoC8bgX5Lm0BqdOe5UmYNM4Y/DkGMwtE5oe4U3uiLBW2/a/bc6u/FhSSTFTkQpGiHJvfT52/hFWFoXH2aghtJ6N/47x/5mncZNF1ZtekeR36kMI8l9eKLsr/xNHoxZuich7CxC5VyxDFroL5/B978TgnChS3xdQ7G+kWQ76yF8IEfC9DWfEXAtVni6eyUT/T0sS8V11/goDUaoawH4cIXsjhDgBw0AUg08w60vxIw==; eightdigits_last_operation_time=1563471498532; _gaexp=GAX1.2.qU-sAdN6TYKEx3Ct5IxKmQ.18131.2!3EVDiQIQQJe7BfX2AZdTfg.18137.1; dtPC=471497496_494h4");

        }

        Request request = requestBuilder.build();

        Log.d("HttpClient, Request: ", payload);
        try {
            Response response = oClient.newCall(request).execute();
            try {
                clientCallbacks.responseReceived(new JSONObject(response.body().string()), operation);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void putJson(String uri, String payload, String operation, HttpClientCallbacks clientCallbacks) {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, payload);
        Request request = new Request.Builder()
                .url(uri)
                .put(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("x-version", "1.9.1")
                .addHeader("x-platform", "web")
                .addHeader("Cache-Control", "no-cache")
                .build();
        Log.d("HttpClient, Request: ", payload);
        try {
            Response response = oClient.newCall(request).execute();
            try {
                clientCallbacks.responseReceived(new JSONObject(response.body().string()), operation);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void testPay( HttpClientCallbacks clientCallbacks) {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"creditCardPayment\":{\"optionIdentifier\":\"MTQ5Ng==\",\"holder\":\"ALİ UZUNYOL\",\"number\":\"4111111111111111\",\"expireDateMonth\":\"07\",\"expireDateYear\":\"2022\",\"cvv\":\"123\",\"fee\":{\"currency\":\"USD\",\"amount\":415.95}},\"pnrId\":\"NTA4Mzg3OTUx\",\"pnrNo\":\"95PKDF\",\"surName\":\"UZUNYOL\",\"operationType\":\"SellPnr\",\"optionExtensionId\":null,\"smsSelected\":false}");
        Request request = new Request.Builder()
                .url("https://web.flypgs.com/pegasus/payment/complete")
                .put(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("x-version", "1.9.1")
                .addHeader("x-platform", "web")
                .addHeader("Cache-Control", "no-cache")
                .build();
        Log.d("HttpClient, Request: ", body.toString());
        try {
            Response response = oClient.newCall(request).execute();
            try {
                clientCallbacks.responseReceived(new JSONObject(response.body().string()), "testPay");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void getJson(String uri, String operation, HttpClientCallbacks clientCallbacks) {
        MediaType mediaType = MediaType.parse("application/json");
        Request request = new Request.Builder()
                .url(uri)
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("x-version", "1.9.1")
                .addHeader("x-platform", "web")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("user-agent:", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36 OPR/62.0.3331.66")
                .build();
        Log.d("HttpClient, Request: ", uri);

        try {
            Response response = oClient.newCall(request).execute();
            try {
                clientCallbacks.responseReceived(new JSONObject(response.body().string()), operation);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public interface HttpClientCallbacks {
        void responseReceived(JSONObject response, String operation);
    }
}
