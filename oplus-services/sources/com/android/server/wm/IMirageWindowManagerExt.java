package com.android.server.wm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IMirageWindowManagerExt {
    public static final int DISPLAY_ID = 2020;

    default void applySurfacePrivacyProtectionPolicy(boolean z, boolean z2, WindowState windowState) {
    }

    default void onDisplayAdded(int i) {
    }

    default void onDisplayRemoved(int i) {
    }

    default boolean onGoingToSleep(int i) {
        return true;
    }

    default void onTaskAdded(int i, Task task) {
    }

    default void onTaskRemoved(int i, Task task) {
    }

    default boolean shouldForceLauncherVisible() {
        return false;
    }

    default boolean shouldHideTaskInRecents(Task task) {
        return false;
    }

    default boolean shouldReparentToNull(int i) {
        return false;
    }
}
