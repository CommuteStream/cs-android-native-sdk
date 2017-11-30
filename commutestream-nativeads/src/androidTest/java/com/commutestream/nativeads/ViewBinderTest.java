package com.commutestream.nativeads;


import org.junit.Test;

public class ViewBinderTest {
    @Test
    public void newViewBinder() {
        ViewBinder v = new ViewBinder(R.layout.test_layout);
        v.setLogo(R.id.native_ad_logo);
        v.setTitle(R.id.native_ad_title);
    }
}
