package com.android.server;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface ISystemServiceManagerExt {
    default void colorSystemServiceOnBootPhase(int i) {
    }

    default void initTimeCosted() {
    }

    default boolean isDebuggable() {
        return false;
    }

    default void onUserExit(int i) {
    }

    default void recordTimeOut(long j, int i, String str) {
    }

    default void setCustomOnWhatToStart() {
    }

    default void setCustomOnWhatToSwitch() {
    }
}
