package com.android.server.om;

import com.android.server.pm.pkg.AndroidPackage;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IOverlayPackageCacheExt {
    default void addExtPackageUser(AndroidPackage androidPackage, int i) {
    }

    default AndroidPackage getPackageForUser(String str, int i) {
        return null;
    }

    default AndroidPackage onPackageAdded(String str, int i) {
        return null;
    }

    default void onPackageRemoved(String str, int i) {
    }
}
