package com.commutestream.nativeads;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AdControllerTest {
    @Test
    public void testDefaultContructor() {
        AdsController adsController = new AdsController();
    }

    @Test
    public void testFetchAds() {
        MockClient mockClient = new MockClient();
        AdsController adsController = new AdsController(mockClient);
        ArrayList<AdRequest> adRequests = new ArrayList<>();
        adsController.fetchAds(adRequests, new AdsController.AdResponseHandler() {
            @Override
            public void onAds(List<Ad> ads) {
            }
        });
    }
}
