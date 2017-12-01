package com.commutestream.nativeads;

import android.support.annotation.NonNull;

public class TransitAgency {
    private String agencyID;

    public TransitAgency(@NonNull String agencyID) {
        this.agencyID = agencyID;
    }

    public String getAgencyID() {
        return agencyID;
    }
}
