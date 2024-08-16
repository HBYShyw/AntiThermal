package com.android.server;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IWatchdogSocExt {
    default void WDTMatterJava(long j) {
    }

    default void getExceptionLog() {
    }

    default long getSfHangTime() {
        return 0L;
    }

    default int getSfRebootTime() {
        return 0;
    }

    default void setSfRebootTime() {
    }

    default void switchFtrace(int i) {
    }
}
