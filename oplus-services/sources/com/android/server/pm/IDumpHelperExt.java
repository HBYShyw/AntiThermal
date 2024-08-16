package com.android.server.pm;

import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IDumpHelperExt {
    default boolean customLogicInDump(String str, PrintWriter printWriter, String[] strArr, int i) {
        return false;
    }

    default boolean hasOplusPackageName(String str) {
        return false;
    }
}
