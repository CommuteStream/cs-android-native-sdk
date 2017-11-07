package com.commutestream.nativeads;

import com.commutestream.nativeads.protobuf.Csnmessages.AdRequests;
import com.commutestream.nativeads.protobuf.Csnmessages.AdReports;
import com.commutestream.nativeads.protobuf.Csnmessages.AdResponses;

import java.util.ArrayDeque;
import java.util.Queue;

class MockClient implements Client {

    Queue<AdResponses> mAdsResponses = new ArrayDeque<>();
    Queue<Boolean> mReportsResponses = new ArrayDeque<>();

    @Override
    public void getAds(AdRequests request, AdResponseHandler handler) {
        AdResponses responses = mAdsResponses.remove();
        if(responses != null) {
            handler.onResponse(responses);
        } else {
            handler.onFailure();
        }
    }

    @Override
    public void sendReports(AdReports report, AdReportsHandler handler) {
        Boolean reportsResponse = mReportsResponses.remove();
        if(reportsResponse != null) {
            handler.onResponse();
        } else {
            handler.onFailure();
        }
    }
}
