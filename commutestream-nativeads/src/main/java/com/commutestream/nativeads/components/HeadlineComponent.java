package com.commutestream.nativeads.components;

import com.commutestream.nativeads.protobuf.Csnmessages;

public class HeadlineComponent implements Component {
    private long componentID;
    private String headline;

    public HeadlineComponent(Csnmessages.HeadlineComponent msg) {
        componentID = msg.getComponentId();
        headline = msg.getHeadline();
    }

    @Override
    public long getComponentID() {
        return componentID;
    }

    public String getHeadline() {
        return headline;
    }
}
