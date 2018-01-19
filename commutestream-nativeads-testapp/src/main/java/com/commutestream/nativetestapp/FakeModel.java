package com.commutestream.nativetestapp;

import com.commutestream.nativeads.Ad;

public class FakeModel {
    private int mIdx;
    private Ad mAd;

    public FakeModel(int idx, Ad ad) {
        mIdx = idx;
        mAd = ad;
    }

    public int getIdx() {
        return mIdx;
    }

    public Ad getAd() {
        return mAd;
    }
}
