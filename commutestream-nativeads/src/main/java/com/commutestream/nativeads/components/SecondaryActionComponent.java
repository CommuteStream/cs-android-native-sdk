package com.commutestream.nativeads.components;

import com.commutestream.nativeads.protobuf.Csnmessages;

public class SecondaryActionComponent implements Component {
    protected long componentID;
    protected String title;
    protected String subtitle;

    public SecondaryActionComponent(Csnmessages.SecondaryActionComponent msg) {
        componentID = msg.getComponentId();
        title = msg.getTitle();
        subtitle = msg.getSubtitle();
    }

    protected SecondaryActionComponent() {
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

    public static class Builder extends SecondaryActionComponent {
        public Builder setComponentID(long componentID) {
            this.componentID = componentID;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setSubtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public SecondaryActionComponent build() {
            return this;
        }
    }
}
