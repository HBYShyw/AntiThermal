package com.android.server.clipboard;

import android.app.AppOpsManager;
import android.content.ClipData;
import android.content.Context;
import android.os.Binder;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IClipboardServiceExt {
    default String getAccessNotificationMessage(Context context, int i, String str, CharSequence charSequence, String str2) {
        return str2;
    }

    default boolean hookClipboardAccessAllowedResult(int i) {
        return false;
    }

    default ClipData hookGetPrimaryClipResult(Context context, ClipData clipData, AppOpsManager appOpsManager, String str, int i, int i2, int i3, int i4) {
        return clipData;
    }

    default void hookServiceReady(Context context, ClipboardService clipboardService) {
    }

    default void hookServiceStart(Binder binder) {
    }

    default boolean hookShowAccessNotification(Context context, String str, int i, int i2, AppOpsManager appOpsManager) {
        return true;
    }

    default boolean isActivityPreloadingPkg(String str) {
        return false;
    }

    default boolean isPrivilegedPackage(String str, Context context) {
        return false;
    }
}
