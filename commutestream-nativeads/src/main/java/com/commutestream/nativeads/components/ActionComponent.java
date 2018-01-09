package com.commutestream.nativeads.components;

import com.commutestream.nativeads.protobuf.Csnmessages;

public class ActionComponent implements Component {
    protected long componentID;
    protected ActionKind kind;
    protected String url;
    protected String title;
    protected Colors colors;


    public static class Builder extends ActionComponent {

        public Builder setComponentID(long componentID) {
            this.componentID = componentID;
            return this;
        }

        public Builder setKind(ActionKind kind) {
            this.kind = kind;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setColors(Colors colors) {
            this.colors = colors;
            return this;
        }

        public ActionComponent build() {
            return this;
        }
    }

    protected ActionComponent() {
    }

    public ActionComponent(Csnmessages.ActionComponent msg) {
        componentID = msg.getComponentId();
        switch (msg.getKind()) {
            case Url: kind = ActionKind.Url;
            default: kind = ActionKind.Url;;
        }
        url = msg.getUrl();
        title = msg.getTitle();
        colors = new Colors(msg.getColors());
    }

    @Override
    public long getComponentID() {
        return componentID;
    }

    public ActionKind getKind() {
        return kind;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public Colors getColors() {
        return colors;
    }
}