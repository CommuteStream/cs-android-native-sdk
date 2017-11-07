package com.commutestream.nativeads.components;

import android.media.Image;

import com.commutestream.nativeads.protobuf.Csnmessages;

public class LogoComponent implements Component {
    private long componentID;
    private Image logo;

    public LogoComponent(Csnmessages.LogoComponent logo) {
        //TODO
    }

    @Override
    public long getComponentID() {
        return componentID;
    }
}
