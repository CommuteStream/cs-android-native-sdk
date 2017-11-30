package com.commutestream.nativeads;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.commutestream.nativeads.components.Component;

/**
 * Render an Ad to a layout
 */
public class AdRenderer {
    private Context mContext;
    public AdRenderer(Context context) {
        mContext = context;
    }

    public View render(ViewGroup viewGroup, Ad ad, ViewBinder binder) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(binder.getLayout(), viewGroup);
        renderInto(view, ad, binder);
        return view;
    }

    public void renderInto(View view, Ad ad, ViewBinder binder) {
        ImageView logoView = view.findViewById(binder.getLogo());
        if(logoView != null) {
            logoView.setImageBitmap(ad.getLogo().getLogo());
            monitorVisibility(ad, ad.getLogo(), logoView);
        }
    }

    private void monitorVisibility(Ad ad, Component component, View view) {
        //TODO attach to VisibilityMonitor?
        // visMon.addComponentView(ad, component, view);
        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                // begin montitoring
                // visMon.addComponentView(ad, ad.getLogo(), logoView);
            }

            @Override
            public void onViewDetachedFromWindow(View view) {
                // stop monitoring
                // visMon.removeComponentView(ad, ad.getLogo(), logoView);
            }
        });
    }
}
