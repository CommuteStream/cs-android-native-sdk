package com.commutestream.nativeads.components;

import com.commutestream.nativeads.protobuf.Csnmessages;

public class LocationComponent implements Component {
    protected long componentID;
    protected double latitude;
    protected double longitude;
    protected String name;
    protected String address;

    public LocationComponent(Csnmessages.LocationComponent msg) {
        componentID = msg.getComponentId();
        latitude = msg.getLocation().getLat();
        longitude = msg.getLocation().getLon();
        name = msg.getName();
        address = msg.getAddress();
    }

    protected LocationComponent() {
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

    public static class Builder extends LocationComponent {
        public Builder setComponentID(long componentID) {
            this.componentID = componentID;
            return this;
        }

        public Builder setLatitude(double latitutde) {
            this.latitude = latitutde;
            return this;
        }

        public Builder setLongitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public LocationComponent build() {
            return this;
        }
    }
}
