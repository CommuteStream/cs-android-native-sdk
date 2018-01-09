package com.commutestream.nativeads.components;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.commutestream.nativeads.CSNLog;
import com.commutestream.nativeads.protobuf.Csnmessages;

public class HeroComponent implements Component {
    protected long componentID;
    protected HeroKind kind;
    protected Bitmap image;
    protected String html;

    public HeroComponent(Csnmessages.HeroComponent msg) {
        componentID = msg.getComponentId();
        if (msg.getKind() == Csnmessages.HeroKind.HTML) {
            kind = HeroKind.HTML;
            html = msg.getBlob().toStringUtf8();
        } else if (msg.getKind() == Csnmessages.HeroKind.Image) {
            kind = HeroKind.Image;
            image = BitmapFactory.decodeByteArray(msg.getBlob().toByteArray(), 0, msg.getBlob().size());
        }
    }

    protected HeroComponent() {
    }

    @Override
    public long getComponentID() {
        return componentID;
    }

    public HeroKind getKind() {
        return kind;
    }

    public String getHtml() {
        return html;
    }

    public Bitmap getImage() {
        return image;
    }

    public static class Builder extends HeroComponent {
        public Builder setComponentID(long componentID) {
            this.componentID = componentID;
            return this;
        }

        public Builder setKind(HeroKind kind) {
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
