package com.android.server;

import android.content.Context;
import android.os.DropBoxManager;
import java.util.HashMap;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IBootReceiverExt {
    default void addFile(DropBoxManager dropBoxManager, HashMap<String, Long> hashMap, String str, Context context) {
    }

    default void hookAddTombstoneToDropBox(String str) {
    }

    default void incrementCriticalDataAndRecordRebootBlocked() {
    }

    default void init(Context context) {
    }

    default void initPowerkeyMonitor() {
    }

    default void notifyOTAUpdateResult(Context context) {
    }

    default void recordAbnormalRestart(DropBoxManager dropBoxManager) {
    }

    default void syncCacheToEmmc() {
    }
}
