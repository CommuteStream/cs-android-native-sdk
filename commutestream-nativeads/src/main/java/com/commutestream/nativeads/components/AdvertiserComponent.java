package com.commutestream.nativeads.components;

import com.commutestream.nativeads.protobuf.Csnmessages;

public class AdvertiserComponent implements Component {
    private long componentID;
    private String advertiser;

    public AdvertiserComponent(Csnmessages.AdvertiserComponent msg) {
        componentID = msg.getComponentId();
        advertiser = msg.getAdvertiser();
    }

    @Override
    public long getComponentID() {
        return componentID;
    }

    public String getAdvertiser() {
        return advertiser;
    }
}
