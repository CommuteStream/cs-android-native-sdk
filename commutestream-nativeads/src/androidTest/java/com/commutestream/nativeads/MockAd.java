package com.commutestream.nativeads;

import com.commutestream.nativeads.protobuf.Csnmessages;

public class MockAd {

    public Csnmessages.AdRequest createMockRequest() {
        return Csnmessages.AdRequest.newBuilder()
                .addAgencies(Csnmessages.TransitAgency.newBuilder()
                .setAgencyId("test_agency_id")
                .build()
                )
                .addRoutes(Csnmessages.TransitRoute.newBuilder()
                .setAgencyId("test_agency_id")
                .setRouteId("test_route_id")
                .build()
                )
                .addStops(Csnmessages.TransitStop.newBuilder()
                .setAgencyId("test_agency_id")
                .setRouteId("test_route_id")
                .setStopId("test_stop_id"))
                .build();
    }
    public Csnmessages.AdResponse createMockResponse(Csnmessages.AdRequest request) {
        return null;
    }
}
