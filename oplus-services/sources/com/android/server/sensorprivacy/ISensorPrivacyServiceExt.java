package com.android.server.sensorprivacy;

import android.content.Context;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface ISensorPrivacyServiceExt {
    public static final int LOCATION = 3;

    default boolean canSkipSetCheckForStealthMode(int i) {
        return false;
    }

    default boolean isStealthSecurityMode() {
        return false;
    }

    default boolean notifySystemUI(Context context, int i) {
        return false;
    }
}
