package com.commutestream.nativeads.components;

import com.commutestream.nativeads.protobuf.Csnmessages;

public class ActionComponent implements Component {
    public static final String ACTION_KIND_URL = "url";
    private long componentID;
    private String kind;
    private String url;
    private String title;
    private Colors colors;

    public ActionComponent(Csnmessages.ActionComponent action) {
        //TODO
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