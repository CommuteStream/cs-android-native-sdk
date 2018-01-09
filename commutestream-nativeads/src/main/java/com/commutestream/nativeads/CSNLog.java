package com.commutestream.nativeads;

import android.util.Log;

public class CSNLog {
    private static final String TAG = "CS_NATIVE";

    public static int d(String msg) { return Log.d(TAG, msg); }

    public static int v(String msg) {
        return Log.v(TAG, msg);
    }

    public static int w(String msg) { return Log.w(TAG, msg); }

    public static int e(String msg) { return Log.e(TAG, msg); }
}
