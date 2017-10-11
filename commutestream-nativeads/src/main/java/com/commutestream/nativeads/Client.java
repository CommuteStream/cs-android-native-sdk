package com.commutestream.nativeads;

import com.commutestream.nativeads.protobuf.Csnmessages.AdReport;
import com.commutestream.nativeads.protobuf.Csnmessages.AdRequest;
import com.commutestream.nativeads.protobuf.Csnmessages.AdResponse;

public interface Client {
    interface AdResponseHandler {
        void onResponse(AdResponse reponse);
    }

    void getAds(AdRequest request, AdResponseHandler handler);

    interface AdReportsHandler {
        void onResponse();
    }

    void sendReports(AdReport report, AdReportsHandler handler);
}
