package com.android.server.pm.pkg.parsing;

import android.content.pm.ApplicationInfo;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface ParsingPackageHidden {
    int getVersionCode();

    int getVersionCodeMajor();

    ApplicationInfo toAppInfoWithoutState();
}
