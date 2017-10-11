package com.commutestream.nativeads;

import com.commutestream.nativeads.protobuf.Csnmessages.AdRequest;
import com.commutestream.nativeads.protobuf.Csnmessages.AdReport;
import com.commutestream.nativeads.protobuf.Csnmessages.AdResponse;

class MockClient implements Client {
    @Override
    public void getAds(AdRequest request, AdResponseHandler handler) {
        AdResponse.Builder responseBuilder = AdResponse.newBuilder();
        AdResponse response = responseBuilder.build();
        handler.onResponse(response);
    }

    @Override
    public void sendReports(AdReport report, AdReportsHandler handler) {
        handler.onResponse();
    }
}
