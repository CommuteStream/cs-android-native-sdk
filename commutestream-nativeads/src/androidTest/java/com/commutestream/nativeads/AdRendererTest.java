package com.commutestream.nativeads;


import android.drm.DrmStore;
import android.view.View;
import android.view.ViewGroup;

import com.commutestream.nativeads.components.ActionComponent;
import com.commutestream.nativeads.components.HeadlineComponent;

import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getContext;

public class AdRendererTest {
    @Test
    public void newAdRenderer() {
        ViewBinder viewBinder = new ViewBinder(R.layout.test_layout);
        Ad.Builder adBuilder = new Ad.Builder();
        ActionComponent action1 = new ActionComponent.Builder()
                                .setComponentID(0)
                                .setTitle("Click Me")
                                .setKind(ActionComponent.ACTION_KIND_URL)
                                .setUrl("https://wikipedia.com")
                                .build();
        HeadlineComponent headlineComponent = new HeadlineComponent.Builder()
                .setComponentID(1)
                .setHeadline("The Headline")
                .build();
        ActionComponent[] actionComponents = new ActionComponent[1];
        actionComponents[0] = action1;
        Ad ad = adBuilder.setAdID(1)
                .setRequestID(1)
                .setVersionID(1)
                .setHeadline(headlineComponent)
                .setActions(actionComponents)
                .build();
        AdRenderer r = new AdRenderer(getContext());
        View view = r.render(null, ad, viewBinder);
    }
}