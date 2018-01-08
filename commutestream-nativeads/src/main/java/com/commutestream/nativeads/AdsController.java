package com.commutestream.nativeads;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.commutestream.nativeads.protobuf.Csnmessages;
import com.commutestream.nativeads.reporting.EncodingUtils;
import com.commutestream.nativeads.reporting.ReportEngine;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.protobuf.ByteString;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;
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
    private AdRenderer adRenderer;

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
        adRenderer = new AdRenderer(context);
        loadIpAddresses();
        loadAaid();
        //TODO periodically update ip addresses and device id info, send reports
    }

    public void fetchAds(final List<AdRequest> requests, final AdResponseHandler responseHandler) {
        final ArrayList<Ad> nullAds = new ArrayList<>(requests.size());
        for(AdRequest request : requests) {
            nullAds.add(null);
        }
        try {
            Csnmessages.AdRequests msg = buildRequestsMessage(requests);
            client.getAds(msg, new Client.AdResponseHandler() {
                @Override
                public void onResponse(Csnmessages.AdResponses responses) {
                    try {
                        List<Ad> ads = decodeResponses(requests, responses);
                        responseHandler.onAds(ads);
                    } catch (Exception ex) {
                        CSNLog.e("Failed to decode ad responses: " + ex);
                        responseHandler.onAds(nullAds);
                    }
                }

                @Override
                public void onFailure() {
                    responseHandler.onAds(nullAds);
                }
            });
        } catch (Exception ex) {
            CSNLog.e("Failed to encode ad requests: " + ex);
            responseHandler.onAds(nullAds);
        }
    }


    /**
     * Render an Ad into a View using a ViewBinder. Returns null and logs the reason on failure.
     * @param viewGroup
     * @param viewBinder
     * @param ad
     * @return
     */
    public View renderAd(ViewGroup viewGroup, ViewBinder viewBinder, Ad ad) {
        //TODO setup view monitoring
        //TODO setup click handling and reporting for secondary action
        try {
            return adRenderer.render(viewGroup, viewBinder, ad);
        } catch (Exception e) {
            CSNLog.e("Failed to render ad: " + e);
            return null;
        }
    }

    private List<Ad> decodeResponses(List<AdRequest> requests, Csnmessages.AdResponses adResponses) throws NoSuchAlgorithmException {
        HashMap<ByteBuffer, ArrayDeque<Ad>> adQueues = new HashMap<>();
        for(Csnmessages.AdResponse adResponse : adResponses.getAdResponsesList()) {
            ArrayDeque<Ad> adQueue = new ArrayDeque<>();
            for(Csnmessages.NativeAd nativeAd : adResponse.getAdsList()) {
                Ad ad = new Ad(nativeAd);
                adQueue.add(ad);
            }
            adQueues.put(ByteBuffer.wrap(adResponse.getHashId().toByteArray()), adQueue);
        }
        ArrayList<Ad> ads = new ArrayList<>(requests.size());
        for(AdRequest request : requests) {
            ArrayDeque<Ad> adQueue = adQueues.get(ByteBuffer.wrap(request.sha256()));
            if (adQueue == null) {
                CSNLog.v("null queue");
                ads.add(null);
            } else if (adQueue.isEmpty()) {
                CSNLog.v("empty queue");
                ads.add(null);
            } else {
                Ad ad = adQueue.remove();
                CSNLog.v("found ad " + ad);
                ads.add(ad);
            }
        }
        return ads;
    }

    private Csnmessages.AdRequests buildRequestsMessage(List<AdRequest> requests) throws NoSuchAlgorithmException {
        HashMap<ByteBuffer, Csnmessages.AdRequest.Builder> requestBuilders = new HashMap();
        for(AdRequest request: requests) {
            ByteBuffer sha256 = ByteBuffer.wrap(request.sha256());
            Csnmessages.AdRequest.Builder builder = requestBuilders.get(sha256);
            if(builder == null) {
                builder = requestBuilder(request);
                requestBuilders.put(sha256, builder);
            } else {
                builder.setNumOfAds(builder.getNumOfAds() + 1);
            }
        }
        Csnmessages.AdRequests.Builder builder = Csnmessages.AdRequests.newBuilder()
                .setDeviceId(Csnmessages.DeviceID.newBuilder()
                        .setDeviceIdType(Csnmessages.DeviceID.Type.AAID)
                        .setDeviceId(ByteString.copyFrom(EncodingUtils.encodeUUID(aaid)))
                        .build())
                .setAdUnit(ByteString.copyFrom(EncodingUtils.encodeUUID(adUnit)))
                .setTimezone(TimeZone.getDefault().getID());

        for(InetAddress addr : ipAddresses) {
            builder.addIpAddresses(ByteString.copyFrom(addr.getAddress()));
        }
        for(Csnmessages.AdRequest.Builder requestBuilder : requestBuilders.values()) {
            builder.addAdRequests(requestBuilder.build());
        }
        return builder.build();
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
        CSNLog.v("Set Ip Addresses: " + ipAddresses.toString());
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
        }).start();
    }


    private synchronized void setDeviceInfo(UUID aaid, boolean limitTracking) {
        CSNLog.v("Set AAID: " + aaid + " Limit Tracking: " + limitTracking);
        this.aaid = aaid;
        this.limitTracking = limitTracking;
        this.reportEngine.setDeviceInfo(aaid, limitTracking);
    }

}
