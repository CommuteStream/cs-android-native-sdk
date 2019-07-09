package com.commutestream.nativeads;

import androidx.annotation.NonNull;

public class TransitStop {
    private String agencyID;
    private String routeID;
    private String stopID;

    public TransitStop(@NonNull String agencyID, @NonNull  String routeID, @NonNull String stopID) {
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
