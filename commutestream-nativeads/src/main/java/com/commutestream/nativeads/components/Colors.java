package com.commutestream.nativeads.components;

import com.commutestream.nativeads.protobuf.Csnmessages;

public class Colors {
    private int foreground;
    private int background;

    public Colors(int foreground, int background) {
        this.foreground = foreground;
        this.background = background;
    }

    public Colors(Csnmessages.Colors msg) {
        Csnmessages.Color fgmsg = msg.getForeground();
        Csnmessages.Color bgmsg = msg.getBackground();
        foreground = Colors.pack(fgmsg.getRed(), fgmsg.getGreen(), fgmsg.getBlue());
        background = Colors.pack(bgmsg.getRed(), bgmsg.getGreen(), bgmsg.getBlue());
    }

    public static int pack(int red, int green, int blue) {
        return 0xff << 24 | (red & 0xff) << 16 | (green & 0xff) << 8 | (blue & 0xff);
    }

    public static int unpackRed(int color) {
        return (color >> 16) & 0xff;
    }

    public static int unpackGreen(int color) {
        return (color >> 8) & 0xff;
    }

    public static int unpackBlue(int color) {
        return color & 0xff;
    }

    public int getForeground() {
        return foreground;
    }

    public int getBackground() {
        return background;
    }
}
