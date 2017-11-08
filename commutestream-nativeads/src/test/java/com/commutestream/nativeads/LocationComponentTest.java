package com.commutestream.nativeads;

import com.commutestream.nativeads.components.LocationComponent;
import com.commutestream.nativeads.protobuf.Csnmessages;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class LocationComponentTest {
    @Test
    public void newFromMessage() {
        long componentID = 404;
        String name = "Moe's Tavern";
        String address = "500 Joker St, Springfield";
        double lat = 32.1;
        double lon = 98.7;
        Csnmessages.LocationComponent msg = Csnmessages.LocationComponent.newBuilder()
                .setComponentId(componentID)
                .setName(name)
                .setAddress(address)
                .setLocation(Csnmessages.Location.newBuilder()
                    .setLat(lat)
                    .setLon(lon)
                    .build())
                .build();
        LocationComponent location = new LocationComponent(msg);
        assertThat(location.getComponentID(), equalTo(componentID));
        assertThat(location.getName(), equalTo(name));
        assertThat(location.getAddress(), equalTo(address));
        assertThat(location.getLatitude(), equalTo(lat));
        assertThat(location.getLongitude(), equalTo(lon));
    }
}
