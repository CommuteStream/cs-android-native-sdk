package com.commutestream.nativeads;

import com.commutestream.nativeads.components.ActionComponent;
import com.commutestream.nativeads.components.Colors;
import com.commutestream.nativeads.protobuf.Csnmessages;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ActionComponentTest {
    @Test
    public void newFromMessage() {
        long componentID = 55;
        String title = "action title";
        String url = "https://wikipedia.com";
        Csnmessages.Colors colors = Csnmessages.Colors.newBuilder()
                .setForeground(Csnmessages.Color.newBuilder()
                        .setRed(11)
                        .setGreen(12)
                        .setBlue(13))
                .setBackground(Csnmessages.Color.newBuilder()
                        .setRed(21)
                        .setGreen(22)
                        .setBlue(23))
                .build();
        Csnmessages.ActionComponent msg = Csnmessages.ActionComponent.newBuilder()
                .setComponentId(componentID)
                .setKind(Csnmessages.ActionKind.Url)
                .setColors(colors)
                .setTitle(title)
                .setUrl(url)
                .build();
        ActionComponent action = new ActionComponent(msg);
        assertThat(action.getComponentID(), equalTo(componentID));
        assertThat(action.getKind(), equalTo(ActionComponent.ACTION_KIND_URL));
        assertThat(action.getTitle(), equalTo(title));
        assertThat(action.getUrl(), equalTo(url));
        Colors colors2 = action.getColors();
        int foreground = colors2.getForeground();
        int expectedForeground = Colors.pack(11, 12, 13);
        int background = colors2.getBackground();
        int expectedBackground = Colors.pack(21,22, 23);
        assertThat(foreground, equalTo(expectedForeground));
        assertThat(background, equalTo(expectedBackground));
    }
}
