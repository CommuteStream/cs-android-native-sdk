package com.commutestream.nativeads;

import com.commutestream.nativeads.components.AdvertiserComponent;
import com.commutestream.nativeads.protobuf.Csnmessages;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class AdvertiserComponentTest {
    @Test
    public void newFromMessage() {
        long componentID = 55;
        String advertiser = "super advertiser";
        Csnmessages.AdvertiserComponent msg = Csnmessages.AdvertiserComponent.newBuilder()
                .setComponentId(componentID)
                .setAdvertiser(advertiser)
                .build();
        AdvertiserComponent action = new AdvertiserComponent(msg);
        assertThat(action.getComponentID(), equalTo(componentID));
        assertThat(action.getAdvertiser(), equalTo(advertiser));
    }
}
