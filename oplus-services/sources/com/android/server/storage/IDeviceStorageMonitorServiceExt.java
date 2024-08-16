package com.android.server.storage;

import android.content.Context;
import android.os.Handler;
import android.os.ShellCommand;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IDeviceStorageMonitorServiceExt {
    default void dataCheck(Handler handler) {
    }

    default void dumpImpl(PrintWriter printWriter) {
    }

    default long getMemoryLowThresholdInternal() {
        return 0L;
    }

    default void onBootPhase() {
    }

    default void onStart(Handler handler, Context context, DeviceStorageMonitorService deviceStorageMonitorService) {
    }

    default void setCmdForceLevel(String str) {
    }

    default void setForceLevel(int i) {
    }

    default void shellCmdForceFull(ShellCommand shellCommand, Context context, String str, Handler handler) {
    }

    default boolean simulationTest(String[] strArr, PrintWriter printWriter) {
        return false;
    }
}
