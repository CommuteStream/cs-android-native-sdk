package com.commutestream.nativeads;

import android.support.test.filters.LargeTest;

import android.support.test.runner.AndroidJUnit4;

import com.commutestream.nativeads.protobuf.Csnmessages;
import com.google.protobuf.ByteString;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static android.support.test.InstrumentationRegistry.getContext;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AdControllerTest {
    @Test
    public void testDefaultConstructor() {
        AdsController adsController = new AdsController(getContext(), UUID.randomUUID());
    }

    @Test
    public void testFetchAdsNone() {
        MockClient mockClient = new MockClient();
        AdsController adsController = new AdsController(getContext(), mockClient, UUID.randomUUID());
        AdRequest adRequest = new AdRequest();
        adRequest.addAgency("bogus");
        ArrayList<AdRequest> adRequests = new ArrayList<>();
        adRequests.add(adRequest);
        adsController.fetchAds(adRequests, new AdsController.AdResponseHandler() {
            @Override
            public void onAds(List<Ad> ads) {
                assertThat(ads, notNullValue());
                assertThat(ads.size(), equalTo(1));
                assertThat(ads.get(0), nullValue());
            }
        });
    }

    @Test
    public void testFetchAdsOne() throws NoSuchAlgorithmException {
        MockClient mockClient = new MockClient();
        AdsController adsController = new AdsController(getContext(), mockClient, UUID.randomUUID());
        AdRequest adRequest = new AdRequest();
        adRequest.addAgency("bogus");
        ArrayList<AdRequest> adRequests = new ArrayList<>();
        adRequests.add(adRequest);
        Csnmessages.AdResponses adResponses = Csnmessages.AdResponses.newBuilder()
                .setServerId("specialid")
                .addAdResponses(addMockAd(responseBuilder(adRequest)).build())
                .build();
        mockClient.addAdsResponse(adResponses);
        adsController.fetchAds(adRequests, new AdsController.AdResponseHandler() {
            @Override
            public void onAds(List<Ad> ads) {
                assertThat(ads, notNullValue());
                assertThat(ads.size(), equalTo(1));
                assertThat(ads.get(0), notNullValue());
            }
        });
    }

    @Test
    public void testFetchAdsDuplicates() {
        MockClient mockClient = new MockClient();
        AdsController adsController = new AdsController(getContext(), mockClient, UUID.randomUUID());
        AdRequest adRequest = new AdRequest();
        adRequest.addAgency("bogus");
        ArrayList<AdRequest> adRequests = new ArrayList<>();
        adRequests.add(adRequest);
        adRequests.add(adRequest);
        adsController.fetchAds(adRequests, new AdsController.AdResponseHandler() {
            @Override
            public void onAds(List<Ad> ads) {
                assertThat(ads, notNullValue());
                assertThat(ads.size(), equalTo(2));
            }
        });
    }

    @Test
    public void testFetchAdsComplex() {
        MockClient mockClient = new MockClient();
        AdsController adsController = new AdsController(getContext(), mockClient, UUID.randomUUID());
        AdRequest adRequest1 = new AdRequest();
        adRequest1.addAgency("palmer");
        AdRequest adRequest2 = new AdRequest();
        adRequest2.addAgency("arnold");
        ArrayList<AdRequest> adRequests = new ArrayList<>();
        adRequests.add(adRequest1);
        adRequests.add(adRequest2);
        adRequests.add(adRequest1);
        adRequests.add(adRequest2);
        adsController.fetchAds(adRequests, new AdsController.AdResponseHandler() {
            @Override
            public void onAds(List<Ad> ads) {
                assertThat(ads, notNullValue());
                assertThat(ads.size(), equalTo(4));
            }
        });
    }

    private Csnmessages.AdResponse.Builder responseBuilder(AdRequest adRequest) throws NoSuchAlgorithmException {
        return Csnmessages.AdResponse.newBuilder()
                .setHashId(ByteString.copyFrom(adRequest.sha256()));
    }

    private Csnmessages.AdResponse.Builder addMockAd(Csnmessages.AdResponse.Builder responseBuilder) {
        responseBuilder.addAds(Csnmessages.NativeAd.newBuilder()
                .setRequestId(new Random().nextLong())
                .setAdId(new Random().nextLong())
                .setVersionId(new Random().nextLong())
                .build());
        return responseBuilder;
    }
}
