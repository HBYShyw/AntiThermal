package com.android.server.wm;

import java.util.LinkedHashMap;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IAnimatingActivityRegistryWrapper {
    default LinkedHashMap<ActivityRecord, Runnable> getFinishedTokens() {
        return new LinkedHashMap<>();
    }
}
