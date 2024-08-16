package com.android.server;

import android.content.Context;
import android.content.Intent;
import android.hardware.health.HealthInfo;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IBatteryServiceExt {
    public static final int PSY_BATTERY_HMAC = 2;
    public static final int PSY_FAST_CHARGE_TYPE = 3;
    public static final int PSY_OTG_ONLINE = 1;

    default void appendExtraToBatteryStatusChangedIntend(Intent intent) {
    }

    default void appendFlagToStatusIntent(Intent intent, int i) {
    }

    default boolean dumpInternalBase(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        return false;
    }

    default String getBatteryStatusStrForDebug() {
        return null;
    }

    default boolean getDebugCommand() {
        return false;
    }

    default boolean getDebugPanic() {
        return false;
    }

    default void handleScreenState(boolean z) {
    }

    default boolean ignoreShutdownIfOverTempByOplusLocked() {
        return false;
    }

    default void initBatteryServiceExtImpl(Context context, BatteryService batteryService, Object obj) {
    }

    default boolean isNeedSkipBatteryChangedBroadcast(HealthInfo healthInfo, int i, int i2, boolean z) {
        return false;
    }

    default void notifyTempChanged() {
    }

    default void onBootPhase(int i) {
    }

    default void onPlugChangedForOplusSysStateManager(int i) {
    }

    default void onStart() {
    }

    default void processValuesForOplusLocked(boolean z, int i, HealthInfo healthInfo) {
    }

    default void saveLastStatsAfterValuesChanged() {
    }

    default void setBatterySaverTest(boolean z, int i, boolean z2) {
    }

    default void setDebugCommand(boolean z) {
    }

    default void setDebugSwitchState(boolean z) {
    }

    default boolean shouldUpdateChargingState(int i, int i2) {
        return false;
    }

    default void updateBatteryService() {
    }

    default void writeEventLowBatteryPowerOff() {
    }
}
