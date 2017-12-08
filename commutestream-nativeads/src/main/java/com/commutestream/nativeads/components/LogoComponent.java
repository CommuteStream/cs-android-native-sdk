package com.commutestream.nativeads.components;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.commutestream.nativeads.protobuf.Csnmessages;

public class LogoComponent implements Component {
    protected long componentID;
    protected Bitmap logo;

    public LogoComponent(Csnmessages.LogoComponent msg) {
        componentID = msg.getComponentId();
        logo = BitmapFactory.decodeByteArray(msg.getImage().toByteArray(), 0, msg.getImage().size());
    }

    protected LogoComponent() {
    }

    @Override
    public long getComponentID() {
        return componentID;
    }

    public Bitmap getLogo() {
        return logo;
    }

    public static class Builder extends LogoComponent {
        public Builder setComponentID(long componentID) {
            this.componentID = componentID;
            return this;
        }

        public Builder setLogo(Bitmap logo) {
            this.logo = logo;
            return this;
        }

        public LogoComponent build() {
            return this;
        }
    }
}
