package com.android.server.devicepolicy;

import android.app.admin.SecurityLog;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class CryptoTestHelper {
    private static native int runSelfTest();

    public static void runAndLogSelfTest() {
        SecurityLog.writeEvent(210031, new Object[]{Integer.valueOf(runSelfTest())});
    }
}
