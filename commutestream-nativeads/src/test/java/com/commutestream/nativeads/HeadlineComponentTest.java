package com.commutestream.nativeads;

import com.commutestream.nativeads.components.HeadlineComponent;
import com.commutestream.nativeads.protobuf.Csnmessages;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class HeadlineComponentTest {
    @Test
    public void newFromMessage() {
        long componentID = 403;
        String headlineText = "the headline is here";
        Csnmessages.HeadlineComponent msg = Csnmessages.HeadlineComponent.newBuilder()
                .setComponentId(componentID)
                .setHeadline(headlineText)
                .build();
        HeadlineComponent headline = new HeadlineComponent(msg);
        assertThat(headline.getComponentID(), equalTo(componentID));
        assertThat(headline.getHeadline(), equalTo(headlineText));

    }
}
