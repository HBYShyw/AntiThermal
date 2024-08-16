package com.android.server.am;

import android.os.Message;
import android.util.IntArray;
import com.android.server.wm.WindowProcessController;
import java.io.PrintWriter;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IOomAdjusterExt {
    default void adjustTopApp(String str, int i, int i2, IntArray intArray) {
    }

    default void adjustUxProcess(ProcessRecord processRecord, int i, int i2) {
    }

    default void bindSmallCore(ProcessRecord processRecord, ProcessRecord processRecord2) {
    }

    default void dumpOomAdjStatsLocked(PrintWriter printWriter) {
    }

    default BroadcastQueue getOplusFgBroadcastQueue() {
        return null;
    }

    default void handleImportantChanged(ProcessRecord processRecord, int i, int i2) {
    }

    default boolean isFrozen(int i) {
        return false;
    }

    default boolean isOclGrpRequestMsgAndSetGroup(Message message) {
        return false;
    }

    default void notifyProcGrpChange(ProcessRecord processRecord, int i) {
    }

    default boolean onHookKillCacheEmpty(WindowProcessController windowProcessController) {
        return false;
    }

    default void onHookadjustUxProcess(ProcessRecord processRecord, int i, int i2, boolean z) {
    }

    default void onOomAdjUpdateLSP(ProcessRecord processRecord, int i, boolean z, String str) {
    }

    default void onPendingOomAdjUpdateLSP(ArrayList<ProcessRecord> arrayList, int i, boolean z, String str) {
    }

    default void setFullOomAdjUpdateInfo(int i, String str, String str2) {
    }

    default void setImportantAppAdj(ProcessRecord processRecord, ProcessRecord processRecord2) {
    }

    default void setProcRecdOldSchedGroup(ProcessRecord processRecord, int i) {
    }

    default boolean skipSetSchedGroup(ProcessRecord processRecord) {
        return false;
    }

    default boolean treatAsFgBroadcast(ProcessRecord processRecord) {
        return false;
    }

    default void updateRecentLockApps() {
    }
}
