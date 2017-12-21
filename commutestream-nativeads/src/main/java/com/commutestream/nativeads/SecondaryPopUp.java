package com.commutestream.nativeads;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.commutestream.nativeads.components.ActionComponent;

public class SecondaryPopUp {
    private Activity activity;
    private PopupWindow popup;
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
        this.activity = activity;
        View view = activity.getLayoutInflater().inflate(R.layout.secondary_view, null);
        popup = new PopupWindow(view, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, true);
        popup.setContentView(view);
        titleView = view.findViewById(R.id.secondary_ad_title);
        subtitleView = view.findViewById(R.id.secondary_ad_subtitle);
        logoView = view.findViewById(R.id.secondary_ad_logo);
        headlineView = view.findViewById(R.id.secondary_ad_headline);
        bodyView = view.findViewById(R.id.secondary_ad_body);
        heroFrame = view.findViewById(R.id.secondary_ad_hero);
        actionsLayout = view.findViewById(R.id.secondary_ad_actions);
        closeButton = view.findViewById(R.id.secondary_ad_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
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
        heroImageView = new ImageView(activity);
        heroImageView.setImageBitmap(ad.getHero().getImage());
        heroFrame.removeAllViews();
        heroFrame.addView(heroImageView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        //TODO add action buttons
        actionsLayout.removeAllViews();
        for (final ActionComponent action : ad.actions) {
            Button actionButton = new Button(activity);
            actionButton.setText(action.getTitle());
            actionButton.setBackgroundColor(action.getColors().getBackground());
            actionButton.setTextColor(action.getColors().getForeground());
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(action.getUrl()));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(i);
                }
            });
            actionsLayout.addView(actionButton);
        }

        popup.showAtLocation(activity.findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
        popup.update();
    }
}
