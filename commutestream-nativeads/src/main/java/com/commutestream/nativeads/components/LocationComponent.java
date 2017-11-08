package com.commutestream.nativeads.components;

import android.location.Location;

import com.commutestream.nativeads.protobuf.Csnmessages;

public class LocationComponent implements Component {
    private long componentID;
    private double latitude;
    private double longitude;
    private String name;
    private String address;

    public LocationComponent(Csnmessages.LocationComponent msg) {
        componentID = msg.getComponentId();
        latitude = msg.getLocation().getLat();
        longitude = msg.getLocation().getLon();
        name = msg.getName();
        address = msg.getAddress();
    }

    @Override
    public long getComponentID() {
        return componentID;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
