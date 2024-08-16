package com.android.server.wm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IDisplayWindowSettingsExt {
    default boolean skipSetWindowingMode(DisplayContent displayContent, boolean z, int i) {
        return false;
    }
}
