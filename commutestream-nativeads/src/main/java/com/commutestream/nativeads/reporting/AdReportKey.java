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
}
