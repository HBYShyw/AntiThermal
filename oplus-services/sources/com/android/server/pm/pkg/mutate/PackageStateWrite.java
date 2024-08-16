package com.android.server.pm.pkg.mutate;

import android.content.pm.PackageManager;
import android.util.ArraySet;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface PackageStateWrite {
    void onChanged();

    PackageStateWrite setCategoryOverride(int i);

    PackageStateWrite setHiddenUntilInstalled(boolean z);

    PackageStateWrite setInstaller(String str, int i);

    PackageStateWrite setLastPackageUsageTime(@PackageManager.NotifyReason int i, long j);

    PackageStateWrite setLoadingCompletedTime(long j);

    PackageStateWrite setLoadingProgress(float f);

    PackageStateWrite setMimeGroup(String str, ArraySet<String> arraySet);

    PackageStateWrite setOverrideSeInfo(String str);

    PackageStateWrite setRequiredForSystemUser(boolean z);

    PackageStateWrite setUpdateAvailable(boolean z);

    PackageStateWrite setUpdateOwner(String str);

    PackageUserStateWrite userState(int i);
}
