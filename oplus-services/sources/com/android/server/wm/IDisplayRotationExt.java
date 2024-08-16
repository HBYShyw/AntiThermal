package com.android.server.wm;

import android.content.Context;
import android.os.Handler;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IDisplayRotationExt {
    default int adjustRotationForReverseLandscape(DisplayContent displayContent, int i) {
        return -1;
    }

    default int blockAllowAllRotationsInTable(int i, DisplayContent displayContent) {
        return i;
    }

    default boolean checkForceUpdate(DisplayContent displayContent) {
        return false;
    }

    default void continueRotation() {
    }

    default boolean drawComplete(DisplayContent displayContent, boolean z, boolean z2) {
        return false;
    }

    default boolean enableRequestOrientationWhenDeviceFolding(DisplayContent displayContent) {
        return false;
    }

    default int forceLauncherRotate(int i, WindowContainer windowContainer) {
        return i;
    }

    default boolean forceSeamlesslyRotated(WindowState windowState, String str) {
        return false;
    }

    default void forceUpdateRotationForCanvas(boolean z) {
    }

    default int getFixedRotationForOrientation(int i, DisplayContent displayContent, int i2) {
        return -1;
    }

    default int getMirageDisplaySensorRotation(int i) {
        return -1;
    }

    default int getMirageFixedRotation(int i) {
        return -1;
    }

    default int getMirageInitialRotation(int i) {
        return -1;
    }

    default int getSuggestRotationForBracketMode() {
        return -1;
    }

    default boolean hasMaskAnimation() {
        return false;
    }

    default int hookActivityOrientation(int i, int i2, DisplayContent displayContent) {
        return i2;
    }

    default int hookLockedRotation(int i, DisplayContent displayContent) {
        return i;
    }

    default int hookUpdateSensorRotation(int i, DisplayContent displayContent) {
        return i;
    }

    default boolean isDisplayEnable(DisplayContent displayContent, boolean z) {
        return false;
    }

    default boolean isForceAllowAllOrientation(DisplayContent displayContent) {
        return false;
    }

    default boolean isSecondDisplay(DisplayContent displayContent) {
        return false;
    }

    default boolean isValidRotationChoice(int i, int i2) {
        return i >= 0 && i != i2;
    }

    default int modifyFreezeRotationWhenDeviceFolding(int i) {
        return i;
    }

    default boolean omitRotationChange(DisplayContent displayContent, int i) {
        return false;
    }

    default void onProposedRotationChanged(int i, int i2, DisplayContent displayContent) {
    }

    default void registerFoldStateListener(Context context, Handler handler, Object obj) {
    }

    default int resolvePreferredRotationInSecondary(int i, int i2, int i3) {
        return -1;
    }

    default void setForceUpdateRotation(boolean z) {
    }

    default void setSensorRotationChanged(DisplayContent displayContent, boolean z) {
    }

    default boolean shouldEnableOrientationListener(DisplayContent displayContent, boolean z) {
        return false;
    }

    default boolean shouldFreezeScreenOrientation() {
        return false;
    }

    default boolean shouldKeepSensorRotationInFixRotation(DisplayContent displayContent, int i, int i2, int i3) {
        return false;
    }

    default int shouldSuggestEnterBracketMode() {
        return -1;
    }

    default boolean skipSendProposedRotationChangeToStatusBar(int i, DisplayContent displayContent) {
        return false;
    }

    default boolean stopRotationInGame(WindowContainer windowContainer) {
        return false;
    }

    default boolean stopRotationInPutt(WindowContainer windowContainer, int i) {
        return false;
    }

    default boolean stopUpdateRotation(DisplayContent displayContent, boolean z) {
        return false;
    }

    default boolean stopUpdateRotationUnchecked(DisplayContent displayContent, boolean z) {
        return false;
    }

    default void updateOrientationSensorRunningState(boolean z) {
    }

    default void updateRotation(int i, DisplayContent displayContent) {
    }
}
