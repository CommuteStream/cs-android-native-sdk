package com.commutestream.nativeads;

import android.app.Activity;
import android.support.test.filters.LargeTest;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.commutestream.nativeads.protobuf.Csnmessages;
import com.commutestream.nativeads.test.TestActivity;
import com.google.protobuf.ByteString;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class AdControllerTest {

    @Rule
    public ActivityTestRule<TestActivity> mActivityRule = new ActivityTestRule<>(TestActivity.class);


    @Test
    public void testFetchAdsNone() {
        MockClient mockClient = new MockClient();
        mockClient.addMarketsResponse(new HashSet<String>(Arrays.asList("bogus")));

        AdsController adsController = new AdsController(mActivityRule.getActivity(), mockClient, UUID.randomUUID());
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
        mockClient.addMarketsResponse(new HashSet<String>(Arrays.asList("bogus")));

        AdsController adsController = new AdsController(mActivityRule.getActivity(), mockClient, UUID.randomUUID());
        AdRequest adRequest = new AdRequest();
        adRequest.addAgency("bogus");
        ArrayList<AdRequest> adRequests = new ArrayList<>();
        adRequests.add(adRequest);
        final Csnmessages.AdResponses adResponses = Csnmessages.AdResponses.newBuilder()
                .setServerId("specialid")
                .addAdResponses(addMockAd(responseBuilder(adRequest)).build())
                .build();
        mockClient.addAdsResponse(adResponses);
        adsController.fetchAds(adRequests, new AdsController.AdResponseHandler() {
            @Override
            public void onAds(List<Ad> ads) {
                assertThat(ads, notNullValue());
                assertThat(ads.size(), equalTo(1));
                Ad ad = ads.get(0);
                assertThat(ad, notNullValue());
                assertThat(ad.getAdID(), equalTo(adResponses.getAdResponses(0).getAds(0).getAdId()));
            }
        });
    }

    @Test
    public void testFetchAdsDuplicates() throws NoSuchAlgorithmException {
        MockClient mockClient = new MockClient();
        mockClient.addMarketsResponse(new HashSet<String>(Arrays.asList("bogus")));

        AdsController adsController = new AdsController(mActivityRule.getActivity(), mockClient, UUID.randomUUID());
        AdRequest adRequest = new AdRequest();
        adRequest.addAgency("bogus");
        ArrayList<AdRequest> adRequests = new ArrayList<>();
        adRequests.add(adRequest);
        adRequests.add(adRequest);
        final Csnmessages.AdResponses adResponses = Csnmessages.AdResponses.newBuilder()
                .setServerId("specialid")
                .addAdResponses(addMockAd(addMockAd(responseBuilder(adRequest))).build())
                .build();
        mockClient.addAdsResponse(adResponses);
        adsController.fetchAds(adRequests, new AdsController.AdResponseHandler() {
            @Override
            public void onAds(List<Ad> ads) {
                assertThat(ads, notNullValue());
                assertThat(ads.size(), equalTo(2));
                Ad ad1 = ads.get(0);
                Ad ad2 = ads.get(1);
                assertThat(ad1, notNullValue());
                assertThat(ad2, notNullValue());
                assertThat(ad1.getAdID(), equalTo(adResponses.getAdResponses(0).getAds(0).getAdId()));
                assertThat(ad2.getAdID(), equalTo(adResponses.getAdResponses(0).getAds(1).getAdId()));
            }
        });
    }

    @Test
    public void testFetchAdsComplex() throws NoSuchAlgorithmException {
        MockClient mockClient = new MockClient();
        mockClient.addMarketsResponse(new HashSet<String>(Arrays.asList("palmer", "arnold")));

        AdsController adsController = new AdsController(mActivityRule.getActivity(), mockClient, UUID.randomUUID());
        AdRequest adRequest1 = new AdRequest();
        adRequest1.addAgency("palmer");
        AdRequest adRequest2 = new AdRequest();
        adRequest2.addAgency("arnold");
        ArrayList<AdRequest> adRequests = new ArrayList<>();
        adRequests.add(adRequest1);
        adRequests.add(adRequest2);
        adRequests.add(adRequest1);
        adRequests.add(adRequest2);
        final Csnmessages.AdResponses adResponses = Csnmessages.AdResponses.newBuilder()
                .setServerId("specialid")
                .addAdResponses(addMockAd(addMockAd(responseBuilder(adRequest1))).build())
                .addAdResponses(addMockAd(responseBuilder(adRequest2)).build())
                .build();
        mockClient.addAdsResponse(adResponses);
        adsController.fetchAds(adRequests, new AdsController.AdResponseHandler() {
            @Override
            public void onAds(List<Ad> ads) {
                assertThat(ads, notNullValue());
                assertThat(ads.size(), equalTo(4));
                Ad ad1 = ads.get(0);
                Ad ad2 = ads.get(1);
                Ad ad3 = ads.get(2);
                Ad ad4 = ads.get(3);
                assertThat(ad1, notNullValue());
                assertThat(ad2, notNullValue());
                assertThat(ad3, notNullValue());
                assertThat(ad4, nullValue());
                assertThat(ad1.getAdID(), equalTo(adResponses.getAdResponses(0).getAds(0).getAdId()));
                assertThat(ad2.getAdID(), equalTo(adResponses.getAdResponses(1).getAds(0).getAdId()));
                assertThat(ad3.getAdID(), equalTo(adResponses.getAdResponses(0).getAds(1).getAdId()));
            }
        });
    }

    @Test
    public void testFetchAdsFilteredMarket() throws NoSuchAlgorithmException {
        MockClient mockClient = new MockClient();
        mockClient.addMarketsResponse(new HashSet<String>(Arrays.asList("arnold")));

        AdsController adsController = new AdsController(mActivityRule.getActivity(), mockClient, UUID.randomUUID());
        AdRequest adRequest1 = new AdRequest();
        adRequest1.addAgency("palmer");
        ArrayList<AdRequest> adRequests = new ArrayList<>();
        adRequests.add(adRequest1);
        adsController.fetchAds(adRequests, new AdsController.AdResponseHandler() {
            @Override
            public void onAds(List<Ad> ads) {
                assertThat(ads, notNullValue());
                assertThat(ads.size(), equalTo(1));
                Ad ad1 = ads.get(0);
                assertThat(ad1, nullValue());
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
