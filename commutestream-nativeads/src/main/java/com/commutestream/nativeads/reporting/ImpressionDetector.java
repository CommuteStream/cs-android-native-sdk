package com.commutestream.nativeads.reporting;

import com.commutestream.nativeads.Ad;
import com.commutestream.nativeads.protobuf.Csnmessages;

public class ImpressionDetector {
    private long totalTimeVisible = 0;
    private long lastVisibleTime = 0;
    private long lastImpressionTime = 0;
    private long maxSampleSkew = MAX_SAMPLE_SKEW;
    private double impressionVisibility = IMPRESSION_VISIBILITY;
    private long impressionTime = IMPRESSION_TIME;
    private long impressionDuration = IMPRESSION_DURATION;
    private static long MAX_SAMPLE_SKEW = 1000;
    private static double IMPRESSION_VISIBILITY = 0.5;
    private static long IMPRESSION_TIME = 1000;
    private static long IMPRESSION_DURATION = 30000;

    public ImpressionDetector() {
    }

    public ImpressionDetector(long maxSampleSkew, double impressionVisibility, long impressionTime, long impressionDuration) {
        this.maxSampleSkew = maxSampleSkew;
        this.impressionVisibility = impressionVisibility;
        this.impressionTime = impressionTime;
        this.impressionDuration = impressionDuration;
    }

    public boolean addVisibility(double viewVisible, double screenVisible) {
        long timestamp = System.currentTimeMillis();
        if(viewVisible > impressionVisibility) {
            long timeDiff = timestamp - lastVisibleTime;
            if(timeDiff < maxSampleSkew) {
                totalTimeVisible += timeDiff;
            } else {
                totalTimeVisible = 0;
            }
        } else {
            totalTimeVisible = 0;
        }
        lastVisibleTime = timestamp;
        if(totalTimeVisible > impressionTime && timestamp - lastImpressionTime > impressionDuration) {
            lastImpressionTime = timestamp;
            return true;
        } else {
            return false;
        }
    }

    public boolean addInteraction(Csnmessages.ComponentInteractionKind kind) {
        long timestamp = System.currentTimeMillis();
        if(timestamp - lastImpressionTime > impressionDuration) {
            lastImpressionTime = timestamp;
            return true;
        } else {
            return false;
        }
    }
}
