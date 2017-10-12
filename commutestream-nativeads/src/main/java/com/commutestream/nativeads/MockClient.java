package com.commutestream.nativeads;

import com.commutestream.nativeads.protobuf.Csnmessages.AdRequests;
import com.commutestream.nativeads.protobuf.Csnmessages.AdReports;
import com.commutestream.nativeads.protobuf.Csnmessages.AdResponses;

class MockClient implements Client {
    @Override
    public void getAds(AdRequests request, AdResponseHandler handler) {
        AdResponses.Builder responseBuilder = AdResponses.newBuilder();
        AdResponses responses = responseBuilder.build();
        handler.onResponse(responses);
    }

    @Override
    public void sendReports(AdReports report, AdReportsHandler handler) {
        handler.onResponse();
    }
}
