package com.android.server.power;

import android.os.Handler;
import android.os.IBinder;
import android.os.IWakeLockCallback;
import android.os.WorkSource;
import android.service.dreams.DreamManagerInternal;
import android.util.SparseArray;
import com.android.server.lights.LightsManager;
import com.android.server.lights.LogicalLight;
import com.android.server.power.PowerManagerService;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IPowerManagerServiceWrapper {
    default void applyWakeLockFlagsOnAcquireLocked(PowerManagerService.WakeLock wakeLock, boolean z) {
    }

    default int findWakeLockIndexLocked(IBinder iBinder) {
        return 0;
    }

    default long getAttentiveTimeoutLocked() {
        return 0L;
    }

    default boolean getBootCompleted() {
        return false;
    }

    default int getDIRTY_USER_ACTIVITY() {
        return 0;
    }

    default int getDIRTY_WAKE_LOCKS() {
        return 0;
    }

    default int getDirty() {
        return 0;
    }

    default int getDozeScreenStateOverrideFromDreamManager() {
        return 0;
    }

    default int getDreamsBatteryLevelMinimumWhenNotPoweredConfig() {
        return 0;
    }

    default boolean getDreamsEnabledOnBatteryConfig() {
        return false;
    }

    default int getMSG_SCREEN_BRIGHTNESS_BOOST_TIMEOUT() {
        return 0;
    }

    default int getMSG_USER_ACTIVITY_TIMEOUT() {
        return 0;
    }

    default IPowerManagerServiceExt getPmsExt() {
        return null;
    }

    default boolean getProximityPositive() {
        return false;
    }

    default int getScreenBrightnessModeSetting() {
        return 0;
    }

    default float getScreenBrightnessOverrideFromWindowManager() {
        return 0.0f;
    }

    default float getScreenBrightnessSettingDefault() {
        return 0.0f;
    }

    default float getScreenBrightnessSettingMaximum() {
        return 0.0f;
    }

    default float getScreenBrightnessSettingMinimum() {
        return 0.0f;
    }

    default long getScreenOffTimeoutLocked(long j, long j2) {
        return 0L;
    }

    default long getScreenOffTimeoutSetting() {
        return 0L;
    }

    default long getSleepTimeoutLocked(long j) {
        return 0L;
    }

    default long getUserActivityTimeoutOverrideFromWindowManager() {
        return 0L;
    }

    default int getWakeLockSummary() {
        return 0;
    }

    default boolean isInteractiveInternal() {
        return false;
    }

    default void notifyWakeLockAcquiredLocked(PowerManagerService.WakeLock wakeLock) {
    }

    default void notifyWakeLockChangingLocked(PowerManagerService.WakeLock wakeLock, int i, String str, String str2, int i2, int i3, WorkSource workSource, String str3, IWakeLockCallback iWakeLockCallback) {
    }

    default void notifyWakeLockReleasedLocked(PowerManagerService.WakeLock wakeLock) {
    }

    default void releaseWakeLockInternal(IBinder iBinder, int i) {
    }

    default void removeWakeLockLocked(PowerManagerService.WakeLock wakeLock, int i) {
    }

    default void setDecoupleHalAutoSuspendModeFromDisplayConfig(boolean z) {
    }

    default void setDecoupleHalInteractiveModeFromDisplayConfig(boolean z) {
    }

    default void setDirty(int i) {
    }

    default void setDozeAfterScreenOff(boolean z) {
    }

    default void setDreamsActivateOnSleepSetting(boolean z) {
    }

    default void setDreamsBatteryLevelMinimumWhenNotPoweredConfig(int i) {
    }

    default void setDreamsEnabledOnBatteryConfig(boolean z) {
    }

    default void setProximityPositive(boolean z) {
    }

    default void setRequestWaitForNegativeProximity(boolean z) {
    }

    default void setScreenBrightnessOverrideFromWindowManager(float f) {
    }

    default void setScreenBrightnessSettingDefault(float f) {
    }

    default void setScreenBrightnessSettingMaximum(float f) {
    }

    default void setScreenBrightnessSettingMinimum(float f) {
    }

    default void setUserActivityTimeoutOverrideFromWindowManager(long j) {
    }

    default boolean setWakeLockDisabledStateLocked(PowerManagerService.WakeLock wakeLock) {
        return false;
    }

    default void updatePowerStateLocked() {
    }

    default void userActivityInternal(int i, long j, int i2, int i3, int i4) {
    }

    default boolean userActivityNoUpdateLocked(long j, int i, int i2, int i3) {
        return false;
    }

    default Object getLock() {
        return new Object();
    }

    default LightsManager getLightsManager() {
        return new LightsManager() { // from class: com.android.server.power.IPowerManagerServiceWrapper.1
            @Override // com.android.server.lights.LightsManager
            public LogicalLight getLight(int i) {
                return new LogicalLight() { // from class: com.android.server.power.IPowerManagerServiceWrapper.1.1
                    @Override // com.android.server.lights.LogicalLight
                    public void pulse() {
                    }

                    @Override // com.android.server.lights.LogicalLight
                    public void pulse(int i2, int i3) {
                    }

                    @Override // com.android.server.lights.LogicalLight
                    public void setBrightness(float f) {
                    }

                    @Override // com.android.server.lights.LogicalLight
                    public void setBrightness(float f, int i2) {
                    }

                    @Override // com.android.server.lights.LogicalLight
                    public void setColor(int i2) {
                    }

                    @Override // com.android.server.lights.LogicalLight
                    public void setFlashing(int i2, int i3, int i4, int i5) {
                    }

                    @Override // com.android.server.lights.LogicalLight
                    public void setVrMode(boolean z) {
                    }

                    @Override // com.android.server.lights.LogicalLight
                    public void turnOff() {
                    }
                };
            }
        };
    }

    default Handler getHandler() {
        return new Handler();
    }

    default ArrayList<PowerManagerService.WakeLock> getWakeLocks() {
        return new ArrayList<>();
    }

    default SparseArray<PowerManagerService.UidState> getUidState() {
        return new SparseArray<>();
    }

    default SparseArray<PowerGroup> getPowerGroups() {
        return new SparseArray<>();
    }

    default DreamManagerInternal getDreamManager() {
        return new DreamManagerInternal() { // from class: com.android.server.power.IPowerManagerServiceWrapper.2
            public boolean canStartDreaming(boolean z) {
                return false;
            }

            public boolean isDreaming() {
                return false;
            }

            public void registerDreamManagerStateListener(DreamManagerInternal.DreamManagerStateListener dreamManagerStateListener) {
            }

            public void requestDream() {
            }

            public void startDream(boolean z, String str) {
            }

            public void stopDream(boolean z, String str) {
            }

            public void unregisterDreamManagerStateListener(DreamManagerInternal.DreamManagerStateListener dreamManagerStateListener) {
            }
        };
    }
}
