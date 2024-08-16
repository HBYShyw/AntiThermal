package com.android.server;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;
import android.content.Intent;
import android.util.Slog;
import com.android.server.DeviceIdleController;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IOplusDeviceIdleHelper extends IOplusCommonFeature {
    public static final IOplusDeviceIdleHelper DEFAULT = new IOplusDeviceIdleHelper() { // from class: com.android.server.IOplusDeviceIdleHelper.1
    };
    public static final String NAME = "IOplusDeviceIdleHelper";

    default void addInvalidDozeWhitelist(List<String> list) {
    }

    default void addPowerSaveWhitelist(String str) {
    }

    default void addPowerSaveWhitelistAllFromSystemConfig() {
    }

    default void addPowerSaveWhitelistExceptIdle(String str) {
    }

    default void dump(PrintWriter printWriter) {
    }

    default void enterDeepSleepQuickly() {
    }

    default boolean getGoogleRestrictSwitch() {
        return false;
    }

    default boolean getOpenDeviceIdleSwitch() {
        return false;
    }

    default long getTotalIntervalToIdle() {
        return 1800000L;
    }

    default boolean isAutoPowerModesEnabled() {
        return true;
    }

    default boolean isDeepInTraffic() {
        return false;
    }

    default boolean isInited() {
        return false;
    }

    default boolean isLightInTraffic() {
        return false;
    }

    default void motionDetected(int i, int i2) {
    }

    default void onDeepIdleOn(ArrayList<String> arrayList) {
    }

    default void onIdleExit() {
    }

    default void onLightIdleOn(ArrayList<String> arrayList) {
    }

    default void onMotionDetected(int i, int i2) {
    }

    default boolean onScreenOff() {
        return false;
    }

    default void onScreenOn() {
    }

    default void removeInvalidDozeWhitelist(String str) {
    }

    default void removePackage(Intent intent) {
    }

    default void setDebugSwitch(boolean z) {
    }

    default void updateLastDeepTrafficRecord() {
    }

    default void updateLastLightTrafficRecord() {
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusDeviceIdleHelper;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void initArgs(Context context, DeviceIdleController deviceIdleController, DeviceIdleController.Constants constants) {
        Slog.d(NAME, "interface do initArgs");
    }

    default List<String> getInValidDozeWhitelist() {
        return new ArrayList();
    }
}
