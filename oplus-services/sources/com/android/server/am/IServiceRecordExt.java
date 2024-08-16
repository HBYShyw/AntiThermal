package com.android.server.am;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IServiceRecordExt {
    public static final int callingPid = -1;
    public static final int callingUid = 0;
    public static final String type = "";

    default String getCallerAppPackage() {
        return null;
    }

    default int getCallingPid() {
        return -1;
    }

    default int getCallingUid() {
        return 0;
    }

    default boolean getExceptionWhenBringUp() {
        return false;
    }

    default int getRestartDelayPromoteCount() {
        return 0;
    }

    default String getType() {
        return "";
    }

    default void incRestartDelayPromoteCount() {
    }

    default void resetRestartDelayPromoteCount() {
    }

    default void setCallerAppPackage(String str) {
    }

    default void setCallingPid(int i) {
    }

    default void setCallingUid(int i) {
    }

    default void setExceptionWhenBringUp(boolean z) {
    }

    default void setType(String str) {
    }
}
