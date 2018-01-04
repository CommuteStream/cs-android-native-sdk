package com.commutestream.nativeads;

import android.support.test.filters.LargeTest;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.support.test.InstrumentationRegistry.getContext;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AdControllerTest {
    @Test
    public void testDefaultConstructor() {
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
                assertThat(ads, nullValue());
            }
        });
    }
}
