package com.commutestream.nativeads;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SecondaryPopUp {
    private Context context;
    private Dialog dialog;
    private ImageView logoView;
    private TextView titleView;
    private TextView subtitleView;
    private TextView headlineView;
    private TextView bodyView;
    private FrameLayout heroFrame;
    private ImageView heroImageView;
    private WebView heroWebView;
    private LinearLayout actionsLayout;
    private Button action1Button;
    private ImageButton closeButton;


    public SecondaryPopUp(Activity activity) {
        this.context = activity;
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.secondary_view);
        titleView = dialog.findViewById(R.id.secondary_ad_title);
        subtitleView = dialog.findViewById(R.id.secondary_ad_subtitle);
        logoView = dialog.findViewById(R.id.secondary_ad_logo);
        headlineView = dialog.findViewById(R.id.secondary_ad_headline);
        bodyView = dialog.findViewById(R.id.secondary_ad_body);
        heroFrame = dialog.findViewById(R.id.secondary_ad_hero);
        actionsLayout = dialog.findViewById(R.id.secondary_ad_actions);
        closeButton = dialog.findViewById(R.id.secondary_ad_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void displayAd(Ad ad) {
        titleView.setText(ad.getSecondaryAction().getTitle());
        subtitleView.setText(ad.getSecondaryAction().getSubtitle());
        logoView.setImageBitmap(ad.getLogo().getLogo());
        headlineView.setText(ad.getHeadline().getHeadline());
        bodyView.setText(ad.getBody().getBody());

        //TODO add image or web view depending on hero kind
        heroImageView = new ImageView(context);
        heroImageView.setImageBitmap(ad.getHero().getImage());
        heroFrame.removeAllViews();
        heroFrame.addView(heroImageView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        //TODO add action buttons
        dialog.show();
    }
}
