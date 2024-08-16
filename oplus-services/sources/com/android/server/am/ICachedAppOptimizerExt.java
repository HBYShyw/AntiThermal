package com.android.server.am;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface ICachedAppOptimizerExt {
    default boolean checkFreezeProc(ProcessRecord processRecord) {
        return true;
    }

    default boolean checkUnfreezeProc(ProcessRecord processRecord) {
        return true;
    }
}
