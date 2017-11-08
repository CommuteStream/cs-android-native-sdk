package com.commutestream.nativeads.components;

import com.commutestream.nativeads.protobuf.Csnmessages;

public class SecondaryActionComponent implements Component {
    private long componentID;
    private String title;
    private String subtitle;

    public SecondaryActionComponent(Csnmessages.SecondaryActionComponent msg) {
        componentID = msg.getComponentId();
        title = msg.getTitle();
        subtitle = msg.getSubtitle();
    }

    @Override
    public long getComponentID() {
        return componentID;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}
