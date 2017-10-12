package com.commutestream.nativeads;

import com.commutestream.nativeads.protobuf.Csnmessages.AdReports;
import com.commutestream.nativeads.protobuf.Csnmessages.AdRequests;
import com.commutestream.nativeads.protobuf.Csnmessages.AdResponses;

public interface Client {
    interface AdResponseHandler {
        void onResponse(AdResponses responses);
        void onFailure();
    }

    void getAds(AdRequests requests, AdResponseHandler handler);

    interface AdReportsHandler {
        void onResponse();
        void onFailure();
    }

    void sendReports(AdReports reports, AdReportsHandler handler);
}
