package com.android.server;

import android.util.ArraySet;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IPinnerServiceExt {
    default int customizePinLauncherBytes(int i) {
        return i;
    }

    default String[] replaceDefaultFiles(String[] strArr) {
        return strArr;
    }

    default void updateExt(ArraySet<String> arraySet, boolean z) {
    }
}
