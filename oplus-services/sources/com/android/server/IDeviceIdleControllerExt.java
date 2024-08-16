package com.android.server;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.ShellCommand;
import android.os.SystemProperties;
import android.util.ArrayMap;
import com.android.server.DeviceIdleController;
import java.io.PrintWriter;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IDeviceIdleControllerExt {
    public static final long ADVANCE_TIME = 10000;
    public static final int ANY_MOTION = 2;
    public static final boolean DEBUG_OPLUS = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    public static final long DEFAULT_TOTAL_INTERVAL_TO_IDLE = 1800000;
    public static final long DURATION_TRAFFIC_CHECK = 300000;
    public static final long QUICK_ENTER_DEEPSLEEP_DRUATION = 180000;
    public static final int SIGNIFICANT_MOTION = 1;
    public static final boolean mDeepIdleAccordingToRus = true;
    public static final boolean mLightIdleAccordingToRus = true;

    default void addInvalidDozeWhitelist(List<String> list) {
    }

    default void dump(PrintWriter printWriter) {
    }

    default void enterSmartDozeIfNeeded(String str) {
    }

    default void hookonBootPhase(int i, Context context, DeviceIdleController deviceIdleController, DeviceIdleController.Constants constants) {
    }

    default void init(Context context) {
    }

    default void initArgs(DeviceIdleController.Constants constants, Context context, Handler handler, DeviceIdleController deviceIdleController) {
    }

    default void initCustomizeDozeModeState() {
    }

    default boolean isClosedSuperFirewall() {
        return false;
    }

    default boolean isCustomizeDozeModeDisabled() {
        return false;
    }

    default boolean isInSmartDozeMode(int i) {
        return false;
    }

    default void onBroadcastIdleState() {
    }

    default void onIdleExit() {
    }

    default void onIdleOn(ArrayMap<String, Integer> arrayMap, boolean z) {
    }

    default void onShellCommand(ShellCommand shellCommand) {
    }

    default void removePackage(Intent intent) {
    }
}
