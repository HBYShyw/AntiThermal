package com.oplus.wrapper.net.wifi;

/* loaded from: classes.dex */
public class WifiConfiguration {
    private final android.net.wifi.WifiConfiguration mWifiConfiguration;

    public WifiConfiguration(android.net.wifi.WifiConfiguration wifiConfiguration) {
        this.mWifiConfiguration = wifiConfiguration;
    }

    public void setApBand(int apBand) {
        this.mWifiConfiguration.apBand = apBand;
    }

    public int getApBand() {
        return this.mWifiConfiguration.apBand;
    }

    public void setApChannel(int apChannel) {
        this.mWifiConfiguration.apChannel = apChannel;
    }

    public int getApChannel() {
        return this.mWifiConfiguration.apChannel;
    }

    public boolean getRequirePmf() {
        return this.mWifiConfiguration.requirePmf;
    }

    /* loaded from: classes.dex */
    public static class KeyMgmt {
        public static final int DPP = getDpp();

        private static int getDpp() {
            return 17;
        }
    }
}
