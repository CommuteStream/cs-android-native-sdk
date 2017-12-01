package com.commutestream.nativeads;

import android.support.annotation.NonNull;

public class TransitRoute {
    private String agencyID;
    private String routeID;

    public TransitRoute(@NonNull String agencyID, @NonNull String routeID) {
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
