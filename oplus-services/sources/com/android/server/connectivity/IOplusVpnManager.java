package com.android.server.connectivity;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.ComponentName;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IOplusVpnManager extends IOplusCommonFeature {
    public static final IOplusVpnManager DEFAULT = new IOplusVpnManager() { // from class: com.android.server.connectivity.IOplusVpnManager.1
    };
    public static final String NAME = "IOplusVpnManager";

    default boolean isVpnDisabled(ComponentName componentName) {
        return false;
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusVpnManager;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }
}
