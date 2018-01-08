package com.commutestream.nativeads;

import com.commutestream.nativeads.components.ActionComponent;
import com.commutestream.nativeads.components.AdvertiserComponent;
import com.commutestream.nativeads.components.BodyComponent;
import com.commutestream.nativeads.components.Colors;
import com.commutestream.nativeads.components.HeadlineComponent;
import com.commutestream.nativeads.components.HeroComponent;
import com.commutestream.nativeads.components.LocationComponent;
import com.commutestream.nativeads.components.LogoComponent;
import com.commutestream.nativeads.components.SecondaryActionComponent;
import com.commutestream.nativeads.components.ViewComponent;
import com.commutestream.nativeads.protobuf.Csnmessages;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Ad {
    protected long requestID;
    protected long adID;
    protected long versionID;
    protected Colors colors;


    protected ArrayList<ActionComponent> actions;
    protected ViewComponent view;
    protected SecondaryActionComponent secondaryAction;
    protected LogoComponent logo;
    protected HeadlineComponent headline;
    protected BodyComponent body;
    protected AdvertiserComponent advertiser;
    protected LocationComponent location;
    protected HeroComponent hero;

    public static class Builder extends Ad {
        public Builder setRequestID(long requestID) {
            this.requestID = requestID;
            return this;
        }

        public Builder setAdID(long adID) {
            this.adID = adID;
            return this;
        }

        public Builder setVersionID(long versionID) {
            this.versionID = versionID;
            return this;
        }

        public Builder setColors(Colors colors) {
            this.colors = colors;
            return this;
        }

        public Builder setActions(ArrayList<ActionComponent> actions) {
            this.actions = actions;
            return this;
        }

        public Builder setView(ViewComponent view) {
            this.view = view;
            return this;
        }

        public Builder setSecondaryAction(SecondaryActionComponent secondaryAction) {
            this.secondaryAction = secondaryAction;
            return this;
        }

        public Builder setLogo(LogoComponent logo) {
            this.logo = logo;
            return this;
        }

        public Builder setHeadline(HeadlineComponent headline) {
            this.headline = headline;
            return this;
        }

        public Builder setBody(BodyComponent body) {
            this.body = body;
            return this;
        }

        public Builder setAdvertiser(AdvertiserComponent advertiser) {
            this.advertiser = advertiser;
            return this;
        }

        public Builder setLocation(LocationComponent location) {
            this.location = location;
            return this;
        }

        public Builder setHero(HeroComponent hero) {
            this.hero = hero;
            return this;
        }

        public Ad build() {
            return this;
        }
    }

    protected Ad() {
    }

    public Ad(Csnmessages.NativeAd ad) {
        requestID = ad.getRequestId();
        adID = ad.getAdId();
        versionID = ad.getVersionId();
        colors = new Colors(ad.getColors());
        actions = new ArrayList<>(3);
        view = new ViewComponent(ad.getView());
        secondaryAction = new SecondaryActionComponent(ad.getSecondaryActionScreen());
        logo = new LogoComponent(ad.getLogo());
        headline = new HeadlineComponent(ad.getHeadline());
        body = new BodyComponent(ad.getBody());
        advertiser = new AdvertiserComponent(ad.getAdvertiser());
        location = new LocationComponent(ad.getLocation());
        hero = new HeroComponent(ad.getHero());
        for (Csnmessages.ActionComponent action : ad.getActionsList()) {
            actions.add(new ActionComponent(action));
        }
    }

    public long getRequestID() {
        return requestID;
    }

    public long getAdID() {
        return adID;
    }

    public long getVersionID() {
        return versionID;
    }

    public Colors getColors() {
        return colors;
    }

    public List<ActionComponent> getActions() {
        return actions;
    }

    public ViewComponent getView() {
        return view;
    }

    public SecondaryActionComponent getSecondaryAction() {
        return secondaryAction;
    }

    public BodyComponent getBody() {
        return body;
    }

    public AdvertiserComponent getAdvertiser() {
        return advertiser;
    }

    public LocationComponent getLocation() {
        return location;
    }

    public HeroComponent getHero() {
        return hero;
    }

    public LogoComponent getLogo() {
        return logo;
    }

    public HeadlineComponent getHeadline() {
        return headline;
    }
}
