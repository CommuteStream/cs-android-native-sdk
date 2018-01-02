package com.commutestream.nativeads.reporting;

import com.commutestream.nativeads.Ad;
import com.commutestream.nativeads.components.Component;
import com.commutestream.nativeads.protobuf.Csnmessages;
import com.google.protobuf.ByteString;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

public class ReportEngine {

    private HashMap<AdReportKey, AdReportBuilder> adReportBuilders;
    private ByteString adUnit;
    private Csnmessages.DeviceID deviceID;
    private boolean limitTracking = false;
    private HashSet<ByteString> ipAddresses;

    public ReportEngine(UUID adUnit, UUID aaid, boolean limitTracking, Collection<InetAddress> ipAddresses) {
        this.adUnit = ByteString.copyFrom(ByteBuffer.allocate(16).putLong(adUnit.getMostSignificantBits()).putLong(adUnit.getLeastSignificantBits()).array());
        this.deviceID = Csnmessages.DeviceID.newBuilder()
                .setDeviceIdType(Csnmessages.DeviceID.Type.AAID)
                .setDeviceId(ByteString.copyFrom(ByteBuffer.allocate(16).putLong(aaid.getMostSignificantBits()).putLong(aaid.getLeastSignificantBits()).array()))
                .build();
        this.limitTracking = limitTracking;
        this.ipAddresses = new HashSet();
        this.setIpAddresses(ipAddresses);
        adReportBuilders = new HashMap();
    }

    public void setIpAddresses(Collection<InetAddress> ipAddresses) {
        for(InetAddress addr : ipAddresses) {
            this.ipAddresses.add(ByteString.copyFrom(addr.getAddress()));
        }
    }

    public void addVisibility(Ad ad, Component component, double viewVisible, double screenVisible) {
        AdReportBuilder adReportBuilder = getAdReportBuilder(ad);
        adReportBuilder.addComponentVisibility(component.getComponentID(), viewVisible, screenVisible);
    }

    public void addInteraction(Ad ad, Component component, Csnmessages.ComponentInteractionKind kind) {
        AdReportBuilder adReportBuilder = getAdReportBuilder(ad);
        adReportBuilder.addComponentInteraction(component.getComponentID(), kind);
    }

    private AdReportBuilder getAdReportBuilder(Ad ad) {
        AdReportKey key = new AdReportKey(ad);
        AdReportBuilder builder = adReportBuilders.get(key);
        if(builder == null) {
            builder = new AdReportBuilder(ad);
            adReportBuilders.put(key, builder);
        }
        return builder;
    }

    public Csnmessages.AdReports build() {
        String timezone = TimeZone.getDefault().getID();
        long time = System.currentTimeMillis();
        Csnmessages.AdReports.Builder builder = Csnmessages.AdReports.newBuilder()
                .setAdUnit(adUnit)
                .setDeviceId(deviceID)
                .setTimezone(timezone)
                .setDeviceTime(time)
                .addAllIpAddresses(ipAddresses);
        for(Map.Entry<AdReportKey, AdReportBuilder> adEntry : adReportBuilders.entrySet()) {
            builder.addAdReports(adEntry.getValue().build());
        }
        return builder.build();
    }


}
