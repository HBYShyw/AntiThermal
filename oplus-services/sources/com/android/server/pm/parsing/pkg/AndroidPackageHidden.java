package com.android.server.pm.parsing.pkg;

import android.content.pm.ApplicationInfo;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
interface AndroidPackageHidden {
    String getPrimaryCpuAbi();

    String getSecondaryCpuAbi();

    @Deprecated
    int getVersionCode();

    int getVersionCodeMajor();

    boolean isOdm();

    boolean isOem();

    boolean isPrivileged();

    boolean isProduct();

    boolean isSystem();

    boolean isSystemExt();

    boolean isVendor();

    ApplicationInfo toAppInfoWithoutState();
}
