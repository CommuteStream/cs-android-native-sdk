package com.commutestream.nativeads;

import com.commutestream.nativeads.protobuf.Csnmessages.AdReports;
import com.commutestream.nativeads.protobuf.Csnmessages.AdRequests;
import com.commutestream.nativeads.protobuf.Csnmessages.AdResponses;

import java.util.HashSet;


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

    interface MarketsHandler {
        void onResponse(HashSet<String> markets);
        void onFailure();
    }

    void getMarkets(MarketsHandler handler);
}
