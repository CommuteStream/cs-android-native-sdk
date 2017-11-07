package com.commutestream.nativeads.components;

import com.commutestream.nativeads.protobuf.Csnmessages;

public class SecondaryActionComponent implements Component {
    private long componentID;
    private String title;
    private String subtitle;

    public SecondaryActionComponent(Csnmessages.SecondaryActionComponent secondary) {
        //TODO
    }

    @Override
    public long getComponentID() {
        return componentID;
    }
}
