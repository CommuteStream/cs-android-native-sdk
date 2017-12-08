package com.commutestream.nativeads.components;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.commutestream.nativeads.CSNLog;
import com.commutestream.nativeads.protobuf.Csnmessages;

public class HeroComponent implements Component {
    public static final String HERO_HTML = "html";
    public static final String HERO_IMAGE = "image";
    protected long componentID;
    protected String kind;
    protected Bitmap image;
    protected String html;

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

    protected HeroComponent() {
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

    public static class Builder extends HeroComponent {
        public Builder setComponentID(int componentID) {
            this.componentID = componentID;
            return this;
        }

        public Builder setKind(String kind) {
            this.kind = kind;
            return this;
        }

        public Builder setImage(Bitmap image) {
            this.image = image;
            return this;
        }

        public Builder setHtml(String html) {
            this.html = html;
            return this;
        }

        public HeroComponent build() {
            return this;
        }
    }
}
