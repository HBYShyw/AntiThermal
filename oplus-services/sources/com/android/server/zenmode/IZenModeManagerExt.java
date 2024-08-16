package com.android.server.zenmode;

import android.content.Context;
import android.content.pm.ActivityInfo;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IZenModeManagerExt {
    public static final boolean DEBUG = false;
    public static final String NAME = "IZenModeManager";

    default void addCallback(Object obj) {
    }

    default boolean canActivityGo(ActivityInfo activityInfo) {
        return true;
    }

    default boolean canEnterPictureInPicture(String str, int i) {
        return true;
    }

    default boolean canInitAppOpVisibilityLw(String str, int i, int i2) {
        return true;
    }

    default boolean canSetAppOpVisibilityLw(String str, int i) {
        return true;
    }

    default boolean canSetLights(int i, int i2, int i3) {
        return true;
    }

    default boolean canVibrationGo(String str) {
        return true;
    }

    default void initEnv(Context context) {
    }

    default boolean isZenModeOn() {
        return false;
    }

    default boolean needBlockWakeUp(int i, String str, String str2) {
        return false;
    }

    default boolean shouldBlockNotifSound(String str) {
        return false;
    }
}
