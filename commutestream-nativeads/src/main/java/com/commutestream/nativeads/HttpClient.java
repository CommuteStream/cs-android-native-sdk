package com.commutestream.nativeads;

import android.util.Log;

import com.commutestream.nativeads.protobuf.Csnmessages.AdRequest;
import com.commutestream.nativeads.protobuf.Csnmessages.AdReport;

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
    public void getAds(AdRequest request, AdResponseHandler handler) {

    }

    @Override
    public void sendReports(AdReport report, AdReportsHandler handler) {

    }
}
