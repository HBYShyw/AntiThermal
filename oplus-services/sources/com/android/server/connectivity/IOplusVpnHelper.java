package com.android.server.connectivity;

import android.app.PendingIntent;
import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import com.android.internal.net.VpnConfig;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IOplusVpnHelper extends IOplusCommonFeature {
    public static final IOplusVpnHelper DEFAULT = new IOplusVpnHelper() { // from class: com.android.server.connectivity.IOplusVpnHelper.1
    };
    public static final String NAME = "IOplusVpnHelper";

    default void hideNotification(int i) {
    }

    default VpnConfig parseApplicationsFromXml(VpnConfig vpnConfig) {
        return vpnConfig;
    }

    default PendingIntent prepareStatusIntent(PendingIntent pendingIntent) {
        return pendingIntent;
    }

    default void showNotification(String str, int i, int i2, String str2, PendingIntent pendingIntent, VpnConfig vpnConfig) {
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusVpnHelper;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }
}
