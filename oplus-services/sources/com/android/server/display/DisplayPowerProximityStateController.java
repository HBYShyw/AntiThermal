package com.android.server.display;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.display.DisplayManagerInternal;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Slog;
import android.util.TimeUtils;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.display.DisplayPowerProximityStateController;
import com.android.server.display.utils.SensorUtils;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class DisplayPowerProximityStateController {
    private static final boolean DEBUG_PRETEND_PROXIMITY_SENSOR_ABSENT = false;
    private static final int MSG_IGNORE_PROXIMITY = 2;

    @VisibleForTesting
    static final int MSG_PROXIMITY_SENSOR_DEBOUNCED = 1;
    private static final int PROXIMITY_NEGATIVE = 0;

    @VisibleForTesting
    static final int PROXIMITY_POSITIVE = 1;
    private static final int PROXIMITY_SENSOR_NEGATIVE_DEBOUNCE_DELAY = 250;

    @VisibleForTesting
    static final int PROXIMITY_SENSOR_POSITIVE_DEBOUNCE_DELAY = 0;

    @VisibleForTesting
    static final int PROXIMITY_UNKNOWN = -1;
    private static final float TYPICAL_PROXIMITY_THRESHOLD = 5.0f;
    private Clock mClock;
    private DisplayDeviceConfig mDisplayDeviceConfig;
    private int mDisplayId;
    private final DisplayPowerProximityStateHandler mHandler;
    private boolean mIgnoreProximityUntilChanged;
    private final Runnable mNudgeUpdatePowerState;

    @GuardedBy({"mLock"})
    private boolean mPendingWaitForNegativeProximityLocked;
    private Sensor mProximitySensor;
    private boolean mProximitySensorEnabled;
    private float mProximityThreshold;
    private boolean mScreenOffBecauseOfProximity;
    private final SensorManager mSensorManager;
    private final String mTag;
    private boolean mWaitingForNegativeProximity;
    private final WakelockController mWakelockController;
    private final Object mLock = new Object();
    private final SensorEventListener mProximitySensorListener = new SensorEventListener() { // from class: com.android.server.display.DisplayPowerProximityStateController.1
        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (DisplayPowerProximityStateController.this.mProximitySensorEnabled) {
                long uptimeMillis = DisplayPowerProximityStateController.this.mClock.uptimeMillis();
                boolean z = false;
                float f = sensorEvent.values[0];
                if (f >= 0.0f && f < DisplayPowerProximityStateController.this.mProximityThreshold) {
                    z = true;
                }
                DisplayPowerProximityStateController.this.handleProximitySensorEvent(uptimeMillis, z);
            }
        }
    };
    private int mPendingProximity = -1;
    private long mPendingProximityDebounceTime = -1;
    private int mProximity = -1;
    private boolean mSkipRampBecauseOfProximityChangeToNegative = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface Clock {
        long uptimeMillis();
    }

    public DisplayPowerProximityStateController(WakelockController wakelockController, DisplayDeviceConfig displayDeviceConfig, Looper looper, Runnable runnable, int i, SensorManager sensorManager, Injector injector) {
        this.mClock = (injector == null ? new Injector() : injector).createClock();
        this.mWakelockController = wakelockController;
        this.mHandler = new DisplayPowerProximityStateHandler(looper);
        this.mNudgeUpdatePowerState = runnable;
        this.mDisplayDeviceConfig = displayDeviceConfig;
        this.mDisplayId = i;
        this.mTag = "DisplayPowerProximityStateController[" + this.mDisplayId + "]";
        this.mSensorManager = sensorManager;
        loadProximitySensor();
    }

    public void updatePendingProximityRequestsLocked() {
        synchronized (this.mLock) {
            this.mWaitingForNegativeProximity |= this.mPendingWaitForNegativeProximityLocked;
            this.mPendingWaitForNegativeProximityLocked = false;
            if (this.mIgnoreProximityUntilChanged) {
                this.mWaitingForNegativeProximity = false;
            }
        }
    }

    public void cleanup() {
        setProximitySensorEnabled(false);
    }

    public boolean isProximitySensorAvailable() {
        return this.mProximitySensor != null;
    }

    public boolean setPendingWaitForNegativeProximityLocked(boolean z) {
        synchronized (this.mLock) {
            if (z) {
                if (!this.mPendingWaitForNegativeProximityLocked) {
                    this.mPendingWaitForNegativeProximityLocked = true;
                    return true;
                }
            }
            return false;
        }
    }

    public void updateProximityState(DisplayManagerInternal.DisplayPowerRequest displayPowerRequest, int i) {
        this.mSkipRampBecauseOfProximityChangeToNegative = false;
        if (this.mProximitySensor != null) {
            if (displayPowerRequest.useProximitySensor && i != 1) {
                setProximitySensorEnabled(true);
                if (!this.mScreenOffBecauseOfProximity && this.mProximity == 1 && !this.mIgnoreProximityUntilChanged) {
                    this.mScreenOffBecauseOfProximity = true;
                    sendOnProximityPositiveWithWakelock();
                }
            } else if (this.mWaitingForNegativeProximity && this.mScreenOffBecauseOfProximity && this.mProximity == 1 && i != 1) {
                setProximitySensorEnabled(true);
            } else {
                setProximitySensorEnabled(false);
                this.mWaitingForNegativeProximity = false;
            }
            if (this.mScreenOffBecauseOfProximity) {
                if (this.mProximity != 1 || this.mIgnoreProximityUntilChanged) {
                    this.mScreenOffBecauseOfProximity = false;
                    this.mSkipRampBecauseOfProximityChangeToNegative = true;
                    sendOnProximityNegativeWithWakelock();
                    return;
                }
                return;
            }
            return;
        }
        setProximitySensorEnabled(false);
        this.mWaitingForNegativeProximity = false;
        this.mIgnoreProximityUntilChanged = false;
        if (this.mScreenOffBecauseOfProximity) {
            this.mScreenOffBecauseOfProximity = false;
            this.mSkipRampBecauseOfProximityChangeToNegative = true;
            sendOnProximityNegativeWithWakelock();
        }
    }

    public boolean shouldSkipRampBecauseOfProximityChangeToNegative() {
        return this.mSkipRampBecauseOfProximityChangeToNegative;
    }

    public boolean isScreenOffBecauseOfProximity() {
        return this.mScreenOffBecauseOfProximity;
    }

    public void ignoreProximitySensorUntilChanged() {
        this.mHandler.sendEmptyMessage(2);
    }

    public void notifyDisplayDeviceChanged(DisplayDeviceConfig displayDeviceConfig) {
        this.mDisplayDeviceConfig = displayDeviceConfig;
        loadProximitySensor();
    }

    public void dumpLocal(PrintWriter printWriter) {
        printWriter.println();
        printWriter.println("DisplayPowerProximityStateController:");
        synchronized (this.mLock) {
            printWriter.println("  mPendingWaitForNegativeProximityLocked=" + this.mPendingWaitForNegativeProximityLocked);
        }
        printWriter.println("  mDisplayId=" + this.mDisplayId);
        printWriter.println("  mWaitingForNegativeProximity=" + this.mWaitingForNegativeProximity);
        printWriter.println("  mIgnoreProximityUntilChanged=" + this.mIgnoreProximityUntilChanged);
        printWriter.println("  mProximitySensor=" + this.mProximitySensor);
        printWriter.println("  mProximitySensorEnabled=" + this.mProximitySensorEnabled);
        printWriter.println("  mProximityThreshold=" + this.mProximityThreshold);
        printWriter.println("  mProximity=" + proximityToString(this.mProximity));
        printWriter.println("  mPendingProximity=" + proximityToString(this.mPendingProximity));
        printWriter.println("  mPendingProximityDebounceTime=" + TimeUtils.formatUptime(this.mPendingProximityDebounceTime));
        printWriter.println("  mScreenOffBecauseOfProximity=" + this.mScreenOffBecauseOfProximity);
        printWriter.println("  mSkipRampBecauseOfProximityChangeToNegative=" + this.mSkipRampBecauseOfProximityChangeToNegative);
    }

    void ignoreProximitySensorUntilChangedInternal() {
        if (this.mIgnoreProximityUntilChanged || this.mProximity != 1) {
            return;
        }
        this.mIgnoreProximityUntilChanged = true;
        Slog.i(this.mTag, "Ignoring proximity");
        this.mNudgeUpdatePowerState.run();
    }

    private void sendOnProximityPositiveWithWakelock() {
        this.mWakelockController.acquireWakelock(1);
        this.mHandler.post(this.mWakelockController.getOnProximityPositiveRunnable());
    }

    private void sendOnProximityNegativeWithWakelock() {
        this.mWakelockController.acquireWakelock(2);
        this.mHandler.post(this.mWakelockController.getOnProximityNegativeRunnable());
    }

    private void loadProximitySensor() {
        if (this.mDisplayId != 0) {
            return;
        }
        Sensor findSensor = SensorUtils.findSensor(this.mSensorManager, this.mDisplayDeviceConfig.getProximitySensor(), 8);
        this.mProximitySensor = findSensor;
        if (findSensor != null) {
            this.mProximityThreshold = Math.min(findSensor.getMaximumRange(), TYPICAL_PROXIMITY_THRESHOLD);
        }
    }

    private void setProximitySensorEnabled(boolean z) {
        if (z) {
            if (this.mProximitySensorEnabled) {
                return;
            }
            this.mProximitySensorEnabled = true;
            this.mIgnoreProximityUntilChanged = false;
            this.mSensorManager.registerListener(this.mProximitySensorListener, this.mProximitySensor, 3, this.mHandler);
            return;
        }
        if (this.mProximitySensorEnabled) {
            this.mProximitySensorEnabled = false;
            this.mProximity = -1;
            this.mIgnoreProximityUntilChanged = false;
            this.mPendingProximity = -1;
            this.mHandler.removeMessages(1);
            this.mSensorManager.unregisterListener(this.mProximitySensorListener);
            if (this.mWakelockController.releaseWakelock(3)) {
                this.mPendingProximityDebounceTime = -1L;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleProximitySensorEvent(long j, boolean z) {
        if (this.mProximitySensorEnabled) {
            int i = this.mPendingProximity;
            if (i != 0 || z) {
                if (i == 1 && z) {
                    return;
                }
                this.mHandler.removeMessages(1);
                if (z) {
                    this.mPendingProximity = 1;
                    this.mPendingProximityDebounceTime = j + 0;
                    this.mWakelockController.acquireWakelock(3);
                } else {
                    this.mPendingProximity = 0;
                    this.mPendingProximityDebounceTime = j + 250;
                    this.mWakelockController.acquireWakelock(3);
                }
                debounceProximitySensor();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void debounceProximitySensor() {
        if (!this.mProximitySensorEnabled || this.mPendingProximity == -1 || this.mPendingProximityDebounceTime < 0) {
            return;
        }
        if (this.mPendingProximityDebounceTime <= this.mClock.uptimeMillis()) {
            if (this.mProximity != this.mPendingProximity) {
                this.mIgnoreProximityUntilChanged = false;
                Slog.i(this.mTag, "No longer ignoring proximity [" + this.mPendingProximity + "]");
            }
            this.mProximity = this.mPendingProximity;
            this.mNudgeUpdatePowerState.run();
            if (this.mWakelockController.releaseWakelock(3)) {
                this.mPendingProximityDebounceTime = -1L;
                return;
            }
            return;
        }
        this.mHandler.sendMessageAtTime(this.mHandler.obtainMessage(1), this.mPendingProximityDebounceTime);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class DisplayPowerProximityStateHandler extends Handler {
        DisplayPowerProximityStateHandler(Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                DisplayPowerProximityStateController.this.debounceProximitySensor();
            } else {
                if (i != 2) {
                    return;
                }
                DisplayPowerProximityStateController.this.ignoreProximitySensorUntilChangedInternal();
            }
        }
    }

    private String proximityToString(int i) {
        return i != -1 ? i != 0 ? i != 1 ? Integer.toString(i) : "Positive" : "Negative" : "Unknown";
    }

    @VisibleForTesting
    boolean getPendingWaitForNegativeProximityLocked() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mPendingWaitForNegativeProximityLocked;
        }
        return z;
    }

    @VisibleForTesting
    boolean getWaitingForNegativeProximity() {
        return this.mWaitingForNegativeProximity;
    }

    @VisibleForTesting
    boolean shouldIgnoreProximityUntilChanged() {
        return this.mIgnoreProximityUntilChanged;
    }

    boolean isProximitySensorEnabled() {
        return this.mProximitySensorEnabled;
    }

    @VisibleForTesting
    Handler getHandler() {
        return this.mHandler;
    }

    @VisibleForTesting
    int getPendingProximity() {
        return this.mPendingProximity;
    }

    @VisibleForTesting
    int getProximity() {
        return this.mProximity;
    }

    @VisibleForTesting
    long getPendingProximityDebounceTime() {
        return this.mPendingProximityDebounceTime;
    }

    @VisibleForTesting
    SensorEventListener getProximitySensorListener() {
        return this.mProximitySensorListener;
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    static class Injector {
        Injector() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ long lambda$createClock$0() {
            return SystemClock.uptimeMillis();
        }

        Clock createClock() {
            return new Clock() { // from class: com.android.server.display.DisplayPowerProximityStateController$Injector$$ExternalSyntheticLambda0
                @Override // com.android.server.display.DisplayPowerProximityStateController.Clock
                public final long uptimeMillis() {
                    long lambda$createClock$0;
                    lambda$createClock$0 = DisplayPowerProximityStateController.Injector.lambda$createClock$0();
                    return lambda$createClock$0;
                }
            };
        }
    }
}
