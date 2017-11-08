package com.commutestream.nativeads.components;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.commutestream.nativeads.protobuf.Csnmessages;

public class LogoComponent implements Component {
    private long componentID;
    private Bitmap logo;

    public LogoComponent(Csnmessages.LogoComponent msg) {
        componentID = msg.getComponentId();
        logo = BitmapFactory.decodeByteArray(msg.getImage().toByteArray(), 0, msg.getImage().size());
    }

    @Override
    public long getComponentID() {
        return componentID;
    }

    public Bitmap getLogo() {
        return logo;
    }
}
