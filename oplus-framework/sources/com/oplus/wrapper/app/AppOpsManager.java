package com.oplus.wrapper.app;

/* loaded from: classes.dex */
public class AppOpsManager {
    public static final int OP_POST_NOTIFICATION = 11;
    private final android.app.AppOpsManager mAppOpsManager;
    public static final int OP_GET_USAGE_STATS = getOpGetUsageStats();
    public static final int OP_WRITE_SETTINGS = getOpWriteSettings();

    public AppOpsManager(android.app.AppOpsManager manager) {
        this.mAppOpsManager = manager;
    }

    private static int getOpGetUsageStats() {
        return 43;
    }

    private static int getOpWriteSettings() {
        return 23;
    }

    public int checkOp(int op, int uid, String packageName) {
        return this.mAppOpsManager.checkOp(op, uid, packageName);
    }

    public int checkOpNoThrow(int op, int uid, String packageName) {
        return this.mAppOpsManager.checkOpNoThrow(op, uid, packageName);
    }
}
