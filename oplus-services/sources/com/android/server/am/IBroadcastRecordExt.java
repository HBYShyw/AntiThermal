package com.android.server.am;

import android.content.Intent;
import java.io.PrintWriter;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IBroadcastRecordExt {
    default void dumpDeliveryState(PrintWriter printWriter, int i) {
    }

    default int getCtrlType() {
        return -1;
    }

    default boolean getIgnoreBrOpt() {
        return false;
    }

    default boolean ignoreBlockUntil(Intent intent) {
        return false;
    }

    default void init(int i) {
    }

    default Intent setAndGetBackupIntent(Intent intent) {
        return null;
    }

    default void setCtrlType(int i) {
    }

    default void setDeliveryState(int i, long j, int i2) {
    }

    default void setIgnoreBrOpt(boolean z) {
    }

    default void setSkipReason(BroadcastRecord broadcastRecord, int i, int i2, String str) {
    }

    default int[] calculateBlockedUntilBeyondCount(List<Object> list, boolean z, Intent intent) {
        return BroadcastRecord.calculateBlockedUntilBeyondCount(list, z);
    }
}
