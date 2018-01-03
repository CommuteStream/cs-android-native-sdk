package com.commutestream.nativeads;

import android.content.Context;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.commutestream.nativeads.protobuf.Csnmessages;
import com.google.protobuf.ByteString;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AdsController {

    public interface AdResponseHandler {
        void onAds(List<Ad> ads);
    }

    private Context context;
    private Client client;

    public AdsController(Context context, Client client) {
        this.context = context;
        this.client = client;
    }

    public AdsController(Context context) {
        this.context = context;
        client = new HttpClient();
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
    }
}
