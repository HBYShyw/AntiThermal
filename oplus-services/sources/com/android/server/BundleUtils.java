package com.android.server;

import android.os.Bundle;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class BundleUtils {
    private BundleUtils() {
    }

    public static boolean isEmpty(Bundle bundle) {
        return bundle == null || bundle.size() == 0;
    }

    public static Bundle clone(Bundle bundle) {
        return bundle != null ? new Bundle(bundle) : new Bundle();
    }
}
