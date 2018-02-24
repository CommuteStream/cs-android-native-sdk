package com.commutestream.nativeads.reporting;

import android.location.Location;
import android.os.Build;

import com.commutestream.nativeads.protobuf.Csnmessages;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

public class EncodingUtils {

    private EncodingUtils() {}

    // encode a double visibility percentage (0.0 to 1.0) value into a 4 bit histogram value
    public static byte encodeVisibility(double visibility) {
        return (byte)(Math.round(visibility*15.0) & 0x0F);
    }

    // set a 4 bit portion of a 64bit value to the sample value which is a 4 bit value itself
    public static long setVisibilitySample(long sample, int position, byte value) {
        long shift = position*4;
        long valueMask = 0x000000000000000FL;
        valueMask = valueMask << shift;
        long sampleMask = valueMask ^ 0xFFFFFFFFFFFFFFFFL;
        long shiftedValue = ((long)value) << shift;
        long maskedSample = sample & sampleMask;
        long newSample = maskedSample | shiftedValue;
        return newSample;
    }

    public static byte[] encodeUUID(UUID uuid) {
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    public static Csnmessages.DeviceLocation encodeDeviceLocation(Location location) {
        // All units are in *meters* and *degrees*
        Csnmessages.DeviceLocation.Builder locationBuilder = Csnmessages.DeviceLocation.newBuilder()
                .setTimestamp(location.getTime())
                .setLatitude(location.getLatitude())
                .setLongitude(location.getLongitude())
                .setAltitude(location.getAccuracy())
                .setBearing(location.getBearing())
                .setSpeed(location.getSpeed())
                .setHorizontalAccuracy(location.getAccuracy())
                .setProvider(location.getProvider());
        if(Build.VERSION.SDK_INT >= 26) {
            locationBuilder
                    .setVerticalAccuracy(location.getVerticalAccuracyMeters())
                    .setBearingAccuracy(location.getBearingAccuracyDegrees())
                    .setSpeedAccuracy(location.getSpeedAccuracyMetersPerSecond());
        }
        return locationBuilder.build();
    }
}
