package com.commutestream.nativeads;

public class TransitStop {
    private String agencyID;
    private String routeID;
    private String stopID;

    public TransitStop(String agencyID, String routeID, String stopID) {
        this.agencyID = agencyID;
        this.routeID = routeID;
        this.stopID = stopID;
    }

    public String getAgencyID() {
        return agencyID;
    }

    public String getRouteID() {
        return routeID;
    }

    public String getStopID() {
        return stopID;
    }
}
