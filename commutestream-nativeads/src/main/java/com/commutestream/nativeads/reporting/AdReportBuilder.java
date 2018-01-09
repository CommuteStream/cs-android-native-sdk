package com.commutestream.nativeads.reporting;

import com.commutestream.nativeads.Ad;
import com.commutestream.nativeads.CSNLog;
import com.commutestream.nativeads.protobuf.Csnmessages;

import java.util.HashMap;

public class AdReportBuilder {
    Csnmessages.AdReport.Builder adReportBuilder;
    HashMap<Long, Csnmessages.ComponentReport.Builder> componentReportBuilders;
    ImpressionDetector impressionDetector;

    public AdReportBuilder(Ad ad) {
        adReportBuilder = Csnmessages.AdReport.newBuilder()
                .setAdId(ad.getAdID())
                .setRequestId(ad.getRequestID())
                .setVersionId(ad.getVersionID());
        componentReportBuilders = new HashMap();
        impressionDetector = new ImpressionDetector();
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
            //CSNLog.d("Adding viewSample: " + viewSample + " deviceSample " + deviceSample);
            builder.addViewVisibilitySamples(viewSample);
            builder.addDeviceVisibilitySamples(deviceSample);
        } else {
            int idx = builder.getViewVisibilitySamplesCount() - 1;
            long curViewSample = builder.getViewVisibilitySamples(idx);
            long curDeviceSample = builder.getDeviceVisibilitySamples(idx);
            long viewSample = EncodingUtils.setVisibilitySample(curViewSample, position, viewEncoded);
            long deviceSample = EncodingUtils.setVisibilitySample(curDeviceSample, position, screenEncoded);
            //CSNLog.d("Setting viewSample: " + viewSample + " deviceSample " + deviceSample);
            builder.setViewVisibilitySamples(idx, viewSample);
            builder.setDeviceVisibilitySamples(idx, deviceSample);
        }
        builder.setVisibilitySampleCount(builder.getVisibilitySampleCount() + 1);
        if (impressionDetector.addVisibility(viewVisible, screenVisible)) {
            addImpression();
        }
    }

    public void addComponentInteraction(long componentID, Csnmessages.ComponentInteractionKind kind) {
        Csnmessages.ComponentReport.Builder builder = getComponentBuilder(componentID);
        builder.addInteractions(Csnmessages.ComponentInteraction.newBuilder()
                .setKind(kind)
                .setDeviceTime(System.currentTimeMillis())
                .build()
        );
        if (impressionDetector.addInteraction(kind)) {
            CSNLog.d("Adding impression for ad " + adReportBuilder.getAdId());
            addImpression();
        }
    }

    private void addImpression() {
        this.adReportBuilder.addImpressions(Csnmessages.AdImpression.newBuilder()
                .setDeviceTime(System.currentTimeMillis())
                .build()
        );
    }

    public Csnmessages.AdReport build() {
        adReportBuilder.clearComponents();
        for (Csnmessages.ComponentReport.Builder componentBuilder : componentReportBuilders.values()) {
            adReportBuilder.addComponents(componentBuilder.build());
        }
        return adReportBuilder.build();
    }

    private Csnmessages.ComponentReport.Builder getComponentBuilder(long componentID) {
        Csnmessages.ComponentReport.Builder builder = componentReportBuilders.get(Long.valueOf(componentID));
        if (builder == null) {
            CSNLog.d("Builder for component " + componentID + " not found");
            builder = Csnmessages.ComponentReport.newBuilder()
                    .setComponentId(componentID);
            componentReportBuilders.put(Long.valueOf(componentID), builder);
        }
        return builder;
    }
}
