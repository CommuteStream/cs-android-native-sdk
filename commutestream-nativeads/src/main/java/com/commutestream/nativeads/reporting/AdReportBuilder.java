package com.commutestream.nativeads.reporting;

import android.util.Xml;

import com.commutestream.nativeads.Ad;
import com.commutestream.nativeads.protobuf.Csnmessages;

import java.util.HashMap;

public class AdReportBuilder {
    Csnmessages.AdReport.Builder adReportBuilder;
    HashMap<Long, Csnmessages.ComponentReport.Builder> componentReportBuilders;

    public AdReportBuilder(Ad ad) {
        adReportBuilder = Csnmessages.AdReport.newBuilder()
                .setAdId(ad.getAdID())
                .setRequestId(ad.getRequestID())
                .setVersionId(ad.getVersionID());
        componentReportBuilders = new HashMap();
    }

    public void addComponentVisibility(long componentID, double viewVisible, double screenVisible) {
        Csnmessages.ComponentReport.Builder builder = getComponentBuilder(componentID);
        if (builder.getVisibilitySampleCount() == 0) {
            builder.setVisibilityEpoch(System.currentTimeMillis());
        }
        int position = (int) (builder.getVisibilitySampleCount() % 16);
        byte viewEncoded = EncodingUtils.encodeVisibility(viewVisible);
        byte screenEncoded = EncodingUtils.encodeVisibility(screenVisible);

        if (position == 0) {
            long viewSample = EncodingUtils.setVisibilitySample(0, 0, viewEncoded);
            long deviceSample = EncodingUtils.setVisibilitySample(0, 0, screenEncoded);
            builder.addViewVisibilitySamples(viewSample);
            builder.addDeviceVisibilitySamples(deviceSample);
        } else {
            int idx = builder.getViewVisibilitySamplesCount() - 1;
            long curViewSample = builder.getViewVisibilitySamples(idx);
            long curDeviceSample = builder.getDeviceVisibilitySamples(idx);
            long viewSample = EncodingUtils.setVisibilitySample(curViewSample, position, viewEncoded);
            long deviceSample = EncodingUtils.setVisibilitySample(curDeviceSample, position, screenEncoded);
            builder.setViewVisibilitySamples(idx, viewSample);
            builder.setDeviceVisibilitySamples(idx, deviceSample);
        }
        builder.setVisibilitySampleCount(builder.getVisibilitySampleCount() + 1);

        //TODO check for impressions
    }

    public void addComponentInteraction(long componentID, Csnmessages.ComponentInteractionKind kind) {
        Csnmessages.ComponentReport.Builder builder = getComponentBuilder(componentID);
    }

    public Csnmessages.AdReport build() {
        adReportBuilder.clearComponents();
        for(Csnmessages.ComponentReport.Builder componentBuilder : componentReportBuilders.values()) {
            adReportBuilder.addComponents(componentBuilder.build());
        }
        return adReportBuilder.build();
    }

    private Csnmessages.ComponentReport.Builder getComponentBuilder(long componentID) {
        Csnmessages.ComponentReport.Builder builder = componentReportBuilders.get(Long.valueOf(componentID));
        if(builder == null) {
            builder = Csnmessages.ComponentReport.newBuilder()
                    .setComponentId(componentID);
            componentReportBuilders.put(Long.valueOf(componentID), builder);
        }
        return builder;
    }
}
