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
        ViewBinder v = new ViewBinder(R.layout.test_layout)
                .setLogo(R.id.native_ad_logo)
                .setHeadline(R.id.native_ad_headline)
                .setBody(R.id.native_ad_body);
        //TODO assert bindings
    }
}
