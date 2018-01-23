package com.commutestream.nativetestapp;

import com.commutestream.nativeads.Ad;
import com.commutestream.nativeads.AdRenderer;
import com.commutestream.nativeads.AdRequest;
import com.commutestream.nativeads.AdsController;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

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
import java.security.Security;
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

    private Ad generateAd(Random rnd, boolean htmlHero, boolean interactiveHero, boolean fancyHero) {
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
                .setHeadline("Look and feel your best " + rnd.nextLong())
                .build();
        BodyComponent bodyComponent = new BodyComponent.Builder()
                .setComponentID(rnd.nextLong())
                .setBody("Get a free massage with purchase of any other service " + rnd.nextLong())
                .build();
        AdvertiserComponent advertiserComponent = new AdvertiserComponent.Builder()
                .setComponentID(rnd.nextLong())
                .setAdvertiser("Hand and Stone Massage " + rnd.nextLong())
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
        HeroComponent.Builder heroComponentBuilder = new HeroComponent.Builder()
                .setComponentID(rnd.nextLong());
        if(!htmlHero) {
                heroComponentBuilder.setKind(HeroKind.Image)
                    .setImage(BitmapFactory.decodeResource(getResources(), R.drawable.test_hero));
        } else {
            String html = "<!DOCTYPE html><html style=\"padding:0px; margin: 0px;\"><body style=\"padding:0px; margin: 0px;\"><a border=\"0\" style=\"padding:0px; margin: 0px;\" href=\"https://commutestream.com\"><img border=\"0\" style=\"padding:0px; margin: 0px;\" src=\"https://ncusar.org/modelarableague/wordpress/wp-content/uploads/2013/04/busacc-1200x627.jpg\"></a></body></html>";
            if(fancyHero) {
                html = "<!DOCTYPE html><html><body style=\"margin:0px\"><iframe width=\"1200\" height=\"627\" src=\"https://www.youtube.com/embed/CLsMnItzhC0?rel=0&amp;start=1&amp;autoplay=1;loop=1\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
            }
            heroComponentBuilder.setKind(HeroKind.HTML)
                    .setHtml(html)
                    .setInteractive(interactiveHero);
        }
        HeroComponent heroComponent = heroComponentBuilder.build();
        return adBuilder.setAdID(rnd.nextLong())
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
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // render fake
        Random rnd = new Random();
        final Ad ad = generateAd(rnd, false, false, false);
        final ViewBinder viewBinder = new ViewBinder(R.layout.adlayout)
                .setHeadline(R.id.headlineView)
                .setLogo(R.id.logoView)
                .setBody(R.id.bodyView);

        AdRenderer adRenderer = new AdRenderer(this);
        final View view = adRenderer.render(null, viewBinder, ad);
        final LinearLayout mainLayout = findViewById(R.id.main_layout);
        Button showPopup = findViewById(R.id.show_popup);
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
        final SecondaryPopUp popup = new SecondaryPopUp(this, reportEngine);
        showPopup.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup.displayAd(ad);
                    }
                }
        );
        mainLayout.addView(view);

        VisibilityMonitor visMonitor = new VisibilityMonitor(reportEngine);
        visMonitor.addView(view, ad, ad.getLogo());
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
        Ad ad2 = generateAd(rnd, true, false, false);
        View view6 = adsController.renderAd(null, viewBinder, ad2, true);
        mainLayout.addView(view6);
        Ad ad3 = generateAd(rnd, true, true, false);
        View view7 = adsController.renderAd(null, viewBinder, ad3, true);
        mainLayout.addView(view7);
        Ad ad4 = generateAd(rnd, true, true, true);
        View view8 = adsController.renderAd(null, viewBinder, ad4, true);
        mainLayout.addView(view8);
        locationListen();

        ArrayList<AdRequest> adRequests = new ArrayList<>(1);
        AdRequest handAndStone = new AdRequest();
        handAndStone.addStop("cta", "", "1580");
        adRequests.add(handAndStone);
        adsController.fetchAds(adRequests, new AdsController.AdResponseHandler() {
            @Override
            public void onAds(List<Ad> ads) {
                Ad ad = ads.get(0);
                if(ad != null) {
                    mainLayout.addView(adsController.renderAd(null, viewBinder, ad, true));
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        locationListen();
    }

    private void locationListen() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
            return;
        }
        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000L, 5.0f, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.v(TAG, "GPS Location changed");
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000L, 5.0f, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.v(TAG, "Network Location changed");
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        } catch (SecurityException e) {
        } catch (Exception e) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.adsController.resume();

    }

    @Override
    public void onPause() {
        super.onPause();
        this.adsController.pause();
    }
}
