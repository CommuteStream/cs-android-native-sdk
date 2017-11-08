package com.commutestream.nativeads;

import com.commutestream.nativeads.components.ViewComponent;
import com.commutestream.nativeads.protobuf.Csnmessages;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ViewComponentTest {
    @Test
    public void newFromMessage() {
        long componentID = 55;
        Csnmessages.ViewComponent msg = Csnmessages.ViewComponent.newBuilder()
                .setComponentId(componentID)
                .build();
        ViewComponent view = new ViewComponent(msg);
        assertThat(view.getComponentID(), equalTo(componentID));
    }
}
