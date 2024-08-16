package com.oplus.wrapper.net;

/* loaded from: classes.dex */
public class WebAddress {
    private final android.net.WebAddress mWebAddress;

    public WebAddress(String address) {
        this.mWebAddress = new android.net.WebAddress(address);
    }

    public String getScheme() {
        return this.mWebAddress.getScheme();
    }

    public String getHost() {
        return this.mWebAddress.getHost();
    }

    public void setPath(String path) {
        this.mWebAddress.setPath(path);
    }

    public String getPath() {
        return this.mWebAddress.getPath();
    }

    public String toString() {
        return this.mWebAddress.toString();
    }
}
