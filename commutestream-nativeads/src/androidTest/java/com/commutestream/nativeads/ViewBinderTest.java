package com.commutestream.nativeads;


import org.junit.Test;

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
