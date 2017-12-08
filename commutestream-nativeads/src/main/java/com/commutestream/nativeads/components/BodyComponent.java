package com.commutestream.nativeads.components;

import com.commutestream.nativeads.protobuf.Csnmessages;

public class BodyComponent implements Component {
    protected long componentID;
    protected String body;

    protected BodyComponent() {
    }

    public BodyComponent(Csnmessages.BodyComponent msg) {
        componentID = msg.getComponentId();
        body = msg.getBody();
    }

    public final static class Builder extends BodyComponent {
        public Builder setComopnentID(long componentID) {
            this.componentID = componentID;
            return this;
        }

        public Builder setBody(String body) {
            this.body = body;
            return this;
        }
    }

    @Override
    public long getComponentID() {
        return componentID;
    }

    public String getBody() {
        return body;
    }
}
