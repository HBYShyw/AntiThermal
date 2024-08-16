package com.android.server.pm.pkg.component;

import android.annotation.SuppressLint;
import android.util.ArrayMap;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface ParsedProcess {
    @SuppressLint({"ConcreteCollection"})
    ArrayMap<String, String> getAppClassNamesByPackage();

    Set<String> getDeniedPermissions();

    int getGwpAsanMode();

    int getMemtagMode();

    String getName();

    int getNativeHeapZeroInitialized();
}
