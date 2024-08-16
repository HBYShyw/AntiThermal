package com.android.server.pm;

import android.content.pm.ApplicationInfo;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IHbtUtilSocExt {
    default void hbtCheckInstall(PackageSetting packageSetting, PackageSetting packageSetting2, boolean z) {
    }

    default void hbtCheckStatus(String str, String[] strArr) {
    }

    default void hbtCheckUninstall(String str, String[] strArr) {
    }

    default void hbtCheckUpdate(String str, String[] strArr, String[] strArr2) {
    }

    default boolean isHbt64BitOnlyChip() {
        return false;
    }

    default String getHbtPrimaryCpuAbi(ApplicationInfo applicationInfo) {
        return applicationInfo.primaryCpuAbi;
    }
}
