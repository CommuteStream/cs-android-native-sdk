package com.commutestream.nativeads.components;

import android.media.Image;

import com.commutestream.nativeads.protobuf.Csnmessages;

public class HeroComponent implements Component {
    public static final String HERO_HTML = "html";
    public static final String HERO_IMAGE = "image";
    private long componentID;
    private String kind;
    private Image image;
    private String html;

    public HeroComponent(Csnmessages.HeroComponent hero) {
        //TODO
    }

    @Override
    public long getComponentID() {
        return componentID;
    }
}
