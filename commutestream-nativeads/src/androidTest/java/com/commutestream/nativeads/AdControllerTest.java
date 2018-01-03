package com.commutestream.nativeads;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.support.test.InstrumentationRegistry.getContext;

public class AdControllerTest {
    @Test
    public void testDefaultContructor() {
        AdsController adsController = new AdsController(getContext(), UUID.randomUUID());
    }

    @Test
    public void testFetchAds() {
        MockClient mockClient = new MockClient();
        AdsController adsController = new AdsController(getContext(), mockClient, UUID.randomUUID());
        ArrayList<AdRequest> adRequests = new ArrayList<>();
        adsController.fetchAds(adRequests, new AdsController.AdResponseHandler() {
            @Override
            public void onAds(List<Ad> ads) {
            }
        });
    }
}
