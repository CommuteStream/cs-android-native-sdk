package com.commutestream.nativeads.components;

import com.commutestream.nativeads.protobuf.Csnmessages;

public class ViewComponent implements Component {
    private long componentID;

    public ViewComponent(Csnmessages.ViewComponent msg) {
        componentID = msg.getComponentId();
    }

    @Override
    public long getComponentID() {
        return componentID;
    }
}
