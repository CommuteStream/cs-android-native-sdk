package com.commutestream.nativeads;

/**
 * Bind views to various ad components
 */
public class ViewBinder {
    private int mLayout = 0;
    private int mLogo = 0;
    private int mTitle = 0;
    private int mSubtitle = 0;

    public ViewBinder(int layout) {
        mLayout = layout;
    }

    public int getLayout() {
        return mLayout;
    }

    public void setLogo(int logo) {
        mLogo = logo;
    }

    public int getLogo() {
        return mLogo;
    }

    public void setTitle(int title) {
        mTitle = title;
    }

    public void setSubtitle(int subtitle) {
        mSubtitle = subtitle;
    }
}
