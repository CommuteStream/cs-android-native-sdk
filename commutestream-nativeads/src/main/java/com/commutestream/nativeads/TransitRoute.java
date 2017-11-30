package com.commutestream.nativeads;

public class TransitRoute {
    private String agencyID;
    private String routeID;

    public TransitRoute(String agencyID, String routeID) {
        this.agencyID = agencyID;
        this.routeID = routeID;
    }

    public String getAgencyID() {
        return agencyID;
    }

    public String getRouteID() {
        return routeID;
    }
}
