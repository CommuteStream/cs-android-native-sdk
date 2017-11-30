package com.commutestream.nativeads;


import android.view.View;

import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getContext;

public class AdRendererTest {
    @Test
    public void newAdRenderer() {
        ViewBinder viewBinder = new ViewBinder(R.layout.test_layout);
        Ad ad = new Ad();
        AdRenderer r = new AdRenderer(getContext());
        View view = r.render(viewGroup, ad, viewBinder);
    }
}