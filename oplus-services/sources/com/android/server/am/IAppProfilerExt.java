package com.android.server.am;

import android.os.Handler;
import com.android.internal.os.ProcessCpuTracker;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IAppProfilerExt {
    default void boost(ProcessProfileRecord processProfileRecord, int i) {
    }

    default boolean checkCPUBusy() {
        return false;
    }

    default boolean filterNativeProcessGetPss(ProcessCpuTracker.Stats stats) {
        return false;
    }

    default boolean isEnableHighLoadSkipPss() {
        return true;
    }

    default boolean isGameScene() {
        return false;
    }

    default boolean isNeedSkipDumpPss(Handler handler) {
        return false;
    }

    default boolean isSkipTrimMemoryForQuickBootScene(String str) {
        return false;
    }

    default void recordPssStats(String str, String str2, int i, int i2, int i3, long j, long j2, long j3, long j4) {
    }

    default void reset(ProcessProfileRecord processProfileRecord) {
    }
}
