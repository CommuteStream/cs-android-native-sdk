package com.commutestream.nativeads.reporting;

import android.location.Location;
import android.os.Build;

import com.commutestream.nativeads.Ad;
import com.commutestream.nativeads.AdsController;
import com.commutestream.nativeads.CSNLog;
import com.commutestream.nativeads.components.Component;
import com.commutestream.nativeads.protobuf.Csnmessages;
import com.google.protobuf.ByteString;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

public class ReportEngine {

    private ByteString adUnit;
    private Csnmessages.DeviceID deviceID;
    private boolean limitTracking;
    private HashSet<ByteString> ipAddresses;
    private HashMap<AdReportKey, AdReportBuilder> adReportBuilders = new HashMap<>();
    private ArrayList<Csnmessages.DeviceLocation> locations = new ArrayList<>();

    public ReportEngine(UUID adUnit, UUID aaid, boolean limitTracking, Collection<InetAddress> ipAddresses) {
        this.adUnit = ByteString.copyFrom(EncodingUtils.encodeUUID(adUnit));
        setDeviceInfo(aaid, limitTracking);
        setIpAddresses(ipAddresses);
    }

    public void setDeviceInfo(UUID aaid, boolean limitTracking) {
        this.deviceID = Csnmessages.DeviceID.newBuilder()
                .setDeviceIdType(Csnmessages.DeviceID.Type.AAID)
                .setDeviceId(ByteString.copyFrom(EncodingUtils.encodeUUID(aaid)))
                .setLimitTracking(limitTracking)
                .build();
        this.limitTracking = limitTracking;
    }

    public void setIpAddresses(Collection<InetAddress> ipAddresses) {
        this.ipAddresses = new HashSet<>();
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

    public void addLocation(Location location) {

        locations.add(EncodingUtils.encodeDeviceLocation(location));
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
                .setSdkVersion(AdsController.SDK_VERSION)
                .addAllDeviceLocations(locations)
                .addAllIpAddresses(ipAddresses);
        for(Map.Entry<AdReportKey, AdReportBuilder> adEntry : adReportBuilders.entrySet()) {
            builder.addAdReports(adEntry.getValue().build());
        }
        locations.clear();
        adReportBuilders.clear();
        return builder.build();
    }
}
