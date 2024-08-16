package com.android.server.am;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.LogPrinter;
import android.util.Printer;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IBroadcastQueueExt {
    public static final int BROADCAST_NEXT_MSG = 202;
    public static final int mAllowDebugTime = 1000;

    default void adjustOrderedBroadcastReceiversQueue(BroadcastRecord broadcastRecord, int i) {
    }

    default void adjustParallelBroadcastReceiversQueue(BroadcastRecord broadcastRecord) {
    }

    default boolean broadcastIntentMissing(Handler handler) {
        return false;
    }

    default Looper createBroadcastLooper(Handler handler) {
        return null;
    }

    default void deliverBrComplete(boolean z, boolean z2) {
    }

    default int getBroadcastNextMsgValue() {
        return 202;
    }

    default String getBroadcastQueueName() {
        return null;
    }

    default long getLastTimeForDispatchMsg() {
        return 0L;
    }

    default boolean getMessageDelayFlagOfBroadcastRecord(BroadcastRecord broadcastRecord) {
        return false;
    }

    default int getOrderedBroadcastsSize() {
        return 0;
    }

    default void handleBroadcastDeliverException(ActivityManagerService activityManagerService, ProcessRecord processRecord, String str) {
    }

    default void handleNextBroadcastMsg(BroadcastQueue broadcastQueue, ActivityManagerService activityManagerService, Message message, String str) {
    }

    default void handleScheduleCurReceiver(BroadcastRecord broadcastRecord, boolean z) {
    }

    default void hookAfterPerformReceive(BroadcastRecord broadcastRecord, BroadcastFilter broadcastFilter, ProcessRecord processRecord) {
    }

    default void hookAfterScheduleCurReceiver(BroadcastRecord broadcastRecord, ProcessRecord processRecord) {
    }

    default void hookEnqueueParallelBroadcast(ArrayList<BroadcastRecord> arrayList, BroadcastRecord broadcastRecord, String str) {
    }

    default void hookSkipCurrentReceiver(BroadcastQueue broadcastQueue, ProcessRecord processRecord) {
    }

    default boolean hookSkipDeliverReceiver(BroadcastQueue broadcastQueue, BroadcastRecord broadcastRecord, ResolveInfo resolveInfo, BroadcastFilter broadcastFilter, boolean z, boolean z2) {
        return z;
    }

    default boolean hookSkipDeliverReceiverAtTail(Context context, BroadcastQueue broadcastQueue, ProcessRecord processRecord, String str, BroadcastRecord broadcastRecord, ResolveInfo resolveInfo, String str2, boolean z) {
        return z;
    }

    default boolean hookSkipDeliverReceiverAtTail(BroadcastRecord broadcastRecord, ResolveInfo resolveInfo, boolean z) {
        return z;
    }

    default void initOplusBroadcastQueueEx(ActivityManagerService activityManagerService, BroadcastQueue broadcastQueue, Handler handler, String str, BroadcastDispatcher broadcastDispatcher) {
    }

    default boolean isAllowedBySystem(BroadcastRecord broadcastRecord, Object obj) {
        return true;
    }

    default boolean isProxyBySystem(boolean z, BroadcastRecord broadcastRecord, Object obj) {
        return false;
    }

    default void killPhantomProcessWhenUidChanged(ActivityManagerService activityManagerService, ProcessRecord processRecord) {
    }

    default void monitorAppStartupInfo(BroadcastRecord broadcastRecord, ResolveInfo resolveInfo) {
    }

    default boolean optimizationBroadcast(ActivityManagerService activityManagerService, BroadcastRecord broadcastRecord, ProcessRecord processRecord, Handler handler, String str, String str2) {
        return false;
    }

    default boolean removeNextBroadcastMessage(Handler handler, BroadcastRecord broadcastRecord) {
        return false;
    }

    default void removeNextMessages(BroadcastRecord broadcastRecord) {
    }

    default void requestProcessNextBroadcastLocked(boolean z, boolean z2) {
    }

    default void setLastTimeForDispatchMsg(long j) {
    }

    default void setMessageDelayFlagForBroadcastRecord(BroadcastRecord broadcastRecord, boolean z) {
    }

    default boolean shouldPreventStartProcessForBroadcast(Context context, BroadcastQueue broadcastQueue, ProcessRecord processRecord, String str, BroadcastRecord broadcastRecord, ResolveInfo resolveInfo, String str2) {
        return false;
    }

    default Printer getLogPrinterForMsgDump() {
        return new LogPrinter(3, "IBroadcastQueueExt");
    }
}
