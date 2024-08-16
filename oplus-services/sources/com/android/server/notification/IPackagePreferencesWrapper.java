package com.android.server.notification;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IPackagePreferencesWrapper {
    default int getImportance() {
        return 0;
    }

    default IPackagePreferencesExt getPackagePreferencesExt() {
        return null;
    }
}
