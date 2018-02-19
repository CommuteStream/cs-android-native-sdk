package com.commutestream.nativeads;


import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class ViewBinderTest {
    @Test
    public void newViewBinder() {
        ViewBinder v = new ViewBinder(R.layout.csn_test_ad_layout)
                .setLogo(R.id.csn_test_ad_logo)
                .setHeadline(R.id.csn_test_ad_headline)
                .setBody(R.id.csn_test_ad_body)
                .setAdvertiser(R.id.csn_test_ad_advertiser);
        //TODO assert bindings
    }
}
