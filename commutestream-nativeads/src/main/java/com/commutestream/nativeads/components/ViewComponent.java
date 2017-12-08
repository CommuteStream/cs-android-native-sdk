package com.commutestream.nativeads.components;

import com.commutestream.nativeads.protobuf.Csnmessages;

public class ViewComponent implements Component {
    protected long componentID;

    public ViewComponent(Csnmessages.ViewComponent msg) {
        componentID = msg.getComponentId();
    }

    protected ViewComponent() {
    }

    @Override
    public long getComponentID() {
        return componentID;
    }

    public static class Builder extends ViewComponent {
        public Builder setComponentID(long componentID) {
            this.componentID = componentID;
            return this;
        }

        public ViewComponent build() {
            return this;
        }
    }
}
