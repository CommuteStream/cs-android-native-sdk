package com.commutestream.nativeads;

import java.util.HashSet;

public class AdRequest {

    private HashSet<TransitAgency> agencies;
    private HashSet<TransitRoute> routes;
    private HashSet<TransitStop> stops;

    public AdRequest() {
        agencies = new HashSet<>();
        routes = new HashSet<>();
        stops = new HashSet<>();
    }

    public String sha256() {
        return "";
    }
}
