package com.android.server.input;

import android.content.Context;
import android.graphics.RectF;
import android.hardware.display.DisplayViewport;
import android.os.Bundle;
import android.os.Handler;
import android.view.InputDevice;
import android.view.InputEvent;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IInputManagerServiceExt {

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface InputManagerNativeCallback {
        default void onNativeDynamicallyConfigLog(int i) {
        }

        default void onNativeDynamicallyConfigLog(boolean z, boolean z2) {
        }

        String requestAdjustNativeDump();
    }

    default void debugInputKeyInject(int i, InputEvent inputEvent, String str) {
    }

    default boolean dynamicallyAdjustDump(PrintWriter printWriter, String[] strArr) {
        return false;
    }

    default int hasTouchedWindow(int i) {
        return 0;
    }

    default void init(Context context, Handler handler, long j) {
    }

    default void inputCancelFromNative(int i) {
    }

    default boolean isNeedIntermittentIntercept(InputEvent inputEvent) {
        return false;
    }

    default boolean isOfficialKeyboard(InputDevice inputDevice) {
        return false;
    }

    default boolean isOplusTrustedApp(String str, int i, String str2) {
        return false;
    }

    default void notifyGestureMonitorUnresponsive(int i, String str) {
    }

    default void notifyImeWindowStateChanged(boolean z) {
    }

    default void notifyInputDispatcherThread(int i) {
    }

    default void notifyInputJitter(String str) {
    }

    default void notifyInputReaderThread(int i) {
    }

    default void notifyRefreshRateChanged(int i, float f) {
    }

    default void onSystemRunning() {
    }

    default void removePackageFromUntrustedRecord(String str, String str2) {
    }

    default void setAccessibilityStatus(boolean z) {
    }

    default void setDisplayViewportsInternal(List<DisplayViewport> list) {
    }

    default boolean setJoyStickConfig(int i, String str) {
        return false;
    }

    default boolean setJoyStickStatus(int i) {
        return false;
    }

    default boolean setJoyStickSwitch(int i) {
        return false;
    }

    default void showTipsDialog(String str, Context context) {
    }

    default void showTouchPadNotification(InputDevice[] inputDeviceArr) {
    }

    default void start() {
    }

    default boolean updateInvalidRegion(String str, List<RectF> list, boolean z, boolean z2, Bundle bundle) {
        return false;
    }

    default void updateUntrustedTouchConfig(String str, boolean z) {
    }

    default HashSet<String> getTrustedPackages() {
        return new HashSet<>();
    }

    default ArrayList<Integer> getTrustedWindowType() {
        return new ArrayList<>();
    }
}
