package com.android.server;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AnyMotionDetector {
    private static final long ACCELEROMETER_DATA_TIMEOUT_MILLIS = 3000;
    private static final boolean DEBUG = false;
    private static final long ORIENTATION_MEASUREMENT_DURATION_MILLIS = 2500;
    private static final long ORIENTATION_MEASUREMENT_INTERVAL_MILLIS = 5000;
    public static final int RESULT_MOVED = 1;
    public static final int RESULT_STATIONARY = 0;
    public static final int RESULT_UNKNOWN = -1;
    private static final int SAMPLING_INTERVAL_MILLIS = 40;
    private static final int STALE_MEASUREMENT_TIMEOUT_MILLIS = 120000;
    private static final int STATE_ACTIVE = 1;
    private static final int STATE_INACTIVE = 0;
    private static final String TAG = "AnyMotionDetector";
    private static final long WAKELOCK_TIMEOUT_MILLIS = 30000;
    private final float THRESHOLD_ENERGY = 5.0f;
    private final Sensor mAccelSensor;
    private final DeviceIdleCallback mCallback;

    @GuardedBy({"mLock"})
    private Vector3 mCurrentGravityVector;
    private final Handler mHandler;
    private final SensorEventListener mListener;
    private final Object mLock;

    @GuardedBy({"mLock"})
    private boolean mMeasurementInProgress;
    private final Runnable mMeasurementTimeout;

    @GuardedBy({"mLock"})
    private boolean mMeasurementTimeoutIsActive;

    @GuardedBy({"mLock"})
    private int mNumSufficientSamples;

    @GuardedBy({"mLock"})
    private Vector3 mPreviousGravityVector;

    @GuardedBy({"mLock"})
    private final RunningSignalStats mRunningStats;
    private final SensorManager mSensorManager;
    private final Runnable mSensorRestart;

    @GuardedBy({"mLock"})
    private boolean mSensorRestartIsActive;

    @GuardedBy({"mLock"})
    private int mState;
    private final float mThresholdAngle;
    private final PowerManager.WakeLock mWakeLock;
    private final Runnable mWakelockTimeout;
    private volatile boolean mWakelockTimeoutIsActive;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    interface DeviceIdleCallback {
        void onAnyMotionResult(int i);
    }

    public AnyMotionDetector(PowerManager powerManager, Handler handler, SensorManager sensorManager, DeviceIdleCallback deviceIdleCallback, float f) {
        Object obj = new Object();
        this.mLock = obj;
        this.mCurrentGravityVector = null;
        this.mPreviousGravityVector = null;
        this.mListener = new SensorEventListener() { // from class: com.android.server.AnyMotionDetector.1
            @Override // android.hardware.SensorEventListener
            public void onAccuracyChanged(Sensor sensor, int i) {
            }

            @Override // android.hardware.SensorEventListener
            public void onSensorChanged(SensorEvent sensorEvent) {
                int stopOrientationMeasurementLocked;
                synchronized (AnyMotionDetector.this.mLock) {
                    long elapsedRealtime = SystemClock.elapsedRealtime();
                    float[] fArr = sensorEvent.values;
                    AnyMotionDetector.this.mRunningStats.accumulate(new Vector3(elapsedRealtime, fArr[0], fArr[1], fArr[2]));
                    stopOrientationMeasurementLocked = AnyMotionDetector.this.mRunningStats.getSampleCount() >= AnyMotionDetector.this.mNumSufficientSamples ? AnyMotionDetector.this.stopOrientationMeasurementLocked() : -1;
                }
                if (stopOrientationMeasurementLocked != -1) {
                    AnyMotionDetector.this.mHandler.removeCallbacks(AnyMotionDetector.this.mWakelockTimeout);
                    AnyMotionDetector.this.mWakelockTimeoutIsActive = false;
                    AnyMotionDetector.this.mCallback.onAnyMotionResult(stopOrientationMeasurementLocked);
                }
            }
        };
        this.mSensorRestart = new Runnable() { // from class: com.android.server.AnyMotionDetector.2
            @Override // java.lang.Runnable
            public void run() {
                synchronized (AnyMotionDetector.this.mLock) {
                    if (AnyMotionDetector.this.mSensorRestartIsActive) {
                        AnyMotionDetector.this.mSensorRestartIsActive = false;
                        AnyMotionDetector.this.startOrientationMeasurementLocked();
                    }
                }
            }
        };
        this.mMeasurementTimeout = new Runnable() { // from class: com.android.server.AnyMotionDetector.3
            @Override // java.lang.Runnable
            public void run() {
                int i;
                synchronized (AnyMotionDetector.this.mLock) {
                    if (AnyMotionDetector.this.mMeasurementTimeoutIsActive) {
                        AnyMotionDetector.this.mMeasurementTimeoutIsActive = false;
                        i = AnyMotionDetector.this.stopOrientationMeasurementLocked();
                    } else {
                        i = -1;
                    }
                }
                if (i != -1) {
                    AnyMotionDetector.this.mHandler.removeCallbacks(AnyMotionDetector.this.mWakelockTimeout);
                    AnyMotionDetector.this.mWakelockTimeoutIsActive = false;
                    AnyMotionDetector.this.mCallback.onAnyMotionResult(i);
                }
            }
        };
        this.mWakelockTimeout = new Runnable() { // from class: com.android.server.AnyMotionDetector.4
            @Override // java.lang.Runnable
            public void run() {
                synchronized (AnyMotionDetector.this.mLock) {
                    if (AnyMotionDetector.this.mWakelockTimeoutIsActive) {
                        AnyMotionDetector.this.mWakelockTimeoutIsActive = false;
                        AnyMotionDetector.this.stop();
                    }
                }
            }
        };
        synchronized (obj) {
            PowerManager.WakeLock newWakeLock = powerManager.newWakeLock(1, TAG);
            this.mWakeLock = newWakeLock;
            newWakeLock.setReferenceCounted(false);
            this.mHandler = handler;
            this.mSensorManager = sensorManager;
            this.mAccelSensor = sensorManager.getDefaultSensor(1);
            this.mMeasurementInProgress = false;
            this.mMeasurementTimeoutIsActive = false;
            this.mWakelockTimeoutIsActive = false;
            this.mSensorRestartIsActive = false;
            this.mState = 0;
            this.mCallback = deviceIdleCallback;
            this.mThresholdAngle = f;
            this.mRunningStats = new RunningSignalStats();
            this.mNumSufficientSamples = (int) Math.ceil(62.5d);
        }
    }

    public boolean hasSensor() {
        return this.mAccelSensor != null;
    }

    public void checkForAnyMotion() {
        synchronized (this.mLock) {
            if (this.mState != 1) {
                this.mState = 1;
                this.mCurrentGravityVector = null;
                this.mPreviousGravityVector = null;
                this.mWakeLock.acquire();
                this.mHandler.sendMessageDelayed(Message.obtain(this.mHandler, this.mWakelockTimeout), 30000L);
                this.mWakelockTimeoutIsActive = true;
                startOrientationMeasurementLocked();
            }
        }
    }

    public void stop() {
        synchronized (this.mLock) {
            if (this.mState == 1) {
                this.mState = 0;
            }
            this.mHandler.removeCallbacks(this.mMeasurementTimeout);
            this.mHandler.removeCallbacks(this.mSensorRestart);
            this.mMeasurementTimeoutIsActive = false;
            this.mSensorRestartIsActive = false;
            if (this.mMeasurementInProgress) {
                this.mMeasurementInProgress = false;
                this.mSensorManager.unregisterListener(this.mListener);
            }
            this.mCurrentGravityVector = null;
            this.mPreviousGravityVector = null;
            if (this.mWakeLock.isHeld()) {
                this.mHandler.removeCallbacks(this.mWakelockTimeout);
                this.mWakelockTimeoutIsActive = false;
                this.mWakeLock.release();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void startOrientationMeasurementLocked() {
        Sensor sensor;
        if (this.mMeasurementInProgress || (sensor = this.mAccelSensor) == null) {
            return;
        }
        if (this.mSensorManager.registerListener(this.mListener, sensor, EventLogTags.VOLUME_CHANGED)) {
            this.mMeasurementInProgress = true;
            this.mRunningStats.reset();
        }
        this.mHandler.sendMessageDelayed(Message.obtain(this.mHandler, this.mMeasurementTimeout), 3000L);
        this.mMeasurementTimeoutIsActive = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public int stopOrientationMeasurementLocked() {
        if (!this.mMeasurementInProgress) {
            return -1;
        }
        this.mHandler.removeCallbacks(this.mMeasurementTimeout);
        this.mMeasurementTimeoutIsActive = false;
        this.mSensorManager.unregisterListener(this.mListener);
        this.mMeasurementInProgress = false;
        this.mPreviousGravityVector = this.mCurrentGravityVector;
        this.mCurrentGravityVector = this.mRunningStats.getRunningAverage();
        if (this.mRunningStats.getSampleCount() == 0) {
            Slog.w(TAG, "No accelerometer data acquired for orientation measurement.");
        }
        int stationaryStatusLocked = getStationaryStatusLocked();
        this.mRunningStats.reset();
        if (stationaryStatusLocked != -1) {
            if (this.mWakeLock.isHeld()) {
                this.mHandler.removeCallbacks(this.mWakelockTimeout);
                this.mWakelockTimeoutIsActive = false;
                this.mWakeLock.release();
            }
            this.mState = 0;
        } else {
            this.mHandler.sendMessageDelayed(Message.obtain(this.mHandler, this.mSensorRestart), ORIENTATION_MEASUREMENT_INTERVAL_MILLIS);
            this.mSensorRestartIsActive = true;
        }
        return stationaryStatusLocked;
    }

    @GuardedBy({"mLock"})
    private int getStationaryStatusLocked() {
        Vector3 vector3 = this.mPreviousGravityVector;
        if (vector3 == null || this.mCurrentGravityVector == null) {
            return -1;
        }
        float angleBetween = vector3.normalized().angleBetween(this.mCurrentGravityVector.normalized());
        if (angleBetween >= this.mThresholdAngle || this.mRunningStats.getEnergy() >= 5.0f) {
            return (!Float.isNaN(angleBetween) && this.mCurrentGravityVector.timeMillisSinceBoot - this.mPreviousGravityVector.timeMillisSinceBoot > 120000) ? -1 : 1;
        }
        return 0;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class Vector3 {
        public long timeMillisSinceBoot;
        public float x;
        public float y;
        public float z;

        public Vector3(long j, float f, float f2, float f3) {
            this.timeMillisSinceBoot = j;
            this.x = f;
            this.y = f2;
            this.z = f3;
        }

        public float norm() {
            return (float) Math.sqrt(dotProduct(this));
        }

        public Vector3 normalized() {
            float norm = norm();
            return new Vector3(this.timeMillisSinceBoot, this.x / norm, this.y / norm, this.z / norm);
        }

        public float angleBetween(Vector3 vector3) {
            float abs = Math.abs((float) Math.toDegrees(Math.atan2(cross(vector3).norm(), dotProduct(vector3))));
            Slog.d(AnyMotionDetector.TAG, "angleBetween: this = " + toString() + ", other = " + vector3.toString() + ", degrees = " + abs);
            return abs;
        }

        public Vector3 cross(Vector3 vector3) {
            long j = vector3.timeMillisSinceBoot;
            float f = this.y;
            float f2 = vector3.z;
            float f3 = this.z;
            float f4 = vector3.y;
            float f5 = vector3.x;
            float f6 = this.x;
            return new Vector3(j, (f * f2) - (f3 * f4), (f3 * f5) - (f2 * f6), (f6 * f4) - (f * f5));
        }

        public String toString() {
            return ((("timeMillisSinceBoot=" + this.timeMillisSinceBoot) + " | x=" + this.x) + ", y=" + this.y) + ", z=" + this.z;
        }

        public float dotProduct(Vector3 vector3) {
            return (this.x * vector3.x) + (this.y * vector3.y) + (this.z * vector3.z);
        }

        public Vector3 times(float f) {
            return new Vector3(this.timeMillisSinceBoot, this.x * f, this.y * f, this.z * f);
        }

        public Vector3 plus(Vector3 vector3) {
            return new Vector3(vector3.timeMillisSinceBoot, vector3.x + this.x, vector3.y + this.y, this.z + vector3.z);
        }

        public Vector3 minus(Vector3 vector3) {
            return new Vector3(vector3.timeMillisSinceBoot, this.x - vector3.x, this.y - vector3.y, this.z - vector3.z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class RunningSignalStats {
        Vector3 currentVector;
        float energy;
        Vector3 previousVector;
        Vector3 runningSum;
        int sampleCount;

        public RunningSignalStats() {
            reset();
        }

        public void reset() {
            this.previousVector = null;
            this.currentVector = null;
            this.runningSum = new Vector3(0L, 0.0f, 0.0f, 0.0f);
            this.energy = 0.0f;
            this.sampleCount = 0;
        }

        public void accumulate(Vector3 vector3) {
            if (vector3 == null) {
                return;
            }
            this.sampleCount++;
            this.runningSum = this.runningSum.plus(vector3);
            Vector3 vector32 = this.currentVector;
            this.previousVector = vector32;
            this.currentVector = vector3;
            if (vector32 != null) {
                Vector3 minus = vector3.minus(vector32);
                float f = minus.x;
                float f2 = minus.y;
                float f3 = minus.z;
                this.energy += (f * f) + (f2 * f2) + (f3 * f3);
            }
        }

        public Vector3 getRunningAverage() {
            int i = this.sampleCount;
            if (i > 0) {
                return this.runningSum.times(1.0f / i);
            }
            return null;
        }

        public float getEnergy() {
            return this.energy;
        }

        public int getSampleCount() {
            return this.sampleCount;
        }

        public String toString() {
            Vector3 vector3 = this.currentVector;
            String vector32 = vector3 == null ? "null" : vector3.toString();
            Vector3 vector33 = this.previousVector;
            return ((("previousVector = " + (vector33 != null ? vector33.toString() : "null")) + ", currentVector = " + vector32) + ", sampleCount = " + this.sampleCount) + ", energy = " + this.energy;
        }
    }
}
