package com.commutestream.nativeads.reporting;

import com.commutestream.nativeads.Ad;
import com.commutestream.nativeads.protobuf.Csnmessages;

public class ImpressionDetector {
    private long totalTimeVisible = 0;
    private long lastVisibleTime = 0;
    private long lastImpressionTime = 0;
    private static long MAX_SAMPLE_SKEW = 1000;
    private static double IMPRESSION_VISIBILITY = 0.5;
    private static long IMPRESSION_TIME = 1000;
    private static long IMPRESSION_DURATION = 30000;

    public ImpressionDetector() {
    }

    public boolean addVisibility(double viewVisible, double screenVisible) {
        long timestamp = System.currentTimeMillis();
        if(viewVisible > IMPRESSION_VISIBILITY) {
            long timeDiff = timestamp - lastVisibleTime;
            if(timeDiff < MAX_SAMPLE_SKEW) {
                totalTimeVisible += timeDiff;
            } else {
                totalTimeVisible = 0;
            }
        } else {
            totalTimeVisible = 0;
        }
        lastVisibleTime = timestamp;
        if(totalTimeVisible > IMPRESSION_TIME && timestamp - lastImpressionTime > IMPRESSION_DURATION) {
            lastImpressionTime = timestamp;
            return true;
        } else {
            return false;
        }
    }

    public boolean addInteraction(Csnmessages.ComponentInteractionKind kind) {
        long timestamp = System.currentTimeMillis();
        if(timestamp - lastImpressionTime > IMPRESSION_DURATION) {
            lastImpressionTime = timestamp;
            return true;
        } else {
            return false;
        }
    }
}
