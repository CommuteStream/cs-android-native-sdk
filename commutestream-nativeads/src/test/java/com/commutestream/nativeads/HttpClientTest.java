package com.commutestream.nativeads;

import com.commutestream.nativeads.protobuf.Csnmessages.DeviceID;
import com.commutestream.nativeads.protobuf.Csnmessages.TransitAgency;
import com.commutestream.nativeads.protobuf.Csnmessages.NativeAd;
import com.commutestream.nativeads.protobuf.Csnmessages.AdRequest;
import com.commutestream.nativeads.protobuf.Csnmessages.AdResponse;
import com.commutestream.nativeads.protobuf.Csnmessages.AdRequests;
import com.commutestream.nativeads.protobuf.Csnmessages.AdResponses;
import com.google.protobuf.ByteString;

import org.junit.Test;


import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;


import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class HttpClientTest {

    class TestAdResponseHandler implements Client.AdResponseHandler {
        int response_count = 0;
        int failure_count = 0;
        AdResponses responses = null;
        @Override
        public void onResponse(AdResponses responses) {
            synchronized (this) {
                this.responses = responses;
                this.response_count += 1;
                notifyAll();
            }
        }
        @Override
        public void onFailure() {
            synchronized (this) {
                this.failure_count += 1;
                notifyAll();
            }
        }
    }

    class TestAdReportsHandler implements Client.AdReportsHandler {
        int response_count = 0;
        int failure_count = 0;
        @Override
        public void onResponse() {
            synchronized (this) {
                this.response_count += 1;
                notifyAll();
            }
        }
        @Override
        public void onFailure() {
            synchronized (this) {
                this.failure_count += 1;
                notifyAll();
            }
        }
    }

    @Test
    public void testDefaultContructor() throws Exception {
        HttpClient mClient = new HttpClient();
        HttpUrl baseUrl = mClient.getBaseURL();
        assertThat(baseUrl.scheme(), equalTo("https"));
        assertThat(baseUrl.host(), equalTo("api.commutestream.com"));
    }

    @Test
    public void testBaseUrlContructor() throws Exception {
        HttpUrl baseUrl = new HttpUrl.Builder().scheme("https").host("testhost").build();
        HttpClient mClient = new HttpClient(baseUrl);
        HttpUrl clientBaseUrl = mClient.getBaseURL();
        assertThat(clientBaseUrl.scheme(), equalTo("https"));
        assertThat(clientBaseUrl.host(), equalTo("testhost"));
    }

    @Test
    public void getAdsSuccess() throws Exception {
        int adID = (int) (Math.random()*1000.0);
        int versionID = (int) (Math.random()*1000.0);
        int requestID = (int) (Math.random()*1000.0);
        byte[] hashID = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
        byte[] adUnitID = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
        byte[] deviceID = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
        String timezone = "America/Chicago";
        NativeAd ad1 = NativeAd.newBuilder().setAdId(adID)
                .setVersionId(versionID)
                .setRequestId(requestID)
                .build();
        AdRequest request = AdRequest.newBuilder()
                .setHashId(ByteString.copyFrom(hashID))
                .setNumOfAds(1)
                .addAgencies(TransitAgency.newBuilder().setAgencyId("test").build())
                .build();
        AdRequests requests = AdRequests.newBuilder()
                .setAdUnit(ByteString.copyFrom(adUnitID))
                .setDeviceId(DeviceID.newBuilder()
                        .setDeviceIdType(DeviceID.Type.AAID)
                        .setDeviceId(ByteString.copyFrom(deviceID))
                        .build())
                .setTimezone(timezone)
                .addAdRequests(request)
                .build();
        AdResponse response = AdResponse.newBuilder()
                .setHashId(ByteString.copyFrom(hashID))
                .addAds(ad1)
                .build();
        AdResponses responses = AdResponses.newBuilder()
                .setServerId("0")
                .addAdResponses(response)
                .build();
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/x-cs-protobuf")
                .setBody(responses.toString()));
        HttpUrl baseUrl = server.url("/");
        HttpClient client = new HttpClient(baseUrl);
        TestAdResponseHandler handler = new TestAdResponseHandler();
        client.getAds(requests, (Client.AdResponseHandler) handler);
        synchronized (handler) {
            handler.wait(100);
        }
        assertThat(handler.responses, notNullValue());
    }
}