package com.commutestream.nativeads.components;

import android.view.View;

import com.commutestream.nativeads.protobuf.Csnmessages;

public class ViewComponent implements Component {
    private long componentID;

    public ViewComponent(Csnmessages.ViewComponent view) {
        //TODO
    }

    @Override
    public long getComponentID() {
        return componentID;
    }
}
