package com.oplus.wrapper.net;

/* loaded from: classes.dex */
public class NetworkTemplate {
    private final android.net.NetworkTemplate mNetworkTemplate;

    public NetworkTemplate(android.net.NetworkTemplate networkTemplate) {
        this.mNetworkTemplate = networkTemplate;
    }

    public static NetworkTemplate buildTemplateMobileAll(String subscriberId) {
        android.net.NetworkTemplate networkTemplate = android.net.NetworkTemplate.buildTemplateMobileAll(subscriberId);
        if (networkTemplate == null) {
            return null;
        }
        return new NetworkTemplate(networkTemplate);
    }

    public static NetworkTemplate buildTemplateWifiWildcard() {
        android.net.NetworkTemplate networkTemplate = android.net.NetworkTemplate.buildTemplateWifiWildcard();
        if (networkTemplate == null) {
            return null;
        }
        return new NetworkTemplate(networkTemplate);
    }

    public android.net.NetworkTemplate getNetworkTemplate() {
        return this.mNetworkTemplate;
    }
}
