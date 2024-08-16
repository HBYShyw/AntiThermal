package com.android.server.wm;

import android.content.pm.ActivityInfo;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IActivityRecordSocExt {
    default void acquireActivityBoost(String str, WindowProcessController windowProcessController, ActivityInfo activityInfo, ActivityTaskManagerService activityTaskManagerService, String str2) {
    }

    default int getPerfActivityBoostHandler() {
        return -1;
    }

    default void hookOnWindowsDrawn() {
    }

    default void initSoc() {
    }

    default int isAppInfoGame(ActivityInfo activityInfo) {
        return 0;
    }

    default boolean isEnableBoostFramework() {
        return false;
    }

    default boolean isLaunching() {
        return false;
    }

    default void perfLockReleaseHandler() {
    }

    default void releaseActivityBoost() {
    }

    default void setLaunching(boolean z) {
    }

    default void setPerfActivityBoostHandler(int i) {
    }

    default void setTranslucentWindowLaunch(boolean z) {
    }
}
