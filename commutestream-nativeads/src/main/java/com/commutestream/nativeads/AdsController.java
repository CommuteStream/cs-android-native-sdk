package com.commutestream.nativeads;

import android.content.Context;

import com.commutestream.nativeads.reporting.ReportEngine;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.commutestream.nativeads.protobuf.Csnmessages;
import com.google.protobuf.ByteString;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class AdsController {

    public interface AdResponseHandler {
        void onAds(List<Ad> ads);
    }

    private UUID adUnit;
    private UUID aaid = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private boolean limitTracking = true;
    private Collection<InetAddress> ipAddresses = new ArrayList<>();
    private ReportEngine reportEngine;
    private Context context;
    private Client client;

    public AdsController(Context context, Client client, UUID adUnit) {
        init(context, client, adUnit);
    }

    public AdsController(Context context, UUID adUnit) {
        client = new HttpClient();
        init(context, client, adUnit);
    }

    private void init(Context context, Client client, UUID adUnit) {
        this.context = context;
        this.client = client;
        this.adUnit = adUnit;
        reportEngine = new ReportEngine(adUnit, aaid, limitTracking, ipAddresses);
        loadAaid();
        loadIpAddresses();
    }

    public void fetchAds(List<AdRequest> requests, final AdResponseHandler responseHandler) {
        //TODO convert requests to AdRequests message
        Csnmessages.AdRequests msg = Csnmessages.AdRequests.newBuilder()
                .build();


        client.getAds(msg, new Client.AdResponseHandler() {
            @Override
            public void onResponse(Csnmessages.AdResponses responses) {
                responseHandler.onAds(null);
            }

            @Override
            public void onFailure() {
                responseHandler.onAds(null);
            }
        });

    }

    private Csnmessages.AdRequests buildRequestsMessage(List<AdRequest> requests) throws NoSuchAlgorithmException {
        HashMap<byte[], Csnmessages.AdRequest.Builder> requestBuilders = new HashMap();
        for(AdRequest request: requests) {
            byte[] sha256 = request.sha256();
            Csnmessages.AdRequest.Builder builder = requestBuilders.get(sha256);
            if(builder == null) {
                builder = requestBuilder(request);
                requestBuilders.put(sha256, builder);
            } else {
                builder.setNumOfAds(builder.getNumOfAds() + 1);
            }
        }
        return Csnmessages.AdRequests.newBuilder().build();
    }

    private Csnmessages.AdRequest.Builder requestBuilder(AdRequest request) throws NoSuchAlgorithmException {
        Csnmessages.AdRequest.Builder builder = Csnmessages.AdRequest.newBuilder()
                .setHashId(ByteString.copyFrom(request.sha256()))
                .setNumOfAds(1);
        for(TransitAgency agency : request.getAgencies()) {
            builder.addAgencies(Csnmessages.TransitAgency.newBuilder()
            .setAgencyId(agency.getAgencyID()).build());
        }
        for(TransitRoute route : request.getRoutes()) {
            builder.addRoutes(Csnmessages.TransitRoute.newBuilder()
            .setAgencyId(route.getAgencyID())
            .setRouteId(route.getRouteID()).build());
        }
        for(TransitStop stop : request.getStops()) {
            builder.addStops(Csnmessages.TransitStop.newBuilder()
                    .setAgencyId(stop.getAgencyID())
                    .setRouteId(stop.getRouteID())
                    .setStopId(stop.getStopID()).build());
        }
        return builder;
    }

    private void loadIpAddresses() {
        HashSet<InetAddress> ipAddresses = new HashSet();
        List<NetworkInterface> interfaces = null;
        try {
            interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
        } catch (Exception ex) {
            interfaces = new ArrayList<>();
        }
        for (NetworkInterface intf : interfaces) {
            for(InetAddress addr : Collections.list(intf.getInetAddresses())) {
                if(!addr.isLoopbackAddress()) {
                    ipAddresses.add(addr);
                }
            }
        }
        setIpAddresses(ipAddresses);
    }

    private synchronized void setIpAddresses(Collection<InetAddress> ipAddresses) {
        this.ipAddresses = ipAddresses;
        reportEngine.setIpAddresses(ipAddresses);
    }

    private void loadAaid() {
        final AdsController controller = this;
        final Context context = this.context;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Info adInfo = null;
                try {
                    adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);

                } catch(Exception ex) {
                    CSNLog.e("Error getting Advertising ID: " + ex.getMessage());
                }

                final String aaidStr = adInfo.getId();
                final UUID aaid = UUID.fromString(aaidStr);
                final boolean limitTracking = adInfo.isLimitAdTrackingEnabled();
                controller.setDeviceInfo(aaid,limitTracking);
            }
        }).run();
    }


    private synchronized void setDeviceInfo(UUID aaid, boolean limitTracking) {
        this.aaid = aaid;
        this.limitTracking = limitTracking;
        this.reportEngine.setDeviceInfo(aaid, limitTracking);
    }
}
