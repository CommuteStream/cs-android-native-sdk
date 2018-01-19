package com.commutestream.nativeads;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import com.commutestream.nativeads.components.Component;
import com.commutestream.nativeads.protobuf.Csnmessages;
import com.commutestream.nativeads.reporting.DeviceLocation;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class AdsController {

    public interface AdResponseHandler {
        void onAds(List<Ad> ads);
    }

    public final static String SDK_VERSION = "1.2.6";
    private UUID adUnit;
    private UUID aaid = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private boolean limitTracking = true;
    private Collection<InetAddress> ipAddresses = new ArrayList<>();
    private ReportEngine reportEngine;
    private Activity activity;
    private Client client;
    private AdRenderer adRenderer;
    private VisibilityMonitor visMonitor;
    private SecondaryPopUp popUp;
    private Timer periodicTimer;

    public AdsController(Activity activity, Client client, UUID adUnit) {
        init(activity, client, adUnit);
    }

    public AdsController(Activity activity, UUID adUnit) {
        client = new HttpClient();
        init(activity, client, adUnit);
    }

    /**
     * Start all background activity. Should be called in Activity onResume
     */
    public void resume() {
        startPeriodicTasks();
    }

    /**
     * Pause all background activity. Should be called in Activity onPause
     */
    public void pause() {
        sendReports();
        stopPeriodicTasks();
    }

    /**
     * Fetch ads from the remote server. Returns a List compliant container where each location
     * contains the resulting Ad or null for each AdRequest. The results are passed to the response
     * handler similar to a callback.
     * @param requests
     * @param responseHandler
     */
    public void fetchAds(final List<AdRequest> requests, final AdResponseHandler responseHandler) {
        final ArrayList<Ad> nullAds = new ArrayList<>(requests.size());
        for(AdRequest request : requests) {
            nullAds.add(null);
        }
        final Handler mainHandler = new Handler(activity.getMainLooper());
        final Runnable nullAdsRunner = new Runnable() {
            @Override
            public void run() {
                responseHandler.onAds(nullAds);
            }
        };
        try {
            Csnmessages.AdRequests msg = buildRequestsMessage(requests);
            client.getAds(msg, new Client.AdResponseHandler() {
                @Override
                public void onResponse(Csnmessages.AdResponses responses) {
                    try {
                        final List<Ad> ads = decodeResponses(requests, responses);
                        Runnable adsRunner = new Runnable() {
                            @Override
                            public void run() {
                                responseHandler.onAds(ads);
                            }
                        };
                        mainHandler.post(adsRunner);
                    } catch (Exception ex) {
                        CSNLog.e("Failed to decode ad responses: " + ex);
                        mainHandler.post(nullAdsRunner);
                    }
                }

                @Override
                public void onFailure() {
                    mainHandler.post(nullAdsRunner);
                }
            });
        } catch (Exception ex) {
            CSNLog.e("Failed to encode ad requests: " + ex);
            mainHandler.post(nullAdsRunner);
        }
    }

    /**
     * Render an Ad into a View using a ViewBinder. Returns null and logs the reason on failure.
     * @param viewGroup
     * @param viewBinder
     * @param ad
     * @return
     */
    public View renderAd(ViewGroup viewGroup, ViewBinder viewBinder, Ad ad, boolean parentTouch) {
        try {
            View view = adRenderer.render(viewGroup, viewBinder, ad);
            setupAdViews(view, viewBinder, ad, parentTouch);
            return view;
        } catch (Exception e) {
            CSNLog.e("Failed to render ad: " + e);
            return null;
        }
    }

    /**
     * Render an Ad into a given View using a ViewBinder. Logs the reason on failure.
     * @param view
     * @param viewBinder
     * @param ad
     * @return
     */
    public View renderAdInto(View view, ViewBinder viewBinder, Ad ad, boolean parentTouch) {
        try {
            adRenderer.renderInto(view, viewBinder, ad);
            setupAdViews(view, viewBinder, ad, parentTouch);
            return view;
        } catch (Exception e) {
            CSNLog.e("Failed to render ad: " + e);
            return null;
        }
    }

    private void init(Activity activity, Client client, UUID adUnit) {
        this.activity = activity;
        this.client = client;
        this.adUnit = adUnit;
        reportEngine = new ReportEngine(adUnit, aaid, limitTracking, ipAddresses);
        adRenderer = new AdRenderer(activity);
        visMonitor = new VisibilityMonitor(reportEngine);
        popUp = new SecondaryPopUp(activity, reportEngine);
        loadIpAddresses();
        loadAaid();
        resume();
    }

    private void startPeriodicTasks() {
        if(periodicTimer != null) {
            return;
        }
        final AdsController adsController = this;
        visMonitor.startMonitoring();
        periodicTimer = new Timer("CommuteStream AdsController", true);
        adsController.loadAaid();
        adsController.loadIpAddresses();
        periodicTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                CSNLog.d("running periodic task");
                adsController.loadAaid();
                adsController.loadIpAddresses();
                adsController.loadDeviceLocation();
                adsController.sendReports();
            }
        }, 30000, 30000);
    }

    private void stopPeriodicTasks() {
        visMonitor.stopMonitoring();
        periodicTimer.cancel();
        periodicTimer = null;
    }

    private void sendReports() {
        try {
            Csnmessages.AdReports reports = reportEngine.build();
            CSNLog.d("Reports " + reports);
            this.client.sendReports(reports, new Client.AdReportsHandler() {
                @Override
                public void onResponse() {
                    CSNLog.d("Sent reports");
                }

                @Override
                public void onFailure() {
                    CSNLog.w("Failed to send reports");
                }
            });
        } catch (Exception ex) {
            CSNLog.e("Failed to send reports: " + ex);
        }
    }

    private void setupAdViews(View view, ViewBinder viewBinder, Ad ad, boolean parentTouch) {
        View logoView = view.findViewById(viewBinder.getLogo());
        View headlineView = view.findViewById(viewBinder.getHeadline());
        View bodyView = view.findViewById(viewBinder.getBody());
        monitorView(view, ad, ad.getView());
        monitorView(logoView, ad, ad.getLogo());
        monitorView(headlineView, ad, ad.getHeadline());
        monitorView(bodyView, ad, ad.getBody());
        if(parentTouch) {
            addClickHandler(view, ad, ad.getView());
        } else {
            addClickHandler(logoView, ad, ad.getLogo());
            addClickHandler(headlineView, ad, ad.getHeadline());
            addClickHandler(bodyView, ad, ad.getBody());
        }
    }

    private void monitorView(View view, Ad ad, Component component) {
        if(view != null && component != null) {
            visMonitor.addView(view, ad, component);
        }
    }

    private void addClickHandler(final View view, final Ad ad, final Component component) {
        final AdsController adsController = this;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adsController.handleAdClick(view, ad, component);
            }
        });
    }

    private void handleAdClick(final View view, final Ad ad, final Component component) {
        reportEngine.addInteraction(ad, component, Csnmessages.ComponentInteractionKind.Tap);
        popUp.displayAd(ad);
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
                ads.add(null);
            } else if (adQueue.isEmpty()) {
                ads.add(null);
            } else {
                Ad ad = adQueue.remove();
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
                        .setLimitTracking(limitTracking)
                        .build())
                .setAdUnit(ByteString.copyFrom(EncodingUtils.encodeUUID(adUnit)))
                .setTimezone(TimeZone.getDefault().getID())
                .setSdkVersion(SDK_VERSION);
        Location location = DeviceLocation.getBestLocation(activity);
        if(location != null) {
            builder.addDeviceLocations(EncodingUtils.encodeDeviceLocation(location));
        }
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
        this.ipAddresses = ipAddresses;
        reportEngine.setIpAddresses(ipAddresses);
    }

    private void loadAaid() {
        final AdsController controller = this;
        final Context context = this.activity;
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

    private void loadDeviceLocation() {
        Location location = DeviceLocation.getBestLocation(activity);
        if(location != null) {
            addDeviceLocation(location);
        }
    }

    private synchronized void addDeviceLocation(Location location) {
        this.reportEngine.addLocation(location);
    }

}
