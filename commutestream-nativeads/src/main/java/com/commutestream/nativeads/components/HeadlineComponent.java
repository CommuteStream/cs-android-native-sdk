package com.commutestream.nativeads.components;

import com.commutestream.nativeads.protobuf.Csnmessages;

public class HeadlineComponent implements Component {
    protected long componentID;
    protected String headline;

    protected HeadlineComponent() {
    }

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

    public static class Builder extends HeadlineComponent {
        public Builder setComponentID(long componentID) {
            this.componentID = componentID;
            return this;
        }

        public Builder setHeadline(String headline) {
            this.headline = headline;
            return this;
        }

        public HeadlineComponent build() {
            return this;
        }
    }
}
