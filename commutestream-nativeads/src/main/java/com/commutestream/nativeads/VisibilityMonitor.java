package com.commutestream.nativeads;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.commutestream.nativeads.components.Component;
import com.commutestream.nativeads.reporting.ReportEngine;

import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

public class VisibilityMonitor {
    final private ReportEngine reportEngine;
    final private HashSet<ViewMonitor> componentViews;
    private Timer timer;
    private TimerTask timerTask;

    public VisibilityMonitor(final ReportEngine reportEngine) {
        this.reportEngine = reportEngine;
        this.timer = new Timer("CS Native SDK Ad Visibility Monitor", true);
        this.componentViews = new HashSet<>();
        startMonitoring();
    }

    public void addView(View view, final Ad ad, final Component component) {
        final VisibilityMonitor monitor = this;
        final ViewMonitor viewMonitor = new ViewMonitor(view, ad, component);
        this.componentViews.add(viewMonitor);
        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                monitor.removeViewMonitor(viewMonitor);
            }
        });
    }

    private void removeViewMonitor(ViewMonitor viewMonitor) {
        this.componentViews.remove(viewMonitor);
    }

    public void stopMonitoring() {
        timerTask.cancel();
        timer.purge();
    }

    public void startMonitoring() {
        if(timerTask != null) {
            stopMonitoring();
        }
        final VisibilityMonitor monitor = this;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        monitor.checkViews();
                    }
                });
            }
        };
        // schedule this task 1 second from now, every quarter of a second
        timer.scheduleAtFixedRate(timerTask, 250, 250);
        checkViews();
    }

    private void checkViews() {
        for (ViewMonitor monitor : componentViews) {
            monitor.reportVisibility(reportEngine);
        }
    }
}

