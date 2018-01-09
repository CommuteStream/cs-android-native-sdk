package com.commutestream.nativeads;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

import com.commutestream.nativeads.components.Component;
import com.commutestream.nativeads.reporting.ReportEngine;

public class ViewMonitor {
    final private View view;
    final private Ad ad;
    final private Component component;
    private boolean visible = false;
    private Rect globalVisibleRect = new Rect();
    private Rect viewVisibleRect = new Rect();
    private Point viewOffsetPoint = new Point();

    public ViewMonitor(View view, Ad ad, Component component) {
        this.view = view;
        this.ad = ad;
        this.component = component;
    }

    public void reportVisibility(ReportEngine engine) {
        view.getRootView().getGlobalVisibleRect(globalVisibleRect);
        view.getGlobalVisibleRect(viewVisibleRect, viewOffsetPoint);

        visible = view.isShown() && Rect.intersects(globalVisibleRect, viewVisibleRect);
        double screenVisible = 0;
        double viewVisible = 0;
        if(visible) {
            Rect intersected = new Rect(globalVisibleRect);
            intersected.intersect(viewVisibleRect);
            int windowArea = globalVisibleRect.width()*globalVisibleRect.height();
            int intersectArea = intersected.width()*intersected.height();
            int viewArea = view.getWidth()*view.getHeight();
            screenVisible = (double)intersectArea / (double) windowArea;
            viewVisible = (double)intersectArea /  (double) viewArea;
        }
        engine.addVisibility(ad, component, viewVisible, screenVisible);
    }
}
