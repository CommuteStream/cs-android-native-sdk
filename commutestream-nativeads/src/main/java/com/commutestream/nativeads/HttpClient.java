package com.commutestream.nativeads;

import com.commutestream.nativeads.protobuf.Csnmessages.AdResponses;
import com.commutestream.nativeads.protobuf.Csnmessages.AdRequests;
import com.commutestream.nativeads.protobuf.Csnmessages.AdReports;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HttpClient implements Client {

    private HttpUrl mBaseURL = new HttpUrl.Builder().scheme("https").host("api.commutestream.com").build();
    private OkHttpClient mClient;
    private boolean mAsync = true;

    HttpClient() {
        init();
    }

    HttpClient(HttpUrl baseURL) {
        mBaseURL = baseURL;
        init();
    }

    public void setAsync(boolean async) {
        mAsync = async;
    }

    public HttpUrl getBaseURL() {
        return mBaseURL;
    }

    private void init() {
        mClient = new OkHttpClient.Builder().addInterceptor(new HttpLogger()).build();
    }

    private void performRequest(Request request, Callback callback) {
        Call call = mClient.newCall(request);
        if(!mAsync) {
            try {
                Response response = call.execute();
                try {
                    callback.onResponse(call, response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                callback.onFailure(call, e);
            }
        } else {
            call.enqueue(callback);
        }
    }

    @Override
    public void getAds(AdRequests requests, final AdResponseHandler handler) {
        HttpUrl url = mBaseURL.newBuilder("/v2/native_ads")
                .build();
        RequestBody body = RequestBody.create(MediaType.parse("application/x-protobuf"), requests.toByteArray());
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
                if(response.code() == 200) {
                    AdResponses responses = AdResponses.parseFrom(response.body().bytes());
                    handler.onResponse(responses);
                } else {
                    handler.onFailure();
                }
            }
        };
        performRequest(request, callback);
    }

    @Override
    public void sendReports(AdReports reports, final AdReportsHandler handler) {
        HttpUrl url = mBaseURL.newBuilder("/v2/native_reports")
                .build();
        RequestBody body = RequestBody.create(MediaType.parse("application/x-protobuf"), reports.toByteArray());
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
                if(response.code() == 204) {
                    handler.onResponse();
                } else {
                    handler.onFailure();
                }
            }
        };
        performRequest(request, callback);
    }
}
