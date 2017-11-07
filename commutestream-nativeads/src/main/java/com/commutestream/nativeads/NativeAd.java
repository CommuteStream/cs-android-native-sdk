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

public class NativeAd {
    private long adID;
    private long versionID;
    private Colors colors;
    private ActionComponent[] actions;
    private ViewComponent view;
    private SecondaryActionComponent secondaryAction;
    private LogoComponent logo;
    private HeadlineComponent headline;
    private BodyComponent body;
    private AdvertiserComponent advertiser;
    private LocationComponent location;
    private HeroComponent hero;

    public NativeAd(Csnmessages.NativeAd ad) {
        adID = ad.getAdId();
        versionID = ad.getVersionId();
        colors = new Colors(ad.getColors());
        //TODO actions
        view = new ViewComponent(ad.getView());
        secondaryAction = new SecondaryActionComponent(ad.getSecondaryActionScreen());
        logo = new LogoComponent(ad.getLogo());
        headline = new HeadlineComponent(ad.getHeadline());
        body = new BodyComponent(ad.getBody());
        advertiser = new AdvertiserComponent(ad.getAdvertiser());
        location = new LocationComponent(ad.getLocation());
        hero = new HeroComponent(ad.getHero());
    }
}
