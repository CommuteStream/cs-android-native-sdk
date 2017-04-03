package com.commutestream.nativetestapp;

import com.commutestream.nativeads.AdsController;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.commutestream.nativetestapp.R;

public class MainActivity extends AppCompatActivity {

    private AdsController mAdsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
