package com.commutestream.nativeads;

import android.security.NetworkSecurityPolicy;

import com.commutestream.nativeads.protobuf.Csnmessages.DeviceID;
import com.commutestream.nativeads.protobuf.Csnmessages.TransitAgency;
import com.commutestream.nativeads.protobuf.Csnmessages.NativeAd;
import com.commutestream.nativeads.protobuf.Csnmessages.AdRequest;
import com.commutestream.nativeads.protobuf.Csnmessages.AdResponse;
import com.commutestream.nativeads.protobuf.Csnmessages.AdRequests;
import com.commutestream.nativeads.protobuf.Csnmessages.AdResponses;
import com.commutestream.nativeads.protobuf.Csnmessages.AdReport;
import com.commutestream.nativeads.protobuf.Csnmessages.AdReports;
import com.google.protobuf.ByteString;

import org.junit.Test;


import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;


import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.Buffer;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, shadows = HttpClientTest.NetworkSecurityPolicyWorkaround.class)
public class HttpClientTest {

    @Implements(NetworkSecurityPolicy.class)
    public static class NetworkSecurityPolicyWorkaround {
        @Implementation public static NetworkSecurityPolicy getInstance() {
            try {
                Class<?> shadow = Class.forName("android.security.NetworkSecurityPolicy");
                return (NetworkSecurityPolicy) shadow.newInstance();
            } catch (Exception e) {
                throw new AssertionError();
            }
        }

        @Implementation public boolean isCleartextTrafficPermitted(String hostname) {
            return true;
        }
    }

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
        Buffer body = new Buffer();
        body.write(responses.toByteArray());

        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/x-cs-protobuf")
                .setBody(body));
        HttpUrl baseUrl = server.url("/");
        HttpClient client = new HttpClient(baseUrl);
        client.setAsync(false);
        TestAdResponseHandler handler = new TestAdResponseHandler();
        client.getAds(requests, handler);
        assertThat(handler.response_count, greaterThan(0));
        assertThat(handler.responses, notNullValue());
    }

    @Test
    public void getAdsFailure() throws Exception {
        byte[] hashID = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
        byte[] adUnitID = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
        byte[] deviceID = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
        String timezone = "America/Chicago";
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
        MockWebServer server = new MockWebServer();
        HttpUrl baseUrl = server.url("/");
        HttpClient client = new HttpClient(baseUrl);
        client.setAsync(false);

        int[] codes = {400, 500, 503};
        for(int code : codes) {
            server.enqueue(new MockResponse()
                    .setResponseCode(code));
            TestAdResponseHandler handler = new TestAdResponseHandler();
            client.getAds(requests, handler);
            assertThat(handler.failure_count, greaterThan(0));
        }
        server.shutdown();
        TestAdResponseHandler handler = new TestAdResponseHandler();
        client.getAds(requests, handler);
        assertThat(handler.failure_count, greaterThan(0));
    }

    @Test
    public void sendReportsSuccess() throws Exception {
        int adID = (int) (Math.random()*1000.0);
        int versionID = (int) (Math.random()*1000.0);
        int requestID = (int) (Math.random()*1000.0);
        byte[] adUnitID = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
        byte[] deviceID = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
        String timezone = "America/Chicago";
        AdReport report = AdReport.newBuilder()
                .setAdId(adID)
                .setRequestId(requestID)
                .setVersionId(versionID)
                .build();
        AdReports reports = AdReports.newBuilder()
                .setAdUnit(ByteString.copyFrom(adUnitID))
                .setDeviceId(DeviceID.newBuilder()
                        .setDeviceIdType(DeviceID.Type.AAID)
                        .setDeviceId(ByteString.copyFrom(deviceID))
                        .build())
                .setTimezone(timezone)
                .addAdReports(report)
                .build();
        MockWebServer server = new MockWebServer();
        Buffer body = new Buffer();
        body.write(reports.toByteArray());

        server.enqueue(new MockResponse()
                .setResponseCode(204));
        HttpUrl baseUrl = server.url("/");
        HttpClient client = new HttpClient(baseUrl);
        client.setAsync(false);
        TestAdReportsHandler handler = new TestAdReportsHandler();
        client.sendReports(reports, handler);
        assertThat(handler.response_count, greaterThan(0));
    }

    @Test
    public void sendReportsFailure() throws Exception {
        int adID = (int) (Math.random()*1000.0);
        int versionID = (int) (Math.random()*1000.0);
        int requestID = (int) (Math.random()*1000.0);
        byte[] adUnitID = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
        byte[] deviceID = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
        String timezone = "America/Chicago";
        AdReport report = AdReport.newBuilder()
                .setAdId(adID)
                .setRequestId(requestID)
                .setVersionId(versionID)
                .build();
        AdReports reports = AdReports.newBuilder()
                .setAdUnit(ByteString.copyFrom(adUnitID))
                .setDeviceId(DeviceID.newBuilder()
                        .setDeviceIdType(DeviceID.Type.AAID)
                        .setDeviceId(ByteString.copyFrom(deviceID))
                        .build())
                .setTimezone(timezone)
                .addAdReports(report)
                .build();
        MockWebServer server = new MockWebServer();
        HttpUrl baseUrl = server.url("/");
        HttpClient client = new HttpClient(baseUrl);
        client.setAsync(false);

        int[] codes = {400, 500, 503};
        for(int code : codes) {
            server.enqueue(new MockResponse()
                    .setResponseCode(code));
            TestAdReportsHandler handler = new TestAdReportsHandler();
            client.sendReports(reports, handler);
            assertThat(handler.failure_count, greaterThan(0));
        }
        server.shutdown();
        TestAdReportsHandler handler = new TestAdReportsHandler();
        client.sendReports(reports, handler);
        assertThat(handler.failure_count, greaterThan(0));
    }
}