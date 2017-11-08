package com.commutestream.nativeads;

import com.commutestream.nativeads.components.BodyComponent;
import com.commutestream.nativeads.protobuf.Csnmessages;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class BodyComponentTest {
    @Test
    public void newFromMessage() {
        long componentID = 42;
        String bodyText = "the body";
        Csnmessages.BodyComponent msg = Csnmessages.BodyComponent.newBuilder()
                .setComponentId(componentID)
                .setBody(bodyText)
                .build();
        BodyComponent body = new BodyComponent(msg);
        assertThat(body.getComponentID(), equalTo(componentID));
        assertThat(body.getBody(), equalTo(bodyText));
    }
}
