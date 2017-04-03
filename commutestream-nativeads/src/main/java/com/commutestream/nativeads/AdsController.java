package com.commutestream.nativeads;

public class AdsController {

    private Client mClient;

    AdsController(Client client) {
        mClient = client;
    }

    AdsController() {
        mClient = new HttpClient();
    }
}
