package com.commutestream.nativeads;

import android.util.Log;

import com.commutestream.nativeads.protobuf.Csnmessages;

public class HttpClient implements Client {

    private String mHostname = "api.commutestream.com";

    HttpClient() {
        Log.i("CS Native Ads", "HttpClient: default contructor");
    }

    HttpClient(String hostname) {
        mHostname = hostname;
    }

    public String getHostname() {
        return mHostname;
    }

    @Override
    public void getAds(Csnmessages.AdRequest request, AdResponseHandler handler) {

    }

    @Override
    public void sendReports(Csnmessages.AdReport report, AdReportsHandler handler) {

    }
}
