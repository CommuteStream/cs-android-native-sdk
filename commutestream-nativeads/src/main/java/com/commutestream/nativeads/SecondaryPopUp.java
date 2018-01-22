package com.commutestream.nativeads;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.commutestream.nativeads.components.ActionComponent;
import com.commutestream.nativeads.protobuf.Csnmessages;
import com.commutestream.nativeads.reporting.ReportEngine;

import android.os.Handler;
import java.util.logging.LogRecord;

public class SecondaryPopUp {
    private Activity activity;
    private ReportEngine reportEngine;
    private PopupWindow popup;
    private ImageView logoView;
    private TextView titleView;
    private TextView subtitleView;
    private TextView headlineView;
    private TextView bodyView;
    private TextView advertiserView;
    private FrameLayout heroFrame;
    private ImageView heroImageView;
    private WebView heroWebView;
    private WebChromeClient heroWebClient;
    private LinearLayout actionsLayout;
    private Button action1Button;
    private ImageButton closeButton;
    private ViewTreeObserver heroFrameTreeObserver;
    private Boolean heroHeightAdjusted;
    private ImageButton poweredByCSButton;



    public SecondaryPopUp(final Activity activity, ReportEngine reportEngine) {
        this.reportEngine = reportEngine;
        this.activity = activity;
        View view = activity.getLayoutInflater().inflate(R.layout.secondary_view, null);
        popup = new PopupWindow(view, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, true);
        popup.setContentView(view);
        titleView = view.findViewById(R.id.secondary_ad_title);
        subtitleView = view.findViewById(R.id.secondary_ad_subtitle);
        logoView = view.findViewById(R.id.secondary_ad_logo);
        headlineView = view.findViewById(R.id.secondary_ad_headline);
        bodyView = view.findViewById(R.id.secondary_ad_body);
        advertiserView = view.findViewById(R.id.secondary_ad_advertiser);
        heroFrame = view.findViewById(R.id.secondary_ad_hero);
        actionsLayout = view.findViewById(R.id.secondary_ad_actions);
        closeButton = view.findViewById(R.id.secondary_ad_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
        poweredByCSButton = view.findViewById(R.id.powered_by_cs_button);
        poweredByCSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                String url = "https://commutestream.com";
                i.setData(Uri.parse(url));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(i);
            }
        });


        heroHeightAdjusted = false;
        heroFrameTreeObserver = heroFrame.getViewTreeObserver();
        heroFrameTreeObserver.addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
//                    this.heroFrameTreeObserver.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                } else {
//                    this.heroFrameTreeObserver.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                }
                int width  = heroFrame.getMeasuredWidth();
                int height = heroFrame.getMeasuredHeight();

                int newHeight = (int)Math.floor(width * 0.5225);
                if(!heroHeightAdjusted){
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,newHeight);
                    heroFrame.setLayoutParams(params);
                    heroHeightAdjusted = true;
                }


            }
        });
    }



    public void displayAd(final Ad ad) {
        titleView.setText(ad.getSecondaryAction().getTitle());
        subtitleView.setText(ad.getSecondaryAction().getSubtitle());
        logoView.setImageBitmap(ad.getLogo().getLogo());
        headlineView.setText(ad.getHeadline().getHeadline());
        bodyView.setText(ad.getBody().getBody());
        advertiserView.setText(ad.getAdvertiser().getAdvertiser());

        switch(ad.getHero().getKind()) {
            case HTML:
                heroWebView = new WebView(activity);
                heroWebView.getSettings().setJavaScriptEnabled(true);
                heroWebView.getSettings().setDomStorageEnabled(true);
                heroWebView.getSettings().setUseWideViewPort(true);
                heroWebView.getSettings().setLoadWithOverviewMode(true);
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                    heroWebView.setWebContentsDebuggingEnabled(true);
                }
                heroWebView.loadData(ad.getHero().getHtml(), "text/html", null);
                if(!ad.getHero().getInteractive()) {
                    heroWebView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return true;
                        }
                    });
                }
                heroFrame.removeAllViews();
                heroFrame.addView(heroWebView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                break;
            case Image:
                heroImageView = new ImageView(activity);
                heroImageView.setImageBitmap(ad.getHero().getImage());
                heroFrame.removeAllViews();
                heroFrame.addView(heroImageView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                break;
        }


        Float buttonFontSize = (float)14;

        switch (ad.actions.size()){
            case 1:
                buttonFontSize = (float)22;
                break;

            case 2:
                buttonFontSize = (float)19;
                break;

            case 3:
                buttonFontSize = (float)14;
                break;

            default:
                break;
        }

        actionsLayout.removeAllViews();
        for (final ActionComponent action : ad.actions) {
            Button actionButton = new Button(activity);
            actionButton.setText(action.getTitle());
            actionButton.setBackgroundColor(action.getColors().getBackground());
            actionButton.setTextColor(action.getColors().getForeground());
            actionButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, buttonFontSize);

            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    reportEngine.addInteraction(ad, action, Csnmessages.ComponentInteractionKind.Tap);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(action.getUrl()));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getBackground().setAlpha(120);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            v.getBackground().setAlpha(255);
                        }
                    }, 300);


                    activity.startActivity(i);
                }
            });

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
            params.weight = 1.0f;
            params.setMargins(5,0,5,0);
            actionsLayout.addView(actionButton, params);
        }

        popup.showAtLocation(activity.findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
        popup.update();
    }
}
