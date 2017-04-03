package com.commutestream.nativeads;

import com.commutestream.nativeads.protobuf.Csnmessages.AdReport;
import com.commutestream.nativeads.protobuf.Csnmessages.AdRequest;
import com.commutestream.nativeads.protobuf.Csnmessages.AdResponse;

public interface Client {

    public interface AdResponseHandler {
        void onResponse(AdResponse reponse);
    }

    public void getAds(AdRequest request, AdResponseHandler handler);



    public interface AdReportsHandler {
        void onResponse();
    }

    public void sendReports(AdReport report, AdReportsHandler handler);

}
