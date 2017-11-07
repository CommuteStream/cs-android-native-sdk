package com.commutestream.nativeads.components;

import com.commutestream.nativeads.protobuf.Csnmessages;

public class LocationComponent implements Component {
    private long componentID;

    public LocationComponent(Csnmessages.LocationComponent location) {
        //TODO
    }

    @Override
    public long getComponentID() {
        return componentID;
    }
}
