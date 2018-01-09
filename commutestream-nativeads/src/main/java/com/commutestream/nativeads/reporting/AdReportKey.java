package com.commutestream.nativeads.reporting;

import com.commutestream.nativeads.Ad;

public class AdReportKey {
    long adID;
    long versionID;
    long requestID;

    AdReportKey(Ad ad) {
        adID = ad.getAdID();
        versionID = ad.getVersionID();
        requestID = ad.getRequestID();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        AdReportKey k = (AdReportKey) obj;
        if(k == null) {
            return false;
        }
        return adID == k.adID && versionID == k.versionID && requestID == k.requestID;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(adID).hashCode() ^ Long.valueOf(versionID).hashCode() ^ Long.valueOf(requestID).hashCode();
    }
}
