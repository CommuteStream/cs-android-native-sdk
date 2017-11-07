package com.commutestream.nativeads.components;

import com.commutestream.nativeads.protobuf.Csnmessages;

public class BodyComponent implements Component {
    private long componentID;
    private String body;

    public BodyComponent(Csnmessages.BodyComponent body) {
        //TODO
    }

    @Override
    public long getComponentID() {
        return componentID;
    }
}
