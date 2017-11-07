package com.commutestream.nativeads.components;

import com.commutestream.nativeads.protobuf.Csnmessages;

public class HeadlineComponent implements Component {
    private long componentID;
    private String headline;

    public HeadlineComponent(Csnmessages.HeadlineComponent headline) {
        //TODO
    }

    @Override
    public long getComponentID() {
        return componentID;
    }
}
