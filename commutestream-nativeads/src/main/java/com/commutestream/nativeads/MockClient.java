package com.commutestream.nativeads;

import com.commutestream.nativeads.protobuf.Csnmessages;

class MockClient implements Client {
    @Override
    public void getAds(Csnmessages.AdRequest request, AdResponseHandler handler) {

    }

    @Override
    public void sendReports(Csnmessages.AdReport report, AdReportsHandler handler) {

    }
}
