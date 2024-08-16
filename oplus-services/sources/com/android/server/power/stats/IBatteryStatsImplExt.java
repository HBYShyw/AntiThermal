package com.android.server.power.stats;

import android.content.ComponentName;
import android.content.Context;
import android.os.BatteryStats;
import android.os.Handler;
import android.os.Message;
import android.telephony.ModemActivityInfo;
import android.util.AtomicFile;
import com.android.server.power.stats.BatteryStatsImpl;
import java.io.File;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IBatteryStatsImplExt {
    default String addDevicePowerStatsDeltaString(String str) {
        return str;
    }

    default void addThermalForegroundApp(long j, long j2, String str, int i, int i2) {
    }

    default void addThermalJobProc(long j, long j2, String str) {
    }

    default void addThermalNetState(long j, long j2, boolean z) {
    }

    default void addThermalOnOffEvent(int i, long j, long j2, boolean z) {
    }

    default void addThermalPhoneOnOff(long j, long j2, boolean z) {
    }

    default void addThermalPhoneSignal(long j, long j2, byte b) {
    }

    default void addThermalPhoneState(long j, long j2, byte b) {
    }

    default void addThermalScreenBrightnessEvent(long j, long j2, int i, int i2) {
    }

    default void addThermalWifiRssi(long j, long j2, int i) {
    }

    default void addThermalWifiStatus(long j, long j2, int i) {
    }

    default void addThermalnetSyncProc(long j, long j2, String str) {
    }

    default void backupThermalLogFile() {
    }

    default void backupThermalStatsFile() {
    }

    default void clearThermalAllHistory() {
    }

    default void collectCheckinFile(int i, AtomicFile atomicFile, BatteryStatsImpl.BatteryCallback batteryCallback) {
    }

    default void dumpThemalHeatDetailLocked(PrintWriter printWriter) {
    }

    default void dumpThemalLocked(PrintWriter printWriter, long j) {
    }

    default void dumpThemalRawLocked(PrintWriter printWriter, long j) {
    }

    default void dumpThemalRecLocked(Context context, PrintWriter printWriter, int i, int i2, long j) {
    }

    default void dumpThermalConfig(PrintWriter printWriter) {
    }

    default void getMonitorAppLocked(PrintWriter printWriter) {
    }

    default void getPhoneTemp(PrintWriter printWriter) {
    }

    default void initBatteryStatsImplExtImpl(BatteryStatsImpl batteryStatsImpl) {
    }

    default void initBatteryStatsImplExtImpl(BatteryStatsImpl batteryStatsImpl, File file, Handler handler) {
    }

    default boolean isOpDump() {
        return false;
    }

    default void logSwitch(boolean z) {
    }

    default void noteActivityLocked(int i, ComponentName componentName, boolean z, BatteryStats.HistoryItem historyItem, long j, Handler handler, String str, boolean z2, Context context) {
    }

    default void noteActivityPausedLocked(int i, ComponentName componentName, boolean z, BatteryStats.HistoryItem historyItem, long j, Handler handler) {
    }

    default void noteActivityResumedLocked(int i, ComponentName componentName, boolean z, BatteryStats.HistoryItem historyItem, long j, Handler handler, String str) {
    }

    default void noteConnectivityChangedLocked(int i, String str, long j, long j2) {
    }

    default void notePhoneDataConnectionStateLocked(long j, long j2, int i) {
    }

    default void noteScreenBrightnessModeChangedLock(boolean z) {
    }

    default boolean onBatteryStatsMessageHandle(Message message) {
        return false;
    }

    default void onSystemServicesReady(Context context) {
    }

    default void printChargeMapLocked(PrintWriter printWriter) {
    }

    default void printThermalHeatThreshold(PrintWriter printWriter) {
    }

    default void printThermalUploadTemp(PrintWriter printWriter) {
    }

    default void recordBluetoothPowerDrainMaMs(long j) {
    }

    default void recordGpsPowerDrainMaMs(long j) {
    }

    default void recordMobilePowerDrainMaMs(long j) {
    }

    default void recordNetworkActivityBytes(int i, long j) {
    }

    default void recordWifiPowerDrainMaMs(long j) {
    }

    default void setHeatBetweenTime(PrintWriter printWriter, int i) {
    }

    default void setInBatteryStatsImplInstance(BatteryStats batteryStats) {
    }

    default void setMonitorAppLimitTime(PrintWriter printWriter, int i) {
    }

    default void setOpDump(boolean z) {
    }

    default void setScreenBrightness(int i) {
    }

    default void setThermalConfig() {
    }

    default void setThermalCpuLoading(int i, int i2, int i3, int i4, int i5, String str, String str2) {
    }

    default void setThermalHeatThreshold(PrintWriter printWriter, int i) {
    }

    default void setThermalState(Object obj) {
    }

    default void toggleThermalDebugSwith(PrintWriter printWriter, int i) {
    }

    default void updateCpuStatsNow(PrintWriter printWriter) {
    }

    default void updateMobileRadioState(ModemActivityInfo modemActivityInfo) {
    }
}
