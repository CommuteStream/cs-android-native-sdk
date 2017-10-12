package com.commutestream.nativeads;


import android.util.Log;

import com.commutestream.nativeads.protobuf.Csnmessages;
import com.commutestream.nativeads.protobuf.Csnmessages.AdRequests;
import com.commutestream.nativeads.protobuf.Csnmessages.AdReports;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class HttpClient implements Client {

    private HttpUrl mBaseURL = new HttpUrl.Builder().scheme("https").host("api.commutestream.com").build();
    private OkHttpClient mClient;

    HttpClient() {
        init();
    }

    HttpClient(HttpUrl baseURL) {
        mBaseURL = baseURL;
        init();
    }

    public HttpUrl getBaseURL() {
        return mBaseURL;
    }

    private void init() {
        mClient = new OkHttpClient.Builder().addInterceptor(new HttpLogger()).build();
    }

    @Override
    public void getAds(AdRequests requests, final AdResponseHandler handler) {
        HttpUrl url = mBaseURL.newBuilder("/v2/native_ads")
                .build();
        RequestBody body = RequestBody.create(MediaType.parse("application/x-cs-protobuf"), "test");
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //TODO decode responses
                handler.onResponse(null);
            }
        };
        mClient.newCall(request).enqueue(callback);
    }

    @Override
    public void sendReports(AdReports reports, final AdReportsHandler handler) {
        HttpUrl url = mBaseURL.newBuilder("/v2/native_reports")
                .build();
        RequestBody body = RequestBody.create(MediaType.parse("application/x-cs-protobuf"), "test");
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handler.onResponse();
            }
        };
        mClient.newCall(request).enqueue(callback);
    }
}
