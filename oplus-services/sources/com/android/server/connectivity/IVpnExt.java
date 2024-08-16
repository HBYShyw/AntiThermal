package com.android.server.connectivity;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import com.android.internal.net.VpnConfig;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IVpnExt {
    default boolean doesHaveVPNAppWhiteList(ComponentName componentName) {
        return false;
    }

    default void hideNotification(int i) {
    }

    default void init(Context context) {
    }

    default boolean isInVPNAppWhiteList(ComponentName componentName, String str) {
        return false;
    }

    default boolean isVpnDisabled(ComponentName componentName) {
        return false;
    }

    default VpnConfig parseApplicationsFromXml(VpnConfig vpnConfig) {
        return vpnConfig;
    }

    default PendingIntent prepareStatusIntent(PendingIntent pendingIntent) {
        return pendingIntent;
    }

    default void showNotification(String str, int i, int i2, String str2, PendingIntent pendingIntent, VpnConfig vpnConfig) {
    }
}
