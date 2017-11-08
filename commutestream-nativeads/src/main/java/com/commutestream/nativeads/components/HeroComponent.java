package com.commutestream.nativeads.components;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.commutestream.nativeads.CSNLog;
import com.commutestream.nativeads.protobuf.Csnmessages;

public class HeroComponent implements Component {
    public static final String HERO_HTML = "html";
    public static final String HERO_IMAGE = "image";
    private long componentID;
    private String kind;
    private Bitmap image;
    private String html;

    public HeroComponent(Csnmessages.HeroComponent msg) {
        componentID = msg.getComponentId();
        if (msg.getKind() == Csnmessages.HeroKind.HTML) {
            kind = HERO_HTML;
            html = msg.getBlob().toStringUtf8();
        } else if (msg.getKind() == Csnmessages.HeroKind.Image) {
            kind = HERO_IMAGE;
            image = BitmapFactory.decodeByteArray(msg.getBlob().toByteArray(), 0, msg.getBlob().size());
        }
    }

    @Override
    public long getComponentID() {
        return componentID;
    }

    public String getKind() {
        return kind;
    }

    public String getHtml() {
        return html;
    }

    public Bitmap getImage() {
        return image;
    }
}
