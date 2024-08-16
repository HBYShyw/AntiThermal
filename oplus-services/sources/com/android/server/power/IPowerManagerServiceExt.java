package com.android.server.power;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.IBinder;
import android.os.IWakeLockCallback;
import android.os.Looper;
import android.os.Message;
import android.os.WorkSource;
import android.service.dreams.DreamManagerInternal;
import com.android.server.power.PowerManagerService;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IPowerManagerServiceExt {
    default boolean acquireBaseProxyWakeLock(IBinder iBinder, int i, int i2, String str, String str2, WorkSource workSource, String str3, int i3, int i4) {
        return false;
    }

    default boolean acquireBaseProxyWakeLock(IBinder iBinder, int i, int i2, String str, String str2, WorkSource workSource, String str3, int i3, int i4, IWakeLockCallback iWakeLockCallback) {
        return false;
    }

    default boolean acquireBaseProxyWakeLock(IBinder iBinder, int i, int i2, String str, String str2, WorkSource workSource, String str3, int i3, int i4, IWakeLockCallback iWakeLockCallback, boolean z) {
        return false;
    }

    default void acquireSuspendBlockerEnd(String str) {
    }

    default void acquireSuspendBlockerStart() {
    }

    default void cancelCheck(String str) {
    }

    default void disableScreenStayAwakeOfApp(boolean z, int i) {
    }

    default boolean dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        return false;
    }

    default void dumpBaseProxyWakeLock(PrintWriter printWriter) {
    }

    default void dumpSmartLauncher(PrintWriter printWriter) {
    }

    default boolean getCastMode() {
        return false;
    }

    default Looper getCustomPowerManagerLooper() {
        return null;
    }

    default boolean getIgnoreBright(PowerManagerService.WakeLock wakeLock) {
        return false;
    }

    default boolean getOnDozeSwitch() {
        return false;
    }

    default boolean getProximityLockFromInCallUiValueLocked() {
        return false;
    }

    default int getScreenBrightnessSetting() {
        return 0;
    }

    default long getScreenDimDurationLocked(long j, boolean z) {
        return -1L;
    }

    default long getScreenOffTimeoutLocked(long j) {
        return j;
    }

    default void getWakeLockSummaryFlags(PowerManagerService.WakeLock wakeLock) {
    }

    default void handleAodChanged() {
    }

    default void handleBaseWakeLockDeath(PowerManagerService.WakeLock wakeLock) {
    }

    default int handleWakeUpReasonEarly(int i, String str) {
        return i;
    }

    default String handleWakeUpdetailsEarly(String str, int i, String str2, int i2) {
        return str;
    }

    default void init() {
    }

    default void init(Context context) {
    }

    default boolean interceptAcquireWakeLockInternal(IBinder iBinder, String str, int i, WorkSource workSource, int i2, String str2) {
        return false;
    }

    default boolean interceptSetLowPowerModeInternalIsPowered() {
        return false;
    }

    default boolean interceptShutdownOrRebootInternal(String str) {
        return false;
    }

    default boolean interceptSleepDisplayGroupNoUpdateLocked(int i, long j, int i2, int i3, int i4) {
        return false;
    }

    default boolean interceptWakeDisplayGroupNoUpdateLocked(int i, long j, int i2, String str, int i3, String str2, int i4, boolean z) {
        return false;
    }

    default boolean isBeingKeptAwakeLocked(int i, boolean z) {
        return z;
    }

    default boolean isBlockedByBiometrics() {
        return false;
    }

    default boolean isCustomPowerSaveModeDisabled() {
        return false;
    }

    default boolean isFingerprintOpticalSupport() {
        return false;
    }

    default boolean isShouldGoAod() {
        return false;
    }

    default boolean isSideFingerprintSupport() {
        return false;
    }

    default boolean isValidBrightness(int i) {
        return false;
    }

    default boolean isWakelockNeedIgnoreOnAfterRelease(PowerManagerService.WakeLock wakeLock) {
        return false;
    }

    default boolean islowLevelRebootValidReason(String str) {
        return true;
    }

    default boolean notAllowedSetUserActivityTimeoutOverrideFromWindowManager(long j, boolean z, int i) {
        return false;
    }

    default void notePowerkeyProcessEvent(String str, boolean z, boolean z2) {
    }

    default void notePowerkeyProcessStagePoint(String str) {
    }

    default void noteWakeLockChange(PowerManagerService.WakeLock wakeLock, boolean z) {
    }

    default void noteWorkSourceChange(PowerManagerService.WakeLock wakeLock, WorkSource workSource) {
    }

    default void onAcquireNewWakeLockInternal(PowerManagerService.WakeLock wakeLock, IBinder iBinder, int i, String str, String str2, WorkSource workSource, String str3, int i2, int i3) {
    }

    default void onAodsystemReady() {
    }

    default boolean onApplyWakeLockFlagsOnAcquireLocked(PowerManagerService.WakeLock wakeLock, int i) {
        return false;
    }

    default void onBootComplete() {
    }

    default void onDeviceIdle() {
    }

    default void onDisplayStateChange(DreamManagerInternal dreamManagerInternal, int i) {
    }

    default void onDisplayStateChange(boolean z) {
    }

    default void onPowerManagerHandlerHandleMessage(Message message) {
    }

    default void onReadConfigurationLocked() {
    }

    default void onRemoveWakeLockLocked(int i, PowerManagerService.WakeLock wakeLock) {
    }

    default void onSleepDisplayGroupNoUpdateLockedEnd(int i, int i2) {
    }

    default void onStart() {
    }

    default void onUserActivityNoUpdateLocked(boolean z, int i) {
    }

    default void onUserActivityNoUpdateLocked(boolean z, int i, int i2) {
    }

    default void onWakefulnessChangeFinished(int i) {
    }

    default void printStackTraceInfo() {
    }

    default void registerOtherContentObserver(ContentResolver contentResolver, ContentObserver contentObserver) {
    }

    default void releaseBaseProxyedWakeLockInternalLocked(IBinder iBinder) {
    }

    default void releaseSuspendBlocker(String str) {
    }

    default void releaseWakeLockInternalLocked(PowerManagerService.WakeLock wakeLock, int i) {
    }

    default void screenOnWakelockCheck(int i, boolean z, boolean z2) {
    }

    default void setAodSettingStatus() {
    }

    default void setDeviceState(int i) {
    }

    default void setDozeOverrideFromDreamManager(int i, int i2) {
    }

    default int setDozeOverrideFromDreamManagerInternal(int i, int i2) {
        return i;
    }

    default void setGlobalWakefulnessLocked(int i, int i2, long j, int i3, String str) {
    }

    default void setLowPowerModeInternalEnd(boolean z) {
    }

    default void setScreenBrightnessOverrideFromWindowManager(float f) {
    }

    default void setScreenOffPositive(String str) {
    }

    default boolean shouldCommitScreenBrightnessOverrideMap() {
        return false;
    }

    default void stopDreamByMessage(DreamManagerInternal dreamManagerInternal) {
    }

    default void systemReady() {
    }

    default void systemReady(SuspendBlocker suspendBlocker) {
    }

    default float updateAutoBrightness(float f) {
        return f;
    }

    default void updateDisplayPowerStateLocked(int i, int i2, int i3, int i4) {
    }

    default void updateDisplayPowerStateLockedStart() {
    }

    default void updateProxyedWakeLockWorkSource(IBinder iBinder, WorkSource workSource, String str) {
    }

    default boolean updateProxyedWakeLockWorkSource(IBinder iBinder, WorkSource workSource, String str, PowerManagerService.WakeLock wakeLock) {
        return false;
    }

    default void updateSettingsLocked(ContentResolver contentResolver) {
    }

    default void updateWakeLockSummaryLockedStart() {
    }

    default void uploadAttentionChangeTimeout(long j) {
    }

    default void userActivity(int i, long j, int i2, int i3, int i4) {
    }

    default void userActivityNoUpdateChangeLightsLocked() {
    }

    default void wakeDisplayGroupNoUpdateLockedEnd(int i, String str) {
    }

    default void wakeDisplayGroupNoUpdateLockedStart(int i, long j, String str, int i2, String str2, int i3) {
    }

    default void stopDream(DreamManagerInternal dreamManagerInternal) {
        dreamManagerInternal.stopDream(false, "unknownReason");
    }
}
