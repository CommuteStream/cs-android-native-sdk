package com.commutestream.nativeads;

import com.commutestream.nativeads.components.SecondaryActionComponent;
import com.commutestream.nativeads.protobuf.Csnmessages;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SecondaryActionComponentTest {
    @Test
    public void newFromMessage() {
        long componentID = 5054;
        String title = "secondary title";
        String subtitle = "secondary subtitle";
        Csnmessages.SecondaryActionComponent msg = Csnmessages.SecondaryActionComponent.newBuilder()
                .setComponentId(componentID)
                .setTitle(title)
                .setSubtitle(subtitle)
                .build();
        SecondaryActionComponent secAction = new SecondaryActionComponent(msg);
        assertThat(secAction.getComponentID(), equalTo(componentID));
        assertThat(secAction.getTitle(), equalTo(title));
        assertThat(secAction.getSubtitle(), equalTo(subtitle));
    }
}
