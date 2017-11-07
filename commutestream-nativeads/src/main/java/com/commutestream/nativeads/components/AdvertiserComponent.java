package com.commutestream.nativeads.components;

import com.commutestream.nativeads.protobuf.Csnmessages;

public class AdvertiserComponent implements Component {
    private long componentID;
    private String headline;

    public AdvertiserComponent(Csnmessages.AdvertiserComponent advertiser) {

    }

    @Override
    public long getComponentID() {
        return componentID;
    }

    public String getHeadline() {
        return headline;
    }
}
