package com.commutestream.nativeads;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.commutestream.nativeads.components.BodyComponent;
import com.commutestream.nativeads.components.Component;
import com.commutestream.nativeads.components.HeadlineComponent;
import com.commutestream.nativeads.components.LogoComponent;
import com.commutestream.nativeads.components.SecondaryActionComponent;
import com.commutestream.nativeads.components.ViewComponent;

/**
 * Render an Ad to a layout
 */
public class AdRenderer {
    private Context context;
    public AdRenderer(Context context) {
        this.context = context;
    }

    public View render(ViewGroup viewGroup, ViewBinder binder, Ad ad) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(binder.getLayout(), viewGroup);
        renderInto(view, binder, ad);
        return view;
    }

    public void renderInto(View view, ViewBinder binder, Ad ad) {
        renderLogo(view, ad, binder);
        renderHeadline(view, ad, binder);
        renderBody(view, ad, binder);
    }

    protected void renderLogo(View view, Ad ad, ViewBinder binder) {
        LogoComponent logo = ad.getLogo();
        ImageView logoView = view.findViewById(binder.getLogo());
        CSNLog.v("Logo View: " + logoView);
        if(logoView != null && logo != null) {
            Bitmap logoBitmap = logo.getLogo();
            if(logoBitmap != null) {
                logoView.setImageBitmap(logoBitmap);
            }
        }
    }

    protected void renderHeadline(View view, Ad ad, ViewBinder binder) {
        HeadlineComponent headline = ad.getHeadline();
        TextView headlineView = view.findViewById(binder.getHeadline());
        if(headlineView != null && headline != null) {
            headlineView.setText(headline.getHeadline());
        }
    }

    protected void renderBody(View view, Ad ad, ViewBinder binder) {
        BodyComponent body = ad.getBody();
        TextView bodyView = view.findViewById(binder.getBody());
        if(bodyView != null && body != null) {
            bodyView.setText(body.getBody());
        }
    }
}
