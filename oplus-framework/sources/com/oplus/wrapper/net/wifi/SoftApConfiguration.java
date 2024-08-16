package com.oplus.wrapper.net.wifi;

import android.net.MacAddress;
import android.net.wifi.SoftApConfiguration;
import java.util.List;

/* loaded from: classes.dex */
public class SoftApConfiguration {
    public static final int BAND_2GHZ = 1;
    public static final int BAND_5GHZ = 2;
    public static final int BAND_60GHZ = 8;
    public static final int BAND_6GHZ = 4;
    private final android.net.wifi.SoftApConfiguration mSoftApConfiguration;

    public SoftApConfiguration(android.net.wifi.SoftApConfiguration softApConfiguratiion) {
        this.mSoftApConfiguration = softApConfiguratiion;
    }

    public int getBand() {
        return this.mSoftApConfiguration.getBand();
    }

    /* loaded from: classes.dex */
    public static final class Builder {
        private final SoftApConfiguration.Builder mBuilder;

        public Builder() {
            this.mBuilder = new SoftApConfiguration.Builder();
        }

        public Builder(android.net.wifi.SoftApConfiguration other) {
            this.mBuilder = new SoftApConfiguration.Builder(other);
        }

        public Builder setBand(int band) {
            this.mBuilder.setBand(band);
            return this;
        }

        public Builder setBlockedClientList(List<MacAddress> blockedClientList) {
            this.mBuilder.setBlockedClientList(blockedClientList);
            return this;
        }

        public android.net.wifi.SoftApConfiguration build() {
            return this.mBuilder.build();
        }
    }
}
