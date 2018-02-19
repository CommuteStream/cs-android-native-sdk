package com.commutestream.nativeads;

/**
 * Bind views to various ad components
 */
public class ViewBinder {
    private int layout = 0;
    private int logo = 0;
    private int headline = 0;
    private int body = 0;
    private int advertiser = 0;

    public ViewBinder(int layout) {
        this.layout = layout;
    }

    public int getLayout() {
        return this.layout;
    }

    public ViewBinder setLogo(int logo) {
        this.logo = logo;
        return this;
    }

    public int getLogo() {
        return logo;
    }

    public ViewBinder setHeadline(int headline) {
        this.headline = headline;
        return this;
    }

    public int getHeadline() {
        return this.headline;
    }

    public int getBody() {
        return body;
    }

    public ViewBinder setBody(int body) {
        this.body = body;
        return this;
    }

    public int getAdvertiser() {
        return advertiser;
    }

    public ViewBinder setAdvertiser(int advertiser) {
        this.advertiser = advertiser;
        return this;
    }
}
