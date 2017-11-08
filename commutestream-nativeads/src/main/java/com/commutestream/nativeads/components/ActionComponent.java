package com.commutestream.nativeads.components;

import com.commutestream.nativeads.protobuf.Csnmessages;

public class ActionComponent implements Component {
    public static final String ACTION_KIND_URL = "url";
    private long componentID;
    private String kind;
    private String url;
    private String title;
    private Colors colors;

    public ActionComponent(Csnmessages.ActionComponent msg) {
        componentID = msg.getComponentId();
        switch (msg.getKind()) {
            case Url: kind = ACTION_KIND_URL;
            default: kind = ACTION_KIND_URL;
        }
        url = msg.getUrl();
        title = msg.getTitle();
        colors = new Colors(msg.getColors());
    }

    @Override
    public long getComponentID() {
        return componentID;
    }

    public String getKind() {
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