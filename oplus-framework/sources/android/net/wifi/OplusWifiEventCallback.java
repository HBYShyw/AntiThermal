package android.net.wifi;

import android.os.Bundle;

/* loaded from: classes.dex */
public interface OplusWifiEventCallback {
    public static final int ACTION_START = 1;
    public static final int ACTION_STOP = 0;
    public static final int EVENT_WIFI_DUAL_STA = 8;
    public static final int EVENT_WIFI_FDD_V2 = 15;
    public static final int EVENT_WIFI_FDD_V3 = 16;
    public static final int EVENT_WIFI_GAME_LATENCY = 10;
    public static final int EVENT_WIFI_GAME_STUCK = 11;
    public static final int EVENT_WIFI_HOTSPOT_LOCAL_ONLY = 5;
    public static final int EVENT_WIFI_HOTSPOT_TETHERED = 6;
    public static final int EVENT_WIFI_LINK_QUALITY = 9;
    public static final int EVENT_WIFI_MEDIA_STUCK = 12;
    public static final int EVENT_WIFI_NET_SLOW = 13;
    public static final int EVENT_WIFI_P2P_CONNECT_GC = 4;
    public static final int EVENT_WIFI_P2P_CONNECT_GO = 3;
    public static final int EVENT_WIFI_SCAN_EVENT = 0;
    public static final int EVENT_WIFI_SLA = 7;
    public static final int EVENT_WIFI_STA_CONNECT_PRIMARY = 1;
    public static final int EVENT_WIFI_STA_CONNECT_SECONDARY = 2;
    public static final int EVENT_WIFI_TDD_V2 = 14;
    public static final String EXTRA_OPERATOR = "operator";

    void onEvent(int i, int i2, Bundle bundle);
}
