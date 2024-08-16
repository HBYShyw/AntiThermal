package com.android.server.am;

import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.util.SparseArray;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IBroadcastQueueModernImplExt {
    public static final int DELIVERY_GROUP_POLICY = 1;
    public static final int REPLACE_PENDING = 2;

    default void assertHealthLocked(BroadcastProcessQueue broadcastProcessQueue) {
    }

    default void beginAssertHealthLocked() {
    }

    default void broadcastStatistic(ProcessRecord processRecord, BroadcastRecord broadcastRecord, Object obj, int i) {
    }

    default void dumpsys(PrintWriter printWriter, String[] strArr) {
    }

    default void endAssertHealthLocked(SparseArray<BroadcastProcessQueue> sparseArray, Handler handler) {
    }

    default void handleBroadcastTimeout(BroadcastRecord broadcastRecord, ProcessRecord processRecord, int i) {
    }

    default void handleEnqueuedBroadcastOption(BroadcastRecord broadcastRecord, int i, int i2) {
    }

    default void hookCreateProcessQueue(BroadcastProcessQueue broadcastProcessQueue) {
    }

    default void hookScheduleReceiverColdAfterStartProc(BroadcastRecord broadcastRecord, ResolveInfo resolveInfo) {
    }

    default boolean ignoreAnr(ProcessRecord processRecord, BroadcastRecord broadcastRecord) {
        return false;
    }

    default void initArgs(ActivityManagerService activityManagerService, Handler handler) {
    }

    default boolean shouldSkipReceiver(BroadcastRecord broadcastRecord, Object obj) {
        return false;
    }

    default boolean skipReceiverForOsense(BroadcastRecord broadcastRecord, Object obj) {
        return false;
    }

    default String skipScheduleReceiverColdLocked(BroadcastQueue broadcastQueue, BroadcastRecord broadcastRecord, ResolveInfo resolveInfo) {
        return null;
    }

    default String skipScheduleReceiverWarmLocked(BroadcastQueue broadcastQueue, BroadcastRecord broadcastRecord, Object obj) {
        return null;
    }
}
