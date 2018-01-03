package com.commutestream.nativeads;

import android.util.Log;

public class CSNLog {
    private static final String TAG = "CS_NATIVE";

    public static int v(String msg) {
        return Log.v(TAG, msg);
    }

    public static int e(String msg) { return Log.e(TAG, msg); }
}
