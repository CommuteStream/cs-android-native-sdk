package com.commutestream.nativeads;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    public void addAgency(TransitAgency ta) {
        agencies.add(ta);
    }

    public void addRoute(TransitRoute tr) {
        routes.add(tr);
    }

    public void addStop(TransitStop ts) {
        stops.add(ts);
    }

    public String sha256() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        for(TransitAgency agency : agencies) {
            md.update(agency.getAgencyID().getBytes());
        }
        for(TransitRoute route : routes) {
            md.update(route.getAgencyID().getBytes());
            md.update(route.getRouteID().getBytes());
        }
        for(TransitStop stop : stops) {
            md.update(stop.getAgencyID().getBytes());
            md.update(stop.getRouteID().getBytes());
            md.update(stop.getStopID().getBytes());
        }
        return bin2hex(md.digest());
    }

    static String bin2hex(byte[] data) {
        return String.format("%0" + (data.length*2) + "X", new BigInteger(1, data));
    }
}
