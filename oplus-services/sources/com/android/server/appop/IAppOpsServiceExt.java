package com.android.server.appop;

import android.content.Context;
import android.os.Binder;
import android.os.Handler;
import android.util.SparseArray;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IAppOpsServiceExt {
    public static final int CUSTOM_NUM_OP = 10003;
    public static final int OP_CUSTOM_NONE = 10000;

    default void addCustomSwitchedOps(SparseArray<int[]> sparseArray) {
    }

    default void checkCapabilityFailed(int[] iArr) {
    }

    default void hookServiceStart(Binder binder) {
    }

    default void hookSetAudioRestriction(Context context, int i, int i2, int i3, int i4, Handler handler) {
    }

    default boolean isActivityPreloadPkg(String str, int i) {
        return false;
    }

    default void notifyPermissionRecordAsUser(String str, int i, int i2, int i3) {
    }

    default void shouldBackupAppOpsXml() {
    }

    default void syncOpForMultiApp(int i, int i2, String str, int i3, Handler handler) {
    }
}
