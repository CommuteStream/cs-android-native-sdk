package com.commutestream.nativeads.components;

import com.commutestream.nativeads.protobuf.Csnmessages;

public class BodyComponent implements Component {
    private long componentID;
    private String body;

    public BodyComponent(Csnmessages.BodyComponent msg) {
        componentID = msg.getComponentId();
        body = msg.getBody();
    }

    @Override
    public long getComponentID() {
        return componentID;
    }

    public String getBody() {
        return body;
    }
}
