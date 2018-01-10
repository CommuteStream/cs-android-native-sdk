package com.commutestream.nativeads.reporting;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.commutestream.nativeads.CSNLog;

public class DeviceLocation {
    private static final int TIME_DELTA = 1000 * 20;
    private static Location lastBestLocation;

    /**
     * Get the best location from all available services then check to see if it's better than
     * the last returned location and return the better of the two.
     *
     * @return location of device if available, null otherwise
     */
    @Nullable
    public static Location getBestLocation(final Context context) {
        Location newLocation = getBestLocationAmongLocationServices(context);

        if (newLocation != null && isBetterLocation(newLocation, lastBestLocation)) {
            lastBestLocation = newLocation;
            return newLocation;
        } else {
            return lastBestLocation;
        }
    }

    /**
     * Attempt to get the locations from whatever services are availible, then return the best one
     */
    @Nullable
    private static Location getBestLocationAmongLocationServices(final Context context) {
        Location gpsLocation = getLastLocationGPS(context);
        Location networkLocation = getLastLocationNetwork(context);

        if (gpsLocation == null) return networkLocation;
        if (networkLocation == null) return gpsLocation;

        //locations are availible from both Network and GPS - return the better one
        if (isBetterLocation(gpsLocation, networkLocation)) return gpsLocation;
        else return networkLocation;
    }

    //attempts to get the last location from GPS
    @SuppressLint("MissingPermission")
    @Nullable
    private static Location getLastLocationGPS(final Context context) {

        //if permissions are not granted return null
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        //attempt to return the last know location
        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch (SecurityException e) {
            CSNLog.e("GPS SecurityException " + e);
        } catch (IllegalArgumentException e) {
            CSNLog.e("IllegalArgumentException " + e);
        }

        return null;
    }


    //attempts to get the last location from the Network
    @Nullable
    private static Location getLastLocationNetwork(final Context context) {

        //if permissions are not granted return null
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        //attempt to return the last know location
        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } catch (SecurityException e) {
            CSNLog.e("GPS SecurityException " + e);
        } catch (IllegalArgumentException e) {
            CSNLog.e("IllegalArgumentException " + e);
        }

        return null;
    }

    /**
     * Determines whether one Location reading is better than the current
     * Location fix
     *
     * @param location            The new Location that you want to evaluate
     * @param currentBestLocation The current Location fix, to which you want to compare the new
     *                            one
     */
    private static boolean isBetterLocation(Location location,
                                            Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TIME_DELTA;
        boolean isSignificantlyOlder = timeDelta < -TIME_DELTA;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use
        // the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be
            // worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
                .getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and
        // accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate
                && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private static boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

}
