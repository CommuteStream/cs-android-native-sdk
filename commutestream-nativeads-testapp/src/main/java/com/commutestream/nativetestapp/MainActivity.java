package com.commutestream.nativetestapp;

import com.commutestream.nativeads.Ad;
import com.commutestream.nativeads.AdRenderer;
import com.commutestream.nativeads.AdsController;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.commutestream.nativeads.SecondaryPopUp;
import com.commutestream.nativeads.ViewBinder;
import com.commutestream.nativeads.components.ActionComponent;
import com.commutestream.nativeads.components.BodyComponent;
import com.commutestream.nativeads.components.Colors;
import com.commutestream.nativeads.components.HeadlineComponent;
import com.commutestream.nativeads.components.HeroComponent;
import com.commutestream.nativeads.components.LogoComponent;
import com.commutestream.nativeads.components.SecondaryActionComponent;

import static android.view.ViewGroup.*;

public class MainActivity extends AppCompatActivity {

    private AdsController mAdsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // render fake
        ViewBinder viewBinder = new ViewBinder(R.layout.adlayout)
                .setHeadline(R.id.headlineView)
                .setLogo(R.id.logoView)
                .setBody(R.id.bodyView);
        Ad.Builder adBuilder = new Ad.Builder();
        ActionComponent action1 = new ActionComponent.Builder()
                .setComponentID(0)
                .setTitle("Wikipedia")
                .setKind(ActionComponent.ACTION_KIND_URL)
                .setUrl("https://wikipedia.com")
                .setColors(new Colors(R.color.colorAccent, R.color.colorPrimaryDark))
                .build();
        ActionComponent action2 = new ActionComponent.Builder()
                .setComponentID(0)
                .setTitle("Google")
                .setKind(ActionComponent.ACTION_KIND_URL)
                .setUrl("https://google.com")
                .setColors(new Colors(R.color.colorAccent, R.color.colorPrimaryDark))
                .build();
        ActionComponent action3 = new ActionComponent.Builder()
                .setComponentID(0)
                .setTitle("Yahoo")
                .setKind(ActionComponent.ACTION_KIND_URL)
                .setUrl("https://yahoo.com")
                .setColors(new Colors(R.color.colorAccent, R.color.colorPrimaryDark))
                .build();
        HeadlineComponent headlineComponent = new HeadlineComponent.Builder()
                .setComponentID(1)
                .setHeadline("The Headline")
                .build();
        BodyComponent bodyComponent = new BodyComponent.Builder()
                .setComponentID(1)
                .setBody("The Body")
                .build();
        ActionComponent[] actionComponents = new ActionComponent[3];
        actionComponents[0] = action1;
        actionComponents[1] = action2;
        actionComponents[2] = action3;
        SecondaryActionComponent secondaryComponent = new SecondaryActionComponent.Builder()
                .setTitle("Near Clark and Lake")
                .setSubtitle("200 W Lake St.")
                .build();
        LogoComponent logoComponent = new LogoComponent.Builder()
                .setComponentID(2)
                .setLogo(BitmapFactory.decodeResource(getResources(), R.drawable.test_logo))
                .build();
        HeroComponent heroComponent = new HeroComponent.Builder()
                .setComponentID(3)
                .setKind(HeroComponent.HERO_IMAGE)
                .setImage(BitmapFactory.decodeResource(getResources(), R.drawable.test_hero))
                .build();
        final Ad ad = adBuilder.setAdID(1)
                .setRequestID(1)
                .setVersionID(1)
                .setHeadline(headlineComponent)
                .setBody(bodyComponent)
                .setLogo(logoComponent)
                .setSecondaryAction(secondaryComponent)
                .setHero(heroComponent)
                .setActions(actionComponents)
                .build();
        AdRenderer r = new AdRenderer(this);
        View view = r.render(null, ad, viewBinder);
        LinearLayout mainLayout = findViewById(R.id.main_layout);
        Button showPopup = findViewById(R.id.show_popup);
        final SecondaryPopUp popup = new SecondaryPopUp(this);
        showPopup.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup.displayAd(ad);
                    }
                }
        );
        mainLayout.addView(view);
        //mAdsController = new AdsController();

        // get nearbytransit


        //with nearby transit
        //mAdsController.fetchAds(adContexts, new FetchAdsHandler {
        //void onResponse() {
         /// build out ad
        // AdContext adContext = new AdContext();
        //adContext.addStop("cta", "redline", "jackson");
        //mAdsController.buildAd(adParentView, adContext)
    }
}
