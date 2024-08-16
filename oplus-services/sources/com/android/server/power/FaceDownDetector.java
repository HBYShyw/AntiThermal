package com.android.server.power;

import android.R;
import android.app.ActivityThread;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.DeviceConfig;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.OplusIoThread;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class FaceDownDetector implements SensorEventListener {
    private static final boolean DEBUG = false;
    static final float DEFAULT_ACCELERATION_THRESHOLD = 0.2f;
    private static final boolean DEFAULT_FEATURE_ENABLED = true;
    private static final long DEFAULT_INTERACTION_BACKOFF = 60000;
    static final long DEFAULT_TIME_THRESHOLD_MILLIS = 1000;
    static final float DEFAULT_Z_ACCELERATION_THRESHOLD = -9.5f;
    static final String KEY_ACCELERATION_THRESHOLD = "acceleration_threshold";
    static final String KEY_FEATURE_ENABLED = "enable_flip_to_screen_off";
    private static final String KEY_INTERACTION_BACKOFF = "face_down_interaction_backoff_millis";
    static final String KEY_TIME_THRESHOLD_MILLIS = "time_threshold_millis";
    static final String KEY_Z_ACCELERATION_THRESHOLD = "z_acceleration_threshold";
    private static final float MOVING_AVERAGE_WEIGHT = 0.5f;
    private static final int SCREEN_OFF_RESULT = 4;
    private static final String TAG = "FaceDownDetector";
    private static final int UNFLIP = 2;
    private static final int UNKNOWN = 1;
    private static final int USER_INTERACTION = 3;
    private float mAccelerationThreshold;
    private Sensor mAccelerometer;
    private Context mContext;
    private final Handler mHandler;
    private boolean mIsEnabled;
    private final Consumer<Boolean> mOnFlip;

    @VisibleForTesting
    final BroadcastReceiver mScreenReceiver;
    private SensorManager mSensorManager;
    private int mSensorMaxLatencyMicros;
    private Duration mTimeThreshold;
    private final Runnable mUserActivityRunnable;
    private long mUserInteractionBackoffMillis;
    private float mZAccelerationThreshold;
    private float mZAccelerationThresholdLenient;
    private long mLastFlipTime = 0;
    public int mPreviousResultType = 1;
    public long mPreviousResultTime = 0;
    private long mMillisSaved = 0;
    private final ExponentialMovingAverage mCurrentXYAcceleration = new ExponentialMovingAverage(this, MOVING_AVERAGE_WEIGHT);
    private final ExponentialMovingAverage mCurrentZAcceleration = new ExponentialMovingAverage(this, MOVING_AVERAGE_WEIGHT);
    private boolean mFaceDown = false;
    private boolean mInteractive = false;
    private boolean mActive = false;
    private float mPrevAcceleration = 0.0f;
    private long mPrevAccelerationTime = 0;
    private boolean mZAccelerationIsFaceDown = false;
    private long mZAccelerationFaceDownTime = 0;

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public FaceDownDetector(Consumer<Boolean> consumer) {
        Objects.requireNonNull(consumer);
        this.mOnFlip = consumer;
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mScreenReceiver = new ScreenStateReceiver();
        this.mUserActivityRunnable = new Runnable() { // from class: com.android.server.power.FaceDownDetector$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                FaceDownDetector.this.lambda$new$0();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        if (this.mFaceDown) {
            exitFaceDown(3, SystemClock.uptimeMillis() - this.mLastFlipTime);
            updateActiveState();
        }
    }

    public void systemReady(Context context) {
        this.mContext = context;
        SensorManager sensorManager = (SensorManager) context.getSystemService(SensorManager.class);
        this.mSensorManager = sensorManager;
        this.mAccelerometer = sensorManager.getDefaultSensor(1);
        readValuesFromDeviceConfig();
        DeviceConfig.addOnPropertiesChangedListener("attention_manager_service", ActivityThread.currentApplication().getMainExecutor(), new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.power.FaceDownDetector$$ExternalSyntheticLambda1
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                FaceDownDetector.this.lambda$systemReady$1(properties);
            }
        });
        updateActiveState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$systemReady$1(DeviceConfig.Properties properties) {
        onDeviceConfigChange(properties.getKeyset());
    }

    private void registerScreenReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.setPriority(1000);
        context.registerReceiver(this.mScreenReceiver, intentFilter, null, OplusIoThread.getHandler());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateActiveState() {
        long uptimeMillis = SystemClock.uptimeMillis();
        boolean z = this.mPreviousResultType == 3 && uptimeMillis - this.mPreviousResultTime < this.mUserInteractionBackoffMillis;
        boolean z2 = this.mInteractive;
        boolean z3 = z2 && this.mIsEnabled && !z;
        if (this.mActive != z3) {
            if (z3) {
                this.mSensorManager.registerListener(this, this.mAccelerometer, 3, this.mSensorMaxLatencyMicros, OplusIoThread.getHandler());
                if (this.mPreviousResultType == 4) {
                    logScreenOff();
                }
            } else {
                if (this.mFaceDown && !z2) {
                    this.mPreviousResultType = 4;
                    this.mPreviousResultTime = uptimeMillis;
                }
                this.mSensorManager.unregisterListener(this);
                this.mFaceDown = false;
                this.mOnFlip.accept(Boolean.FALSE);
            }
            this.mActive = z3;
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("FaceDownDetector:");
        printWriter.println("  mFaceDown=" + this.mFaceDown);
        printWriter.println("  mActive=" + this.mActive);
        printWriter.println("  mLastFlipTime=" + this.mLastFlipTime);
        printWriter.println("  mSensorMaxLatencyMicros=" + this.mSensorMaxLatencyMicros);
        printWriter.println("  mUserInteractionBackoffMillis=" + this.mUserInteractionBackoffMillis);
        printWriter.println("  mPreviousResultTime=" + this.mPreviousResultTime);
        printWriter.println("  mPreviousResultType=" + this.mPreviousResultType);
        printWriter.println("  mMillisSaved=" + this.mMillisSaved);
        printWriter.println("  mZAccelerationThreshold=" + this.mZAccelerationThreshold);
        printWriter.println("  mAccelerationThreshold=" + this.mAccelerationThreshold);
        printWriter.println("  mTimeThreshold=" + this.mTimeThreshold);
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == 1 && this.mActive && this.mIsEnabled) {
            float[] fArr = sensorEvent.values;
            float f = fArr[0];
            float f2 = fArr[1];
            this.mCurrentXYAcceleration.updateMovingAverage((f * f) + (f2 * f2));
            this.mCurrentZAcceleration.updateMovingAverage(sensorEvent.values[2]);
            long j = sensorEvent.timestamp;
            if (Math.abs(this.mCurrentXYAcceleration.mMovingAverage - this.mPrevAcceleration) > this.mAccelerationThreshold) {
                this.mPrevAcceleration = this.mCurrentXYAcceleration.mMovingAverage;
                this.mPrevAccelerationTime = j;
            }
            boolean z = j - this.mPrevAccelerationTime <= this.mTimeThreshold.toNanos();
            boolean z2 = this.mCurrentZAcceleration.mMovingAverage < (this.mFaceDown ? this.mZAccelerationThresholdLenient : this.mZAccelerationThreshold);
            boolean z3 = z2 && this.mZAccelerationIsFaceDown && j - this.mZAccelerationFaceDownTime > this.mTimeThreshold.toNanos();
            if (z2 && !this.mZAccelerationIsFaceDown) {
                this.mZAccelerationFaceDownTime = j;
                this.mZAccelerationIsFaceDown = true;
            } else if (!z2) {
                this.mZAccelerationIsFaceDown = false;
            }
            if (!z && z3 && !this.mFaceDown) {
                faceDownDetected();
            } else {
                if (z3 || !this.mFaceDown) {
                    return;
                }
                unFlipDetected();
            }
        }
    }

    private void faceDownDetected() {
        this.mLastFlipTime = SystemClock.uptimeMillis();
        this.mFaceDown = true;
        this.mOnFlip.accept(Boolean.TRUE);
    }

    private void unFlipDetected() {
        exitFaceDown(2, SystemClock.uptimeMillis() - this.mLastFlipTime);
    }

    public void userActivity(int i) {
        if (i != 5) {
            OplusIoThread.getHandler().post(this.mUserActivityRunnable);
        }
    }

    private void exitFaceDown(int i, long j) {
        FrameworkStatsLog.write(337, i, j, 0L, 0L);
        this.mFaceDown = false;
        this.mLastFlipTime = 0L;
        this.mPreviousResultType = i;
        this.mPreviousResultTime = SystemClock.uptimeMillis();
        this.mOnFlip.accept(Boolean.FALSE);
    }

    private void logScreenOff() {
        long uptimeMillis = SystemClock.uptimeMillis();
        long j = this.mPreviousResultTime;
        FrameworkStatsLog.write(337, 4, j - this.mLastFlipTime, this.mMillisSaved, uptimeMillis - j);
        this.mPreviousResultType = 1;
    }

    private boolean isEnabled() {
        return DeviceConfig.getBoolean("attention_manager_service", KEY_FEATURE_ENABLED, true) && this.mContext.getResources().getBoolean(17891697);
    }

    private float getAccelerationThreshold() {
        return getFloatFlagValue(KEY_ACCELERATION_THRESHOLD, DEFAULT_ACCELERATION_THRESHOLD, -2.0f, 2.0f);
    }

    private float getZAccelerationThreshold() {
        return getFloatFlagValue(KEY_Z_ACCELERATION_THRESHOLD, DEFAULT_Z_ACCELERATION_THRESHOLD, -15.0f, 0.0f);
    }

    private long getUserInteractionBackoffMillis() {
        return getLongFlagValue(KEY_INTERACTION_BACKOFF, DEFAULT_INTERACTION_BACKOFF, 0L, 3600000L);
    }

    private int getSensorMaxLatencyMicros() {
        return this.mContext.getResources().getInteger(R.integer.config_navBarOpacityMode);
    }

    private float getFloatFlagValue(String str, float f, float f2, float f3) {
        float f4 = DeviceConfig.getFloat("attention_manager_service", str, f);
        if (f4 >= f2 && f4 <= f3) {
            return f4;
        }
        Slog.w(TAG, "Bad flag value supplied for: " + str);
        return f;
    }

    private long getLongFlagValue(String str, long j, long j2, long j3) {
        long j4 = DeviceConfig.getLong("attention_manager_service", str, j);
        if (j4 >= j2 && j4 <= j3) {
            return j4;
        }
        Slog.w(TAG, "Bad flag value supplied for: " + str);
        return j;
    }

    private Duration getTimeThreshold() {
        long j = DeviceConfig.getLong("attention_manager_service", KEY_TIME_THRESHOLD_MILLIS, 1000L);
        if (j < 0 || j > 15000) {
            Slog.w(TAG, "Bad flag value supplied for: time_threshold_millis");
            return Duration.ofMillis(1000L);
        }
        return Duration.ofMillis(j);
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x004c A[LOOP:0: B:2:0x0004->B:19:0x004c, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0063 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void onDeviceConfigChange(Set<String> set) {
        for (String str : set) {
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -1974380596:
                    if (str.equals(KEY_TIME_THRESHOLD_MILLIS)) {
                        c = 0;
                    }
                    switch (c) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                            readValuesFromDeviceConfig();
                            updateActiveState();
                            return;
                        default:
                            Slog.i(TAG, "Ignoring change on " + str);
                    }
                case -1762356372:
                    if (str.equals(KEY_ACCELERATION_THRESHOLD)) {
                        c = 1;
                    }
                    switch (c) {
                    }
                case -1566292150:
                    if (str.equals(KEY_FEATURE_ENABLED)) {
                        c = 2;
                    }
                    switch (c) {
                    }
                case 941263057:
                    if (str.equals(KEY_Z_ACCELERATION_THRESHOLD)) {
                        c = 3;
                    }
                    switch (c) {
                    }
                default:
                    switch (c) {
                    }
            }
        }
    }

    private void readValuesFromDeviceConfig() {
        this.mAccelerationThreshold = getAccelerationThreshold();
        float zAccelerationThreshold = getZAccelerationThreshold();
        this.mZAccelerationThreshold = zAccelerationThreshold;
        this.mZAccelerationThresholdLenient = zAccelerationThreshold + 1.0f;
        this.mTimeThreshold = getTimeThreshold();
        this.mSensorMaxLatencyMicros = getSensorMaxLatencyMicros();
        this.mUserInteractionBackoffMillis = getUserInteractionBackoffMillis();
        boolean z = this.mIsEnabled;
        boolean isEnabled = isEnabled();
        this.mIsEnabled = isEnabled;
        if (z != isEnabled) {
            if (!isEnabled) {
                this.mContext.unregisterReceiver(this.mScreenReceiver);
                this.mInteractive = false;
            } else {
                registerScreenReceiver(this.mContext);
                this.mInteractive = ((PowerManager) this.mContext.getSystemService(PowerManager.class)).isInteractive();
            }
        }
        Slog.i(TAG, "readValuesFromDeviceConfig():\nmAccelerationThreshold=" + this.mAccelerationThreshold + "\nmZAccelerationThreshold=" + this.mZAccelerationThreshold + "\nmTimeThreshold=" + this.mTimeThreshold + "\nmIsEnabled=" + this.mIsEnabled);
    }

    public void setMillisSaved(long j) {
        this.mMillisSaved = j;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class ScreenStateReceiver extends BroadcastReceiver {
        private ScreenStateReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
                FaceDownDetector.this.mInteractive = false;
                FaceDownDetector.this.updateActiveState();
            } else if ("android.intent.action.SCREEN_ON".equals(intent.getAction())) {
                FaceDownDetector.this.mInteractive = true;
                FaceDownDetector.this.updateActiveState();
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class ExponentialMovingAverage {
        private final float mAlpha;
        private final float mInitialAverage;
        private float mMovingAverage;

        ExponentialMovingAverage(FaceDownDetector faceDownDetector, float f) {
            this(f, 0.0f);
        }

        ExponentialMovingAverage(float f, float f2) {
            this.mAlpha = f;
            this.mInitialAverage = f2;
            this.mMovingAverage = f2;
        }

        void updateMovingAverage(float f) {
            this.mMovingAverage = f + (this.mAlpha * (this.mMovingAverage - f));
        }

        void reset() {
            this.mMovingAverage = this.mInitialAverage;
        }
    }
}
