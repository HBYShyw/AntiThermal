package com.android.server.power;

import android.hardware.display.DisplayManagerInternal;
import android.os.PowerManager;
import android.os.PowerManagerInternal;
import android.os.PowerSaveState;
import android.os.Trace;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.LatencyTracker;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PowerGroup {
    private static final boolean DEBUG = false;
    private static final String TAG = "PowerGroup";
    private final DisplayManagerInternal mDisplayManagerInternal;

    @VisibleForTesting
    final DisplayManagerInternal.DisplayPowerRequest mDisplayPowerRequest;
    private final int mGroupId;
    private boolean mIsSandmanSummoned;
    private long mLastPowerOnTime;
    private long mLastSleepTime;
    private int mLastUserActivityEvent;
    private long mLastUserActivityTime;
    private long mLastUserActivityTimeNoChangeLights;
    private long mLastWakeTime;
    private final Notifier mNotifier;
    private boolean mPoweringOn;
    private boolean mReady;
    private final boolean mSupportsSandman;
    private int mUserActivitySummary;
    private int mWakeLockSummary;
    private int mWakefulness;
    private final PowerGroupListener mWakefulnessListener;

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface PowerGroupListener {
        void onWakefulnessChangedLocked(int i, int i2, long j, int i3, int i4, int i5, String str, String str2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PowerGroup(int i, PowerGroupListener powerGroupListener, Notifier notifier, DisplayManagerInternal displayManagerInternal, int i2, boolean z, boolean z2, long j) {
        this.mDisplayPowerRequest = new DisplayManagerInternal.DisplayPowerRequest();
        this.mGroupId = i;
        this.mWakefulnessListener = powerGroupListener;
        this.mNotifier = notifier;
        this.mDisplayManagerInternal = displayManagerInternal;
        this.mWakefulness = i2;
        this.mReady = z;
        this.mSupportsSandman = z2;
        this.mLastWakeTime = j;
        this.mLastSleepTime = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PowerGroup(int i, PowerGroupListener powerGroupListener, Notifier notifier, DisplayManagerInternal displayManagerInternal, long j) {
        this.mDisplayPowerRequest = new DisplayManagerInternal.DisplayPowerRequest();
        this.mGroupId = 0;
        this.mWakefulnessListener = powerGroupListener;
        this.mNotifier = notifier;
        this.mDisplayManagerInternal = displayManagerInternal;
        this.mWakefulness = i;
        this.mReady = false;
        this.mSupportsSandman = true;
        this.mLastWakeTime = j;
        this.mLastSleepTime = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getLastWakeTimeLocked() {
        return this.mLastWakeTime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getLastSleepTimeLocked() {
        return this.mLastSleepTime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getWakefulnessLocked() {
        return this.mWakefulness;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getGroupId() {
        return this.mGroupId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setWakefulnessLocked(int i, long j, int i2, int i3, int i4, String str, String str2) {
        int i5 = this.mWakefulness;
        if (i5 == i) {
            return false;
        }
        if (i == 1) {
            setLastPowerOnTimeLocked(j);
            setIsPoweringOnLocked(true);
            this.mLastWakeTime = j;
        } else if (PowerManagerInternal.isInteractive(i5) && !PowerManagerInternal.isInteractive(i)) {
            this.mLastSleepTime = j;
        }
        this.mWakefulness = i;
        this.mWakefulnessListener.onWakefulnessChangedLocked(this.mGroupId, i, j, i3, i2, i4, str, str2);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isReadyLocked() {
        return this.mReady;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setReadyLocked(boolean z) {
        if (this.mReady == z) {
            return false;
        }
        this.mReady = z;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getLastPowerOnTimeLocked() {
        return this.mLastPowerOnTime;
    }

    void setLastPowerOnTimeLocked(long j) {
        this.mLastPowerOnTime = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isPoweringOnLocked() {
        return this.mPoweringOn;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setIsPoweringOnLocked(boolean z) {
        this.mPoweringOn = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isSandmanSummonedLocked() {
        return this.mIsSandmanSummoned;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSandmanSummonedLocked(boolean z) {
        this.mIsSandmanSummoned = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void wakeUpLocked(long j, int i, String str, int i2, String str2, int i3, LatencyTracker latencyTracker) {
        if (j < this.mLastSleepTime || this.mWakefulness == 1) {
            return;
        }
        Trace.traceBegin(131072L, "wakePowerGroup" + this.mGroupId);
        try {
            Slog.i(TAG, "Waking up power group from " + PowerManagerInternal.wakefulnessToString(this.mWakefulness) + " (groupId=" + this.mGroupId + ", uid=" + i2 + ", reason=" + PowerManager.wakeReasonToString(i) + ", details=" + str + ")...");
            Trace.asyncTraceBegin(131072L, "Screen turning on", this.mGroupId);
            latencyTracker.onActionStart(5, String.valueOf(this.mGroupId));
            setWakefulnessLocked(1, j, i2, i, i3, str2, str);
        } finally {
            Trace.traceEnd(131072L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean dreamLocked(long j, int i, boolean z) {
        if (j < this.mLastWakeTime) {
            return false;
        }
        if (!z && this.mWakefulness != 1) {
            return false;
        }
        Trace.traceBegin(131072L, "dreamPowerGroup" + getGroupId());
        try {
            Slog.i(TAG, "Napping power group (groupId=" + getGroupId() + ", uid=" + i + ")...");
            setSandmanSummonedLocked(true);
            setWakefulnessLocked(2, j, i, 0, 0, null, null);
            return true;
        } finally {
            Trace.traceEnd(131072L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean dozeLocked(long j, int i, int i2) {
        if (j < getLastWakeTimeLocked() || !PowerManagerInternal.isInteractive(this.mWakefulness)) {
            PowerManagerService.mPmsExt.notePowerkeyProcessStagePoint("CANCELED_dozeLocked");
            return false;
        }
        Trace.traceBegin(131072L, "powerOffDisplay");
        try {
            int min = Math.min(16, Math.max(i2, 0));
            long max = j - Math.max(this.mLastUserActivityTimeNoChangeLights, this.mLastUserActivityTime);
            Slog.i(TAG, "Powering off display group due to " + PowerManager.sleepReasonToString(min) + " (groupId= " + getGroupId() + ", uid= " + i + ", millisSinceLastUserActivity=" + max + ", lastUserActivityEvent=" + PowerManager.userActivityEventToString(this.mLastUserActivityEvent) + ")...");
            setSandmanSummonedLocked(true);
            setWakefulnessLocked(3, j, i, min, 0, null, null);
            return true;
        } finally {
            Trace.traceEnd(131072L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean sleepLocked(long j, int i, int i2) {
        if (j < this.mLastWakeTime || getWakefulnessLocked() == 0) {
            PowerManagerService.mPmsExt.notePowerkeyProcessStagePoint("CANCELED_sleepLocked");
            return false;
        }
        Trace.traceBegin(131072L, "sleepPowerGroup");
        try {
            Slog.i(TAG, "Sleeping power group (groupId=" + getGroupId() + ", uid=" + i + ", reason=" + PowerManager.sleepReasonToString(i2) + ")...");
            setSandmanSummonedLocked(true);
            setWakefulnessLocked(0, j, i, i2, 0, null, null);
            return true;
        } finally {
            Trace.traceEnd(131072L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getLastUserActivityTimeLocked() {
        return this.mLastUserActivityTime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLastUserActivityTimeLocked(long j, int i) {
        this.mLastUserActivityTime = j;
        this.mLastUserActivityEvent = i;
    }

    public long getLastUserActivityTimeNoChangeLightsLocked() {
        return this.mLastUserActivityTimeNoChangeLights;
    }

    public void setLastUserActivityTimeNoChangeLightsLocked(long j, int i) {
        this.mLastUserActivityTimeNoChangeLights = j;
        this.mLastUserActivityEvent = i;
    }

    public int getUserActivitySummaryLocked() {
        return this.mUserActivitySummary;
    }

    public boolean isPolicyBrightLocked() {
        return this.mDisplayPowerRequest.policy == 3;
    }

    public boolean isPolicyDimLocked() {
        return this.mDisplayPowerRequest.policy == 2;
    }

    public boolean isBrightOrDimLocked() {
        return this.mDisplayPowerRequest.isBrightOrDim();
    }

    public void setUserActivitySummaryLocked(int i) {
        this.mUserActivitySummary = i;
    }

    public int getWakeLockSummaryLocked() {
        return this.mWakeLockSummary;
    }

    public boolean hasWakeLockKeepingScreenOnLocked() {
        return (this.mWakeLockSummary & 38) != 0;
    }

    public void setWakeLockSummaryLocked(int i) {
        this.mWakeLockSummary = i;
    }

    public boolean supportsSandmanLocked() {
        return this.mSupportsSandman;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean needSuspendBlockerLocked(boolean z, boolean z2) {
        if (isBrightOrDimLocked() && (!this.mDisplayPowerRequest.useProximitySensor || !z || !z2)) {
            return true;
        }
        DisplayManagerInternal.DisplayPowerRequest displayPowerRequest = this.mDisplayPowerRequest;
        return displayPowerRequest.policy == 1 && displayPowerRequest.dozeScreenState == 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public int getDesiredScreenPolicyLocked(boolean z, boolean z2, boolean z3, boolean z4) {
        int wakefulnessLocked = getWakefulnessLocked();
        int wakeLockSummaryLocked = getWakeLockSummaryLocked();
        if (wakefulnessLocked == 0 || z) {
            return 0;
        }
        if (wakefulnessLocked == 3) {
            if ((wakeLockSummaryLocked & 64) != 0) {
                return 1;
            }
            if (z2) {
                return 0;
            }
            if (PowerManagerService.mPmsExt.getOnDozeSwitch()) {
                Slog.d(TAG, "on to doze open, dozing policy doze");
                return 1;
            }
        }
        return ((wakeLockSummaryLocked & 2) == 0 && z3 && (getUserActivitySummaryLocked() & 1) == 0 && !z4 && !PowerManagerService.mPmsExt.getOnDozeSwitch()) ? 2 : 3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getPolicyLocked() {
        return this.mDisplayPowerRequest.policy;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateLocked(float f, boolean z, boolean z2, int i, float f2, boolean z3, PowerSaveState powerSaveState, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8) {
        this.mDisplayPowerRequest.policy = getDesiredScreenPolicyLocked(z4, z5, z6, z7);
        DisplayManagerInternal.DisplayPowerRequest displayPowerRequest = this.mDisplayPowerRequest;
        displayPowerRequest.screenBrightnessOverride = f;
        displayPowerRequest.useProximitySensor = z;
        displayPowerRequest.boostScreenBrightness = z2;
        if (displayPowerRequest.policy == 1) {
            displayPowerRequest.dozeScreenState = i;
            if ((getWakeLockSummaryLocked() & 64) == 0 && PowerManagerService.mPmsExt.getOnDozeSwitch()) {
                Slog.d(TAG, "on to doze open, policy doze without doze wakelock");
                this.mDisplayPowerRequest.dozeScreenState = 0;
            }
            if ((getWakeLockSummaryLocked() & 128) != 0 && !z3) {
                DisplayManagerInternal.DisplayPowerRequest displayPowerRequest2 = this.mDisplayPowerRequest;
                if (displayPowerRequest2.dozeScreenState == 4) {
                    displayPowerRequest2.dozeScreenState = 3;
                }
                if (displayPowerRequest2.dozeScreenState == 6) {
                    displayPowerRequest2.dozeScreenState = 2;
                }
            }
            this.mDisplayPowerRequest.dozeScreenBrightness = f2;
        } else {
            displayPowerRequest.dozeScreenState = 0;
            displayPowerRequest.dozeScreenBrightness = Float.NaN;
        }
        DisplayManagerInternal.DisplayPowerRequest displayPowerRequest3 = this.mDisplayPowerRequest;
        displayPowerRequest3.lowPowerMode = powerSaveState.batterySaverEnabled;
        displayPowerRequest3.screenLowPowerBrightnessFactor = powerSaveState.brightnessFactor;
        boolean requestPowerState = this.mDisplayManagerInternal.requestPowerState(this.mGroupId, displayPowerRequest3, z8);
        this.mNotifier.onScreenPolicyUpdate(this.mGroupId, this.mDisplayPowerRequest.policy);
        return requestPowerState;
    }
}
