package com.commutestream.nativetestapp;

import com.commutestream.nativeads.Ad;
import com.commutestream.nativeads.AdRenderer;
import com.commutestream.nativeads.AdsController;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.commutestream.nativeads.CSNLog;
import com.commutestream.nativeads.reporting.ReportEngine;
import com.commutestream.nativeads.SecondaryPopUp;
import com.commutestream.nativeads.ViewBinder;
import com.commutestream.nativeads.VisibilityMonitor;
import com.commutestream.nativeads.components.ActionComponent;
import com.commutestream.nativeads.components.BodyComponent;
import com.commutestream.nativeads.components.AdvertiserComponent;
import com.commutestream.nativeads.components.Colors;
import com.commutestream.nativeads.components.HeadlineComponent;
import com.commutestream.nativeads.components.HeroComponent;
import com.commutestream.nativeads.components.LogoComponent;
import com.commutestream.nativeads.components.SecondaryActionComponent;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static android.view.ViewGroup.*;

public class MainActivity extends AppCompatActivity {

    private AdsController adsController;

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
        final ActionComponent action1 = new ActionComponent.Builder()
                .setComponentID(0)
                .setTitle("Wikipedia")
                .setKind(ActionComponent.ACTION_KIND_URL)
                .setUrl("https://wikipedia.com")
                .setColors(new Colors(0xffffffff, 0xff0e9047))
                .build();
        ActionComponent action2 = new ActionComponent.Builder()
                .setComponentID(0)
                .setTitle("Google")
                .setKind(ActionComponent.ACTION_KIND_URL)
                .setUrl("https://google.com")
                .setColors(new Colors(0xffffffff, 0xff137dd0))
                .build();
        ActionComponent action3 = new ActionComponent.Builder()
                .setComponentID(0)
                .setTitle("Yahoo")
                .setKind(ActionComponent.ACTION_KIND_URL)
                .setUrl("https://yahoo.com")
                .setColors(new Colors(0xffffffff, 0xffedab1d))
                .build();
        HeadlineComponent headlineComponent = new HeadlineComponent.Builder()
                .setComponentID(1)
                .setHeadline("Look and feel your best")
                .build();
        BodyComponent bodyComponent = new BodyComponent.Builder()
                .setComponentID(1)
                .setBody("Get a free massage with purchase of any other service.")
                .build();

        AdvertiserComponent advertiserComponent = new AdvertiserComponent.Builder()
                .setComponentID(1)
                .setAdvertiser("Hand and Stone Massage")
                .build();

        ActionComponent[] actionComponents = new ActionComponent[3];
        actionComponents[0] = action1;
        actionComponents[1] = action2;
        actionComponents[2] = action3;
        SecondaryActionComponent secondaryComponent = new SecondaryActionComponent.Builder()
                .setTitle("Near Clark and Lake")
                .setSubtitle("200 W Lake St is in Chicago")
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
                .setAdvertiser(advertiserComponent)
                .setLogo(logoComponent)
                .setSecondaryAction(secondaryComponent)
                .setHero(heroComponent)
                .setActions(actionComponents)
                .build();
        AdRenderer r = new AdRenderer(this);
        final View view = r.render(null, ad, viewBinder);
        final LinearLayout mainLayout = findViewById(R.id.main_layout);
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
        HashSet<InetAddress> ipAddrs = new HashSet();
        List<NetworkInterface> interfaces = null;
        try {
            interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
        } catch (Exception ex) {
            interfaces = new ArrayList<>();
        }
        for (NetworkInterface intf : interfaces) {
            for(InetAddress addr : Collections.list(intf.getInetAddresses())) {
                if(!addr.isLoopbackAddress()) {
                    ipAddrs.add(addr);
                    CSNLog.v("Ip Address: " + addr.toString());
                }
            }
        }
        ReportEngine reportEngine = new ReportEngine(UUID.randomUUID(), UUID.randomUUID(), false, ipAddrs);
        VisibilityMonitor visMonitor = new VisibilityMonitor(reportEngine);
        visMonitor.addView(view, ad, logoComponent);
        visMonitor.startMonitoring();
        final Activity activity = this;
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainLayout.removeView(view);
                    }
                });
            }
        };
        timer.schedule(timerTask, 10000);

        adsController = new AdsController(this, UUID.randomUUID());

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
