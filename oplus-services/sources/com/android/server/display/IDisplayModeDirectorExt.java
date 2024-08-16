package com.android.server.display;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IDisplayModeDirectorExt {
    default int getHeight(int i) {
        return i;
    }

    default int getVrrPolicy(float f) {
        return 0;
    }

    default String getVrrPolicyStr(int i) {
        return "POLICY_DEFAULT";
    }

    default int getWidth(int i) {
        return i;
    }

    default boolean isAdfrEnabled() {
        return false;
    }

    default void registerResolutionChangeListener(Runnable runnable) {
    }
}
