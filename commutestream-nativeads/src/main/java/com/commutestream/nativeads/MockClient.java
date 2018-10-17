package com.commutestream.nativeads;

import com.commutestream.nativeads.protobuf.Csnmessages.AdRequests;
import com.commutestream.nativeads.protobuf.Csnmessages.AdReports;
import com.commutestream.nativeads.protobuf.Csnmessages.AdResponses;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.NoSuchElementException;

class MockClient implements Client {

    ArrayDeque<AdResponses> adsResponses = new ArrayDeque<>();
    ArrayDeque<Boolean> reportsResponses = new ArrayDeque<>();
    ArrayDeque<HashSet<String>> marketsResponses = new ArrayDeque<>();

    public void addAdsResponse(AdResponses adResponses) {
        adsResponses.push(adResponses);
    }

    public void addReportsResponse(boolean success) {
        reportsResponses.push(success);
    }

    public void addMarketsResponse(HashSet<String> markets) { marketsResponses.push(markets); }


    @Override
    public void getAds(AdRequests request, AdResponseHandler handler) {
        AdResponses responses = adsResponses.pop();
        if(responses != null) {
            handler.onResponse(responses);
        } else {
            handler.onFailure();
        }
    }

    @Override
    public void sendReports(AdReports report, AdReportsHandler handler) {
        Boolean reportsResponse = reportsResponses.pop();
        if(reportsResponse != null && reportsResponse == true) {
            handler.onResponse();
        } else {
            handler.onFailure();
        }
    }

    @Override
    public void getMarkets(MarketsHandler handler) {
        HashSet<String> marketsResponse = marketsResponses.pop();
        if (marketsResponse != null) {
            handler.onResponse(marketsResponse);
        } else {
            handler.onFailure();
        }
    }
}
