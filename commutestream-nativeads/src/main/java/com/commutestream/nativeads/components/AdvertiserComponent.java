package com.commutestream.nativeads.components;

import com.commutestream.nativeads.Ad;
import com.commutestream.nativeads.protobuf.Csnmessages;

public class AdvertiserComponent implements Component {
    protected long componentID;
    protected String advertiser;

    protected AdvertiserComponent() {
    }

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

    public static class Builder extends AdvertiserComponent {
        public Builder setComponentID(long componentID) {
            this.componentID = componentID;
            return this;
        }

        public Builder setAdvertiser(String advertiser) {
            this.advertiser = advertiser;
            return this;
        }

        public AdvertiserComponent build() {
            return this;
        }
    }
}
