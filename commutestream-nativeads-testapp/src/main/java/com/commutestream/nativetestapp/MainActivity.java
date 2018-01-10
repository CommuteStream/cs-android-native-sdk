package com.commutestream.nativetestapp;

import com.commutestream.nativeads.Ad;
import com.commutestream.nativeads.AdRenderer;
import com.commutestream.nativeads.AdsController;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.commutestream.nativeads.CSNLog;
import com.commutestream.nativeads.components.ActionKind;
import com.commutestream.nativeads.components.HeroKind;
import com.commutestream.nativeads.components.ViewComponent;
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
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static android.view.ViewGroup.*;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "Native Test App";

    private AdsController adsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // render fake
        Random rnd = new Random();
        ViewBinder viewBinder = new ViewBinder(R.layout.adlayout)
                .setHeadline(R.id.headlineView)
                .setLogo(R.id.logoView)
                .setBody(R.id.bodyView);
        Ad.Builder adBuilder = new Ad.Builder();
        final ActionComponent action1 = new ActionComponent.Builder()
                .setComponentID(rnd.nextLong())
                .setTitle("Wikipedia")
                .setKind(ActionKind.Url)
                .setUrl("https://wikipedia.com")
                .setColors(new Colors(0xffffffff, 0xff0e9047))
                .build();
        ViewComponent viewComponent = new ViewComponent.Builder()
                .setComponentID(rnd.nextLong())
                .build();
        ActionComponent action2 = new ActionComponent.Builder()
                .setComponentID(rnd.nextLong())
                .setTitle("Google")
                .setKind(ActionKind.Url)
                .setUrl("https://google.com")
                .setColors(new Colors(0xffffffff, 0xff137dd0))
                .build();
        ActionComponent action3 = new ActionComponent.Builder()
                .setComponentID(rnd.nextLong())
                .setTitle("Yahoo")
                .setKind(ActionKind.Url)
                .setUrl("https://yahoo.com")
                .setColors(new Colors(0xffffffff, 0xffedab1d))
                .build();
        HeadlineComponent headlineComponent = new HeadlineComponent.Builder()
                .setComponentID(rnd.nextLong())
                .setHeadline("Look and feel your best")
                .build();
        BodyComponent bodyComponent = new BodyComponent.Builder()
                .setComponentID(rnd.nextLong())
                .setBody("Get a free massage with purchase of any other service.")
                .build();
        AdvertiserComponent advertiserComponent = new AdvertiserComponent.Builder()
                .setComponentID(rnd.nextLong())
                .setAdvertiser("Hand and Stone Massage")
                .build();

        ArrayList<ActionComponent> actionComponents = new ArrayList<>(3);
        actionComponents.add(action1);
        actionComponents.add(action2);
        actionComponents.add(action3);
        SecondaryActionComponent secondaryComponent = new SecondaryActionComponent.Builder()
                .setTitle("Near Clark and Lake")
                .setSubtitle("200 W Lake St is in Chicago")
                .build()                ;
        LogoComponent logoComponent = new LogoComponent.Builder()
                .setComponentID(rnd.nextLong())
                .setLogo(BitmapFactory.decodeResource(getResources(), R.drawable.test_logo))
                .build();
        HeroComponent heroComponent = new HeroComponent.Builder()
                .setComponentID(rnd.nextLong())
                .setKind(HeroKind.Image)
                .setImage(BitmapFactory.decodeResource(getResources(), R.drawable.test_hero))
                .build();
        final Ad ad = adBuilder.setAdID(rnd.nextLong())
                .setRequestID(rnd.nextLong())
                .setVersionID(rnd.nextLong())
                .setHeadline(headlineComponent)
                .setBody(bodyComponent)
                .setAdvertiser(advertiserComponent)
                .setLogo(logoComponent)
                .setSecondaryAction(secondaryComponent)
                .setHero(heroComponent)
                .setActions(actionComponents)
                .setView(viewComponent)
                .build();
        AdRenderer adRenderer = new AdRenderer(this);
        final View view = adRenderer.render(null, viewBinder, ad);
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
                    Log.v(TAG, "Ip Address: " + addr.toString());
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

        adsController = new AdsController(this, UUID.fromString("c546ebee-6f2a-4f48-947b-e580c45e4f79"));
        final View view2 = adsController.renderAd(null, viewBinder, ad, true);
        mainLayout.addView(view2);
        final View view3 = adsController.renderAd(null, viewBinder, ad, false);
        mainLayout.addView(view3);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view4 = inflater.inflate(R.layout.adlayout, null);
        adsController.renderAdInto(view4, viewBinder, ad, true);
        mainLayout.addView(view4);
        View view5 = inflater.inflate(R.layout.adlayout, null);
        adsController.renderAdInto(view5, viewBinder, ad, false);
        mainLayout.addView(view5);

    }
}
