package com.commutestream.nativeads;

import com.commutestream.nativeads.components.Colors;
import com.commutestream.nativeads.protobuf.Csnmessages;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class ColorsTest {
    @Test
    public void testPack() {
        int red = 11;
        int green = 12;
        int blue = 13;
        int packed = Colors.pack(red, green, blue);
        assertThat(Colors.unpackRed(packed), equalTo(red));
        assertThat(Colors.unpackGreen(packed), equalTo(green));
        assertThat(Colors.unpackBlue(packed), equalTo(blue));
    }

    @Test
    public void newFromMessage() {
        Csnmessages.Color fgmsg = Csnmessages.Color.newBuilder()
                .setRed(11)
                .setGreen(12)
                .setBlue(13)
                .build();
        Csnmessages.Color bgmsg = Csnmessages.Color.newBuilder()
                .setRed(21)
                .setGreen(22)
                .setBlue(23)
                .build();
        Csnmessages.Colors msg = Csnmessages.Colors.newBuilder()
                .setForeground(fgmsg)
                .setBackground(bgmsg)
                .build();
        Colors colors = new Colors(msg);
        int fgpacked = Colors.pack(11,12,13);
        int bgpacked = Colors.pack(21,22,23);
        assertThat(colors.getForeground(), equalTo(fgpacked));
        assertThat(colors.getBackground(), equalTo(bgpacked));
    }
}
