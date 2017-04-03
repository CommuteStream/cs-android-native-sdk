package com.commutestream.nativeads;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.equalTo;


import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class HttpClientTest {
    @Test
    public void testDefaultContructor() throws Exception {
        HttpClient mClient = new HttpClient();
        assertThat(mClient.getHostname(), equalTo("api.commutestream.com"));
    }

    @Test
    public void testHostnameContructor() throws Exception {
        HttpClient mClient = new HttpClient("testhost");
        assertThat(mClient.getHostname(), equalTo("testhost"));
    }
}