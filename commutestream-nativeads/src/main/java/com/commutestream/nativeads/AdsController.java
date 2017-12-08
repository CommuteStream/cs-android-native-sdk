package com.commutestream.nativeads;

import java.util.List;

public class AdsController {

    public interface AdResponseHandler {
        void onAds(List<Ad> ads);
    }

    private Client client;

    public AdsController(Client client) {
        this.client = client;
    }

    public AdsController() {
        client = new HttpClient();
    }

    public void fetchAds(List<AdRequest> requests, AdResponseHandler responseHandler) {
        responseHandler.onAds(null);
    }
}
