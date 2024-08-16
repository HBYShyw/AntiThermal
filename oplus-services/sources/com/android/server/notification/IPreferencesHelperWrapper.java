package com.android.server.notification;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IPreferencesHelperWrapper {
    default int getImportanceOfPackage(String str, int i) {
        return 0;
    }

    default IPackagePreferencesExt getOrCreatePackagePreferencesExt(String str, int i) {
        return null;
    }

    default IPreferencesHelperExt getPreferencesHelperExt() {
        return null;
    }

    default void updateConfig() {
    }
}
