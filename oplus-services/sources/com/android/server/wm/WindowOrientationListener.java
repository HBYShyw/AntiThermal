package com.android.server.wm;

import android.R;
import android.app.ActivityThread;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.DeviceConfig;
import android.rotationresolver.RotationResolverInternal;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import android.view.Surface;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.LocalServices;
import com.android.server.wm.WindowOrientationListener;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public abstract class WindowOrientationListener {
    private static final int DEFAULT_BATCH_LATENCY = 100000;
    private static final long DEFAULT_ROTATION_MEMORIZATION_TIMEOUT_MILLIS = 3000;
    private static final long DEFAULT_ROTATION_RESOLVER_TIMEOUT_MILLIS = 700;
    private static final String KEY_ROTATION_MEMORIZATION_TIMEOUT = "rotation_memorization_timeout_millis";
    private static final String KEY_ROTATION_RESOLVER_TIMEOUT = "rotation_resolver_timeout_millis";
    private static final boolean LOG = SystemProperties.getBoolean("debug.orientation.log", false);
    private static final String TAG = "WindowOrientationListener";
    private static final int TYPE_UI_DEVICE_ORIENTATION = 268369948;
    private static final boolean USE_GRAVITY_SENSOR = false;
    private final Context mContext;
    private int mCurrentRotation;
    private final int mDefaultRotation;
    private boolean mEnabled;
    private Handler mHandler;
    private final Object mLock;

    @VisibleForTesting
    OrientationJudge mOrientationJudge;
    private int mRate;

    @VisibleForTesting
    RotationResolverInternal mRotationResolverService;
    private Sensor mSensor;
    private SensorManager mSensorManager;
    private String mSensorType;
    IWindowOrientationListenerExt mWindowOrientationListenerExt;

    public abstract boolean isKeyguardShowingAndNotOccluded();

    @VisibleForTesting
    abstract boolean isRotationResolverEnabled();

    public abstract void onProposedRotationChanged(int i);

    public WindowOrientationListener(Context context, Handler handler, int i) {
        this(context, handler, i, 2);
    }

    private WindowOrientationListener(Context context, Handler handler, int i, int i2) {
        this.mCurrentRotation = -1;
        this.mLock = new Object();
        this.mWindowOrientationListenerExt = (IWindowOrientationListenerExt) ExtLoader.type(IWindowOrientationListenerExt.class).base(this).create();
        this.mContext = context;
        this.mHandler = handler;
        this.mDefaultRotation = i;
        this.mSensorManager = (SensorManager) context.getSystemService("sensor");
        this.mRate = i2;
        Sensor sensor = null;
        List<Sensor> sensorList = this.mWindowOrientationListenerExt.isFlipDevice() ? this.mSensorManager.getSensorList(TYPE_UI_DEVICE_ORIENTATION) : null;
        Sensor sensor2 = null;
        for (Sensor sensor3 : (sensorList == null || sensorList.isEmpty()) ? this.mSensorManager.getSensorList(27) : sensorList) {
            if (sensor3.isWakeUpSensor()) {
                sensor = sensor3;
            } else {
                sensor2 = sensor3;
            }
        }
        if (sensor != null) {
            this.mSensor = sensor;
        } else {
            this.mSensor = sensor2;
        }
        if (this.mSensor != null) {
            this.mOrientationJudge = new OrientationSensorJudge();
        }
        if (this.mOrientationJudge == null) {
            Sensor defaultSensor = this.mSensorManager.getDefaultSensor(1);
            this.mSensor = defaultSensor;
            if (defaultSensor != null) {
                this.mOrientationJudge = new AccelSensorJudge(context);
            }
        }
    }

    public void enable() {
        enable(true);
    }

    public void enable(boolean z) {
        synchronized (this.mLock) {
            if (this.mSensor == null) {
                Slog.w(TAG, "Cannot detect sensors. Not enabled");
                return;
            }
            if (this.mEnabled) {
                return;
            }
            if (LOG) {
                Slog.d(TAG, "WindowOrientationListener enabled clearCurrentRotation=" + z);
            }
            this.mOrientationJudge.resetLocked(z);
            if (this.mSensor.getType() == 1) {
                this.mSensorManager.registerListener(this.mOrientationJudge, this.mSensor, this.mRate, DEFAULT_BATCH_LATENCY, this.mHandler);
            } else {
                this.mSensorManager.registerListener(this.mOrientationJudge, this.mSensor, this.mRate, this.mHandler);
            }
            this.mEnabled = true;
        }
    }

    public void disable() {
        synchronized (this.mLock) {
            if (this.mSensor == null) {
                Slog.w(TAG, "Cannot detect sensors. Invalid disable");
                return;
            }
            if (this.mEnabled) {
                if (LOG) {
                    Slog.d(TAG, "WindowOrientationListener disabled");
                }
                this.mSensorManager.unregisterListener(this.mOrientationJudge);
                this.mEnabled = false;
            }
        }
    }

    public void onTouchStart() {
        synchronized (this.mLock) {
            OrientationJudge orientationJudge = this.mOrientationJudge;
            if (orientationJudge != null) {
                orientationJudge.onTouchStartLocked();
            }
        }
    }

    public void onTouchEnd() {
        long elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
        synchronized (this.mLock) {
            OrientationJudge orientationJudge = this.mOrientationJudge;
            if (orientationJudge != null) {
                orientationJudge.onTouchEndLocked(elapsedRealtimeNanos);
            }
        }
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    public void setCurrentRotation(int i) {
        synchronized (this.mLock) {
            this.mCurrentRotation = i;
        }
    }

    public int getProposedRotation() {
        synchronized (this.mLock) {
            if (!this.mEnabled) {
                return -1;
            }
            return this.mOrientationJudge.getProposedRotationLocked();
        }
    }

    public boolean canDetectOrientation() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mSensor != null;
        }
        return z;
    }

    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        synchronized (this.mLock) {
            protoOutputStream.write(1133871366145L, this.mEnabled);
            protoOutputStream.write(1159641169922L, this.mCurrentRotation);
        }
        protoOutputStream.end(start);
    }

    public void dump(PrintWriter printWriter, String str) {
        synchronized (this.mLock) {
            printWriter.println(str + TAG);
            String str2 = str + "  ";
            printWriter.println(str2 + "mEnabled=" + this.mEnabled);
            printWriter.println(str2 + "mCurrentRotation=" + Surface.rotationToString(this.mCurrentRotation));
            printWriter.println(str2 + "mSensorType=" + this.mSensorType);
            printWriter.println(str2 + "mSensor=" + this.mSensor);
            printWriter.println(str2 + "mRate=" + this.mRate);
            OrientationJudge orientationJudge = this.mOrientationJudge;
            if (orientationJudge != null) {
                orientationJudge.dumpLocked(printWriter, str2);
            }
        }
    }

    public boolean shouldStayEnabledWhileDreaming() {
        if (this.mContext.getResources().getBoolean(17891699)) {
            return true;
        }
        return this.mSensor.getType() == 27 && this.mSensor.isWakeUpSensor();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public abstract class OrientationJudge implements SensorEventListener {
        protected static final float MILLIS_PER_NANO = 1.0E-6f;
        protected static final long NANOS_PER_MS = 1000000;
        protected static final long PROPOSAL_MIN_TIME_SINCE_TOUCH_END_NANOS = 500000000;

        public abstract void dumpLocked(PrintWriter printWriter, String str);

        public abstract int getProposedRotationLocked();

        @Override // android.hardware.SensorEventListener
        public abstract void onAccuracyChanged(Sensor sensor, int i);

        @Override // android.hardware.SensorEventListener
        public abstract void onSensorChanged(SensorEvent sensorEvent);

        public abstract void onTouchEndLocked(long j);

        public abstract void onTouchStartLocked();

        public abstract void resetLocked(boolean z);

        OrientationJudge() {
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    final class AccelSensorJudge extends OrientationJudge {
        private static final float ACCELERATION_TOLERANCE = 4.0f;
        private static final int ACCELEROMETER_DATA_X = 0;
        private static final int ACCELEROMETER_DATA_Y = 1;
        private static final int ACCELEROMETER_DATA_Z = 2;
        private static final int ADJACENT_ORIENTATION_ANGLE_GAP = 45;
        private static final float FILTER_TIME_CONSTANT_MS = 200.0f;
        private static final float FLAT_ANGLE = 80.0f;
        private static final long FLAT_TIME_NANOS = 1000000000;
        private static final float MAX_ACCELERATION_MAGNITUDE = 13.80665f;
        private static final long MAX_FILTER_DELTA_TIME_NANOS = 1000000000;
        private static final int MAX_TILT = 80;
        private static final float MIN_ACCELERATION_MAGNITUDE = 5.80665f;
        private static final float NEAR_ZERO_MAGNITUDE = 1.0f;
        private static final long PROPOSAL_MIN_TIME_SINCE_ACCELERATION_ENDED_NANOS = 500000000;
        private static final long PROPOSAL_MIN_TIME_SINCE_FLAT_ENDED_NANOS = 500000000;
        private static final long PROPOSAL_MIN_TIME_SINCE_SWING_ENDED_NANOS = 300000000;
        private static final long PROPOSAL_SETTLE_TIME_NANOS = 40000000;
        private static final float RADIANS_TO_DEGREES = 57.29578f;
        private static final float SWING_AWAY_ANGLE_DELTA = 20.0f;
        private static final long SWING_TIME_NANOS = 300000000;
        private static final int TILT_HISTORY_SIZE = 200;
        private static final int TILT_OVERHEAD_ENTER = -40;
        private static final int TILT_OVERHEAD_EXIT = -15;
        private boolean mAccelerating;
        private long mAccelerationTimestampNanos;
        private boolean mFlat;
        private long mFlatTimestampNanos;
        private long mLastFilteredTimestampNanos;
        private float mLastFilteredX;
        private float mLastFilteredY;
        private float mLastFilteredZ;
        private boolean mOverhead;
        private int mPredictedRotation;
        private long mPredictedRotationTimestampNanos;
        private int mProposedRotation;
        private long mSwingTimestampNanos;
        private boolean mSwinging;
        private float[] mTiltHistory;
        private int mTiltHistoryIndex;
        private long[] mTiltHistoryTimestampNanos;
        private final int[][] mTiltToleranceConfig;
        private long mTouchEndedTimestampNanos;
        private boolean mTouched;

        private boolean isAcceleratingLocked(float f) {
            return f < MIN_ACCELERATION_MAGNITUDE || f > MAX_ACCELERATION_MAGNITUDE;
        }

        private float remainingMS(long j, long j2) {
            if (j >= j2) {
                return 0.0f;
            }
            return ((float) (j2 - j)) * 1.0E-6f;
        }

        @Override // com.android.server.wm.WindowOrientationListener.OrientationJudge, android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        public AccelSensorJudge(Context context) {
            super();
            this.mTiltToleranceConfig = new int[][]{new int[]{-25, 70}, new int[]{-25, 65}, new int[]{-25, 60}, new int[]{-25, 65}};
            this.mTouchEndedTimestampNanos = Long.MIN_VALUE;
            this.mTiltHistory = new float[TILT_HISTORY_SIZE];
            this.mTiltHistoryTimestampNanos = new long[TILT_HISTORY_SIZE];
            int[] intArray = context.getResources().getIntArray(R.array.config_biometric_sensors);
            if (intArray.length != 8) {
                Slog.wtf(WindowOrientationListener.TAG, "config_autoRotationTiltTolerance should have exactly 8 elements");
                return;
            }
            for (int i = 0; i < 4; i++) {
                int i2 = i * 2;
                int i3 = intArray[i2];
                int i4 = intArray[i2 + 1];
                if (i3 >= -90 && i3 <= i4 && i4 <= 90) {
                    int[] iArr = this.mTiltToleranceConfig[i];
                    iArr[0] = i3;
                    iArr[1] = i4;
                } else {
                    Slog.wtf(WindowOrientationListener.TAG, "config_autoRotationTiltTolerance contains invalid range: min=" + i3 + ", max=" + i4);
                }
            }
        }

        @Override // com.android.server.wm.WindowOrientationListener.OrientationJudge
        public int getProposedRotationLocked() {
            return this.mProposedRotation;
        }

        @Override // com.android.server.wm.WindowOrientationListener.OrientationJudge
        public void dumpLocked(PrintWriter printWriter, String str) {
            printWriter.println(str + "AccelSensorJudge");
            String str2 = str + "  ";
            printWriter.println(str2 + "mProposedRotation=" + this.mProposedRotation);
            printWriter.println(str2 + "mPredictedRotation=" + this.mPredictedRotation);
            printWriter.println(str2 + "mLastFilteredX=" + this.mLastFilteredX);
            printWriter.println(str2 + "mLastFilteredY=" + this.mLastFilteredY);
            printWriter.println(str2 + "mLastFilteredZ=" + this.mLastFilteredZ);
            printWriter.println(str2 + "mLastFilteredTimestampNanos=" + this.mLastFilteredTimestampNanos + " (" + (((float) (SystemClock.elapsedRealtimeNanos() - this.mLastFilteredTimestampNanos)) * 1.0E-6f) + "ms ago)");
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append("mTiltHistory={last: ");
            sb.append(getLastTiltLocked());
            sb.append("}");
            printWriter.println(sb.toString());
            printWriter.println(str2 + "mFlat=" + this.mFlat);
            printWriter.println(str2 + "mSwinging=" + this.mSwinging);
            printWriter.println(str2 + "mAccelerating=" + this.mAccelerating);
            printWriter.println(str2 + "mOverhead=" + this.mOverhead);
            printWriter.println(str2 + "mTouched=" + this.mTouched);
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str2);
            sb2.append("mTiltToleranceConfig=[");
            printWriter.print(sb2.toString());
            for (int i = 0; i < 4; i++) {
                if (i != 0) {
                    printWriter.print(", ");
                }
                printWriter.print("[");
                printWriter.print(this.mTiltToleranceConfig[i][0]);
                printWriter.print(", ");
                printWriter.print(this.mTiltToleranceConfig[i][1]);
                printWriter.print("]");
            }
            printWriter.println("]");
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:24:0x00ef A[Catch: all -> 0x0342, TryCatch #0 {, blocks: (B:4:0x000b, B:6:0x001c, B:7:0x0057, B:9:0x0066, B:18:0x007c, B:20:0x0097, B:22:0x00e5, B:24:0x00ef, B:26:0x0103, B:28:0x0109, B:29:0x0110, B:30:0x0115, B:32:0x011b, B:33:0x0120, B:35:0x013b, B:36:0x0140, B:38:0x0146, B:41:0x014f, B:42:0x0158, B:44:0x015c, B:46:0x0162, B:47:0x0178, B:50:0x023f, B:52:0x024b, B:54:0x0255, B:56:0x025d, B:57:0x0313, B:69:0x0251, B:70:0x017f, B:72:0x0187, B:74:0x018d, B:75:0x01a3, B:76:0x01a7, B:78:0x01b9, B:79:0x01bb, B:82:0x01c4, B:84:0x01ca, B:86:0x01d0, B:88:0x01d9, B:89:0x0213, B:91:0x0219, B:92:0x0237, B:96:0x0156, B:101:0x00d4, B:103:0x00da, B:104:0x00e1), top: B:3:0x000b }] */
        /* JADX WARN: Removed duplicated region for block: B:52:0x024b A[Catch: all -> 0x0342, TryCatch #0 {, blocks: (B:4:0x000b, B:6:0x001c, B:7:0x0057, B:9:0x0066, B:18:0x007c, B:20:0x0097, B:22:0x00e5, B:24:0x00ef, B:26:0x0103, B:28:0x0109, B:29:0x0110, B:30:0x0115, B:32:0x011b, B:33:0x0120, B:35:0x013b, B:36:0x0140, B:38:0x0146, B:41:0x014f, B:42:0x0158, B:44:0x015c, B:46:0x0162, B:47:0x0178, B:50:0x023f, B:52:0x024b, B:54:0x0255, B:56:0x025d, B:57:0x0313, B:69:0x0251, B:70:0x017f, B:72:0x0187, B:74:0x018d, B:75:0x01a3, B:76:0x01a7, B:78:0x01b9, B:79:0x01bb, B:82:0x01c4, B:84:0x01ca, B:86:0x01d0, B:88:0x01d9, B:89:0x0213, B:91:0x0219, B:92:0x0237, B:96:0x0156, B:101:0x00d4, B:103:0x00da, B:104:0x00e1), top: B:3:0x000b }] */
        /* JADX WARN: Removed duplicated region for block: B:56:0x025d A[Catch: all -> 0x0342, TryCatch #0 {, blocks: (B:4:0x000b, B:6:0x001c, B:7:0x0057, B:9:0x0066, B:18:0x007c, B:20:0x0097, B:22:0x00e5, B:24:0x00ef, B:26:0x0103, B:28:0x0109, B:29:0x0110, B:30:0x0115, B:32:0x011b, B:33:0x0120, B:35:0x013b, B:36:0x0140, B:38:0x0146, B:41:0x014f, B:42:0x0158, B:44:0x015c, B:46:0x0162, B:47:0x0178, B:50:0x023f, B:52:0x024b, B:54:0x0255, B:56:0x025d, B:57:0x0313, B:69:0x0251, B:70:0x017f, B:72:0x0187, B:74:0x018d, B:75:0x01a3, B:76:0x01a7, B:78:0x01b9, B:79:0x01bb, B:82:0x01c4, B:84:0x01ca, B:86:0x01d0, B:88:0x01d9, B:89:0x0213, B:91:0x0219, B:92:0x0237, B:96:0x0156, B:101:0x00d4, B:103:0x00da, B:104:0x00e1), top: B:3:0x000b }] */
        @Override // com.android.server.wm.WindowOrientationListener.OrientationJudge, android.hardware.SensorEventListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onSensorChanged(SensorEvent sensorEvent) {
            byte b;
            boolean z;
            boolean z2;
            int i;
            int i2;
            boolean z3;
            boolean z4;
            synchronized (WindowOrientationListener.this.mLock) {
                float[] fArr = sensorEvent.values;
                boolean z5 = false;
                int i3 = 0;
                float f = fArr[0];
                float f2 = fArr[1];
                float f3 = fArr[2];
                if (WindowOrientationListener.LOG) {
                    Slog.v(WindowOrientationListener.TAG, "Raw acceleration vector: x=" + f + ", y=" + f2 + ", z=" + f3 + ", magnitude=" + Math.sqrt((f * f) + (f2 * f2) + (f3 * f3)));
                }
                long j = sensorEvent.timestamp;
                long j2 = this.mLastFilteredTimestampNanos;
                float f4 = ((float) (j - j2)) * 1.0E-6f;
                if (j >= j2 && j <= j2 + 1000000000 && (f != 0.0f || f2 != 0.0f || f3 != 0.0f)) {
                    float f5 = f4 / (FILTER_TIME_CONSTANT_MS + f4);
                    float f6 = this.mLastFilteredX;
                    f = ((f - f6) * f5) + f6;
                    float f7 = this.mLastFilteredY;
                    f2 = ((f2 - f7) * f5) + f7;
                    float f8 = this.mLastFilteredZ;
                    f3 = (f5 * (f3 - f8)) + f8;
                    if (WindowOrientationListener.LOG) {
                        Slog.v(WindowOrientationListener.TAG, "Filtered acceleration vector: x=" + f + ", y=" + f2 + ", z=" + f3 + ", magnitude=" + Math.sqrt((f * f) + (f2 * f2) + (f3 * f3)));
                    }
                    b = false;
                    this.mLastFilteredTimestampNanos = j;
                    this.mLastFilteredX = f;
                    this.mLastFilteredY = f2;
                    this.mLastFilteredZ = f3;
                    if (b == false) {
                        float sqrt = (float) Math.sqrt((f * f) + (f2 * f2) + (f3 * f3));
                        if (sqrt < 1.0f) {
                            if (WindowOrientationListener.LOG) {
                                Slog.v(WindowOrientationListener.TAG, "Ignoring sensor data, magnitude too close to zero.");
                            }
                            clearPredictedRotationLocked();
                        } else {
                            if (isAcceleratingLocked(sqrt)) {
                                this.mAccelerationTimestampNanos = j;
                                z2 = true;
                            } else {
                                z2 = false;
                            }
                            int round = (int) Math.round(Math.asin(f3 / sqrt) * 57.295780181884766d);
                            float f9 = round;
                            addTiltHistoryEntryLocked(j, f9);
                            if (isFlatLocked(j)) {
                                this.mFlatTimestampNanos = j;
                                z3 = true;
                            } else {
                                z3 = false;
                            }
                            if (isSwingingLocked(j, f9)) {
                                this.mSwingTimestampNanos = j;
                                z = true;
                            } else {
                                z = false;
                            }
                            if (round <= TILT_OVERHEAD_ENTER) {
                                this.mOverhead = true;
                            } else if (round >= TILT_OVERHEAD_EXIT) {
                                this.mOverhead = false;
                            }
                            if (this.mOverhead) {
                                if (WindowOrientationListener.LOG) {
                                    Slog.v(WindowOrientationListener.TAG, "Ignoring sensor data, device is overhead: tiltAngle=" + round);
                                }
                                clearPredictedRotationLocked();
                            } else if (Math.abs(round) > 80) {
                                if (WindowOrientationListener.LOG) {
                                    Slog.v(WindowOrientationListener.TAG, "Ignoring sensor data, tilt angle too high: tiltAngle=" + round);
                                }
                                clearPredictedRotationLocked();
                            } else {
                                z4 = z3;
                                int round2 = (int) Math.round((-Math.atan2(-f, f2)) * 57.295780181884766d);
                                if (round2 < 0) {
                                    round2 += 360;
                                }
                                int i4 = (round2 + 45) / 90;
                                if (i4 != 4) {
                                    i3 = i4;
                                }
                                if (isTiltAngleAcceptableLocked(i3, round) && isOrientationAngleAcceptableLocked(i3, round2)) {
                                    updatePredictedRotationLocked(j, i3);
                                    if (WindowOrientationListener.LOG) {
                                        Slog.v(WindowOrientationListener.TAG, "Predicted: tiltAngle=" + round + ", orientationAngle=" + round2 + ", predictedRotation=" + this.mPredictedRotation + ", predictedRotationAgeMS=" + (((float) (j - this.mPredictedRotationTimestampNanos)) * 1.0E-6f));
                                    }
                                } else {
                                    if (WindowOrientationListener.LOG) {
                                        Slog.v(WindowOrientationListener.TAG, "Ignoring sensor data, no predicted rotation: tiltAngle=" + round + ", orientationAngle=" + round2);
                                    }
                                    clearPredictedRotationLocked();
                                }
                                z5 = z4;
                                this.mFlat = z5;
                                this.mSwinging = z;
                                this.mAccelerating = z2;
                                i = this.mProposedRotation;
                                if (this.mPredictedRotation >= 0 || isPredictedRotationAcceptableLocked(j)) {
                                    this.mProposedRotation = this.mPredictedRotation;
                                }
                                i2 = this.mProposedRotation;
                                if (WindowOrientationListener.LOG) {
                                    Slog.v(WindowOrientationListener.TAG, "Result: currentRotation=" + WindowOrientationListener.this.mCurrentRotation + ", proposedRotation=" + i2 + ", predictedRotation=" + this.mPredictedRotation + ", timeDeltaMS=" + f4 + ", isAccelerating=" + z2 + ", isFlat=" + z5 + ", isSwinging=" + z + ", isOverhead=" + this.mOverhead + ", isTouched=" + this.mTouched + ", timeUntilSettledMS=" + remainingMS(j, this.mPredictedRotationTimestampNanos + PROPOSAL_SETTLE_TIME_NANOS) + ", timeUntilAccelerationDelayExpiredMS=" + remainingMS(j, this.mAccelerationTimestampNanos + 500000000) + ", timeUntilFlatDelayExpiredMS=" + remainingMS(j, this.mFlatTimestampNanos + 500000000) + ", timeUntilSwingDelayExpiredMS=" + remainingMS(j, this.mSwingTimestampNanos + 300000000) + ", timeUntilTouchDelayExpiredMS=" + remainingMS(j, this.mTouchEndedTimestampNanos + 500000000));
                                }
                            }
                            z4 = z3;
                            z5 = z4;
                            this.mFlat = z5;
                            this.mSwinging = z;
                            this.mAccelerating = z2;
                            i = this.mProposedRotation;
                            if (this.mPredictedRotation >= 0) {
                            }
                            this.mProposedRotation = this.mPredictedRotation;
                            i2 = this.mProposedRotation;
                            if (WindowOrientationListener.LOG) {
                            }
                        }
                    }
                    z = false;
                    z2 = false;
                    this.mFlat = z5;
                    this.mSwinging = z;
                    this.mAccelerating = z2;
                    i = this.mProposedRotation;
                    if (this.mPredictedRotation >= 0) {
                    }
                    this.mProposedRotation = this.mPredictedRotation;
                    i2 = this.mProposedRotation;
                    if (WindowOrientationListener.LOG) {
                    }
                }
                if (WindowOrientationListener.LOG) {
                    Slog.v(WindowOrientationListener.TAG, "Resetting orientation listener.");
                }
                resetLocked(true);
                b = true;
                this.mLastFilteredTimestampNanos = j;
                this.mLastFilteredX = f;
                this.mLastFilteredY = f2;
                this.mLastFilteredZ = f3;
                if (b == false) {
                }
                z = false;
                z2 = false;
                this.mFlat = z5;
                this.mSwinging = z;
                this.mAccelerating = z2;
                i = this.mProposedRotation;
                if (this.mPredictedRotation >= 0) {
                }
                this.mProposedRotation = this.mPredictedRotation;
                i2 = this.mProposedRotation;
                if (WindowOrientationListener.LOG) {
                }
            }
            if (i2 == i || i2 < 0) {
                return;
            }
            if (WindowOrientationListener.LOG) {
                Slog.v(WindowOrientationListener.TAG, "Proposed rotation changed!  proposedRotation=" + i2 + ", oldProposedRotation=" + i);
            }
            WindowOrientationListener.this.onProposedRotationChanged(i2);
        }

        @Override // com.android.server.wm.WindowOrientationListener.OrientationJudge
        public void onTouchStartLocked() {
            this.mTouched = true;
        }

        @Override // com.android.server.wm.WindowOrientationListener.OrientationJudge
        public void onTouchEndLocked(long j) {
            this.mTouched = false;
            this.mTouchEndedTimestampNanos = j;
        }

        @Override // com.android.server.wm.WindowOrientationListener.OrientationJudge
        public void resetLocked(boolean z) {
            this.mLastFilteredTimestampNanos = Long.MIN_VALUE;
            if (z) {
                this.mProposedRotation = -1;
            }
            this.mFlatTimestampNanos = Long.MIN_VALUE;
            this.mFlat = false;
            this.mSwingTimestampNanos = Long.MIN_VALUE;
            this.mSwinging = false;
            this.mAccelerationTimestampNanos = Long.MIN_VALUE;
            this.mAccelerating = false;
            this.mOverhead = false;
            clearPredictedRotationLocked();
            clearTiltHistoryLocked();
        }

        private boolean isTiltAngleAcceptableLocked(int i, int i2) {
            int[] iArr = this.mTiltToleranceConfig[i];
            return i2 >= iArr[0] && i2 <= iArr[1];
        }

        private boolean isOrientationAngleAcceptableLocked(int i, int i2) {
            int i3 = WindowOrientationListener.this.mCurrentRotation;
            if (i3 < 0) {
                return true;
            }
            if (i == i3 || i == (i3 + 1) % 4) {
                int i4 = ((i * 90) - 45) + 22;
                if (i == 0) {
                    if (i2 >= 315 && i2 < i4 + 360) {
                        return false;
                    }
                } else if (i2 < i4) {
                    return false;
                }
            }
            if (i != i3 && i != (i3 + 3) % 4) {
                return true;
            }
            int i5 = ((i * 90) + 45) - 22;
            return i == 0 ? i2 > 45 || i2 <= i5 : i2 <= i5;
        }

        private boolean isPredictedRotationAcceptableLocked(long j) {
            return j >= this.mPredictedRotationTimestampNanos + PROPOSAL_SETTLE_TIME_NANOS && j >= this.mFlatTimestampNanos + 500000000 && j >= this.mSwingTimestampNanos + 300000000 && j >= this.mAccelerationTimestampNanos + 500000000 && !this.mTouched && j >= this.mTouchEndedTimestampNanos + 500000000;
        }

        private void clearPredictedRotationLocked() {
            this.mPredictedRotation = -1;
            this.mPredictedRotationTimestampNanos = Long.MIN_VALUE;
        }

        private void updatePredictedRotationLocked(long j, int i) {
            if (this.mPredictedRotation != i) {
                this.mPredictedRotation = i;
                this.mPredictedRotationTimestampNanos = j;
            }
        }

        private void clearTiltHistoryLocked() {
            this.mTiltHistoryTimestampNanos[0] = Long.MIN_VALUE;
            this.mTiltHistoryIndex = 1;
        }

        private void addTiltHistoryEntryLocked(long j, float f) {
            float[] fArr = this.mTiltHistory;
            int i = this.mTiltHistoryIndex;
            fArr[i] = f;
            long[] jArr = this.mTiltHistoryTimestampNanos;
            jArr[i] = j;
            int i2 = (i + 1) % TILT_HISTORY_SIZE;
            this.mTiltHistoryIndex = i2;
            jArr[i2] = Long.MIN_VALUE;
        }

        private boolean isFlatLocked(long j) {
            int i = this.mTiltHistoryIndex;
            do {
                i = nextTiltHistoryIndexLocked(i);
                if (i < 0 || this.mTiltHistory[i] < FLAT_ANGLE) {
                    return false;
                }
            } while (this.mTiltHistoryTimestampNanos[i] + 1000000000 > j);
            return true;
        }

        private boolean isSwingingLocked(long j, float f) {
            int i = this.mTiltHistoryIndex;
            do {
                i = nextTiltHistoryIndexLocked(i);
                if (i < 0 || this.mTiltHistoryTimestampNanos[i] + 300000000 < j) {
                    return false;
                }
            } while (this.mTiltHistory[i] + SWING_AWAY_ANGLE_DELTA > f);
            return true;
        }

        private int nextTiltHistoryIndexLocked(int i) {
            if (i == 0) {
                i = TILT_HISTORY_SIZE;
            }
            int i2 = i - 1;
            if (this.mTiltHistoryTimestampNanos[i2] != Long.MIN_VALUE) {
                return i2;
            }
            return -1;
        }

        private float getLastTiltLocked() {
            int nextTiltHistoryIndexLocked = nextTiltHistoryIndexLocked(this.mTiltHistoryIndex);
            if (nextTiltHistoryIndexLocked >= 0) {
                return this.mTiltHistory[nextTiltHistoryIndexLocked];
            }
            return Float.NaN;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public final class OrientationSensorJudge extends OrientationJudge {
        private static final int ROTATION_UNSET = -1;
        private final ActivityTaskManagerInternal mActivityTaskManagerInternal;
        private Runnable mCancelRotationResolverRequest;
        private int mCurrentCallbackId;
        private int mDesiredRotation;
        private int mLastRotationResolution;
        private long mLastRotationResolutionTimeStamp;
        private int mProposedRotation;
        private boolean mRotationEvaluationScheduled;
        private Runnable mRotationEvaluator;
        private long mRotationMemorizationTimeoutMillis;
        private long mRotationResolverTimeoutMillis;
        private long mTouchEndedTimestampNanos;
        private boolean mTouching;

        private int rotationToLogEnum(int i) {
            if (i == 0) {
                return 1;
            }
            if (i == 1) {
                return 2;
            }
            if (i != 2) {
                return i != 3 ? 0 : 4;
            }
            return 3;
        }

        @Override // com.android.server.wm.WindowOrientationListener.OrientationJudge, android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        OrientationSensorJudge() {
            super();
            this.mTouchEndedTimestampNanos = Long.MIN_VALUE;
            this.mProposedRotation = -1;
            this.mDesiredRotation = -1;
            this.mLastRotationResolution = -1;
            this.mCurrentCallbackId = 0;
            this.mRotationEvaluator = new Runnable() { // from class: com.android.server.wm.WindowOrientationListener.OrientationSensorJudge.2
                @Override // java.lang.Runnable
                public void run() {
                    int evaluateRotationChangeLocked;
                    synchronized (WindowOrientationListener.this.mLock) {
                        OrientationSensorJudge.this.mRotationEvaluationScheduled = false;
                        evaluateRotationChangeLocked = OrientationSensorJudge.this.evaluateRotationChangeLocked();
                    }
                    if (evaluateRotationChangeLocked >= 0) {
                        WindowOrientationListener.this.onProposedRotationChanged(evaluateRotationChangeLocked);
                    }
                }
            };
            setupRotationResolverParameters();
            this.mActivityTaskManagerInternal = (ActivityTaskManagerInternal) LocalServices.getService(ActivityTaskManagerInternal.class);
        }

        private void setupRotationResolverParameters() {
            DeviceConfig.addOnPropertiesChangedListener("window_manager", ActivityThread.currentApplication().getMainExecutor(), new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.wm.WindowOrientationListener$OrientationSensorJudge$$ExternalSyntheticLambda0
                public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                    WindowOrientationListener.OrientationSensorJudge.this.lambda$setupRotationResolverParameters$0(properties);
                }
            });
            readRotationResolverParameters();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setupRotationResolverParameters$0(DeviceConfig.Properties properties) {
            Set keyset = properties.getKeyset();
            if (keyset.contains(WindowOrientationListener.KEY_ROTATION_RESOLVER_TIMEOUT) || keyset.contains(WindowOrientationListener.KEY_ROTATION_MEMORIZATION_TIMEOUT)) {
                readRotationResolverParameters();
            }
        }

        private void readRotationResolverParameters() {
            this.mRotationResolverTimeoutMillis = DeviceConfig.getLong("window_manager", WindowOrientationListener.KEY_ROTATION_RESOLVER_TIMEOUT, WindowOrientationListener.DEFAULT_ROTATION_RESOLVER_TIMEOUT_MILLIS);
            this.mRotationMemorizationTimeoutMillis = DeviceConfig.getLong("window_manager", WindowOrientationListener.KEY_ROTATION_MEMORIZATION_TIMEOUT, WindowOrientationListener.DEFAULT_ROTATION_MEMORIZATION_TIMEOUT_MILLIS);
        }

        @Override // com.android.server.wm.WindowOrientationListener.OrientationJudge
        public int getProposedRotationLocked() {
            return this.mProposedRotation;
        }

        @Override // com.android.server.wm.WindowOrientationListener.OrientationJudge
        public void onTouchStartLocked() {
            this.mTouching = true;
        }

        @Override // com.android.server.wm.WindowOrientationListener.OrientationJudge
        public void onTouchEndLocked(long j) {
            this.mTouching = false;
            this.mTouchEndedTimestampNanos = j;
            if (this.mDesiredRotation != this.mProposedRotation) {
                scheduleRotationEvaluationIfNecessaryLocked(SystemClock.elapsedRealtimeNanos());
            }
        }

        @Override // com.android.server.wm.WindowOrientationListener.OrientationJudge, android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent sensorEvent) {
            String str;
            WindowProcessController topApp;
            ApplicationInfo applicationInfo;
            final int i = (int) sensorEvent.values[0];
            if (i == 4) {
                Slog.v(WindowOrientationListener.TAG, "onSensorChanged mDesiredRotation = 4");
                resetLocked(true);
                return;
            }
            if (i < 0 || i > 3) {
                return;
            }
            if (ActivityTaskManagerDebugConfig.DEBUG_AMS) {
                Slog.v(WindowOrientationListener.TAG, "onSensorChanged mDesiredRotation = " + i);
            }
            FrameworkStatsLog.write(333, sensorEvent.timestamp, rotationToLogEnum(i), 2);
            if (WindowOrientationListener.this.isRotationResolverEnabled()) {
                if (ActivityTaskManagerDebugConfig.DEBUG_AMS) {
                    Slog.v(WindowOrientationListener.TAG, "isRotationResolverEnabled() = trueisKeyguardShowingAndNotOccluded() = " + WindowOrientationListener.this.isKeyguardShowingAndNotOccluded());
                }
                if (WindowOrientationListener.this.isKeyguardShowingAndNotOccluded()) {
                    if (this.mLastRotationResolution != -1 && SystemClock.uptimeMillis() - this.mLastRotationResolutionTimeStamp < this.mRotationMemorizationTimeoutMillis) {
                        Slog.d(WindowOrientationListener.TAG, "Reusing the last rotation resolution: " + this.mLastRotationResolution);
                        finalizeRotation(this.mLastRotationResolution);
                        return;
                    }
                    finalizeRotation(WindowOrientationListener.this.mDefaultRotation);
                    return;
                }
                WindowOrientationListener windowOrientationListener = WindowOrientationListener.this;
                if (windowOrientationListener.mRotationResolverService == null) {
                    windowOrientationListener.mRotationResolverService = (RotationResolverInternal) LocalServices.getService(RotationResolverInternal.class);
                    if (WindowOrientationListener.this.mRotationResolverService == null) {
                        finalizeRotation(i);
                        return;
                    }
                }
                if (ActivityTaskManagerDebugConfig.DEBUG_AMS && WindowOrientationListener.this.mRotationResolverService == null) {
                    Slog.v(WindowOrientationListener.TAG, "mRotationResolverService = null");
                }
                ActivityTaskManagerInternal activityTaskManagerInternal = this.mActivityTaskManagerInternal;
                if (activityTaskManagerInternal == null || (topApp = activityTaskManagerInternal.getTopApp()) == null || (applicationInfo = topApp.mInfo) == null || (str = applicationInfo.packageName) == null) {
                    str = null;
                }
                String str2 = str;
                this.mCurrentCallbackId++;
                if (this.mCancelRotationResolverRequest != null) {
                    WindowOrientationListener.this.getHandler().removeCallbacks(this.mCancelRotationResolverRequest);
                }
                final CancellationSignal cancellationSignal = new CancellationSignal();
                this.mCancelRotationResolverRequest = new Runnable() { // from class: com.android.server.wm.WindowOrientationListener$OrientationSensorJudge$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        cancellationSignal.cancel();
                    }
                };
                WindowOrientationListener.this.getHandler().postDelayed(this.mCancelRotationResolverRequest, this.mRotationResolverTimeoutMillis);
                WindowOrientationListener windowOrientationListener2 = WindowOrientationListener.this;
                RotationResolverInternal rotationResolverInternal = windowOrientationListener2.mRotationResolverService;
                if (rotationResolverInternal == null) {
                    windowOrientationListener2.getHandler().removeCallbacks(this.mCancelRotationResolverRequest);
                    finalizeRotation(i);
                    return;
                } else {
                    rotationResolverInternal.resolveRotation(new RotationResolverInternal.RotationResolverCallbackInternal() { // from class: com.android.server.wm.WindowOrientationListener.OrientationSensorJudge.1
                        private final int mCallbackId;

                        {
                            this.mCallbackId = OrientationSensorJudge.this.mCurrentCallbackId;
                        }

                        public void onSuccess(int i2) {
                            finalizeRotationIfFresh(i2);
                        }

                        public void onFailure(int i2) {
                            finalizeRotationIfFresh(i);
                        }

                        private void finalizeRotationIfFresh(int i2) {
                            if (this.mCallbackId == OrientationSensorJudge.this.mCurrentCallbackId) {
                                WindowOrientationListener.this.getHandler().removeCallbacks(OrientationSensorJudge.this.mCancelRotationResolverRequest);
                                OrientationSensorJudge.this.finalizeRotation(i2);
                            } else {
                                Slog.d(WindowOrientationListener.TAG, String.format("An outdated callback received [%s vs. %s]. Ignoring it.", Integer.valueOf(this.mCallbackId), Integer.valueOf(OrientationSensorJudge.this.mCurrentCallbackId)));
                            }
                        }
                    }, str2, i, WindowOrientationListener.this.mCurrentRotation, this.mRotationResolverTimeoutMillis, cancellationSignal);
                    return;
                }
            }
            finalizeRotation(i);
        }

        @Override // com.android.server.wm.WindowOrientationListener.OrientationJudge
        public void dumpLocked(PrintWriter printWriter, String str) {
            printWriter.println(str + "OrientationSensorJudge");
            String str2 = str + "  ";
            printWriter.println(str2 + "mDesiredRotation=" + Surface.rotationToString(this.mDesiredRotation));
            printWriter.println(str2 + "mProposedRotation=" + Surface.rotationToString(this.mProposedRotation));
            printWriter.println(str2 + "mTouching=" + this.mTouching);
            printWriter.println(str2 + "mTouchEndedTimestampNanos=" + this.mTouchEndedTimestampNanos);
            printWriter.println(str2 + "mLastRotationResolution=" + this.mLastRotationResolution);
        }

        @Override // com.android.server.wm.WindowOrientationListener.OrientationJudge
        public void resetLocked(boolean z) {
            if (z) {
                this.mProposedRotation = -1;
                this.mDesiredRotation = -1;
            }
            this.mTouching = false;
            this.mTouchEndedTimestampNanos = Long.MIN_VALUE;
            unscheduleRotationEvaluationLocked();
        }

        public int evaluateRotationChangeLocked() {
            unscheduleRotationEvaluationLocked();
            if (this.mDesiredRotation == this.mProposedRotation) {
                return -1;
            }
            long elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
            if (isDesiredRotationAcceptableLocked(elapsedRealtimeNanos)) {
                int i = this.mDesiredRotation;
                this.mProposedRotation = i;
                return i;
            }
            scheduleRotationEvaluationIfNecessaryLocked(elapsedRealtimeNanos);
            return -1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void finalizeRotation(int i) {
            int evaluateRotationChangeLocked;
            synchronized (WindowOrientationListener.this.mLock) {
                this.mDesiredRotation = i;
                evaluateRotationChangeLocked = evaluateRotationChangeLocked();
            }
            if (evaluateRotationChangeLocked >= 0) {
                this.mLastRotationResolution = evaluateRotationChangeLocked;
                this.mLastRotationResolutionTimeStamp = SystemClock.uptimeMillis();
                WindowOrientationListener.this.onProposedRotationChanged(evaluateRotationChangeLocked);
            }
        }

        private boolean isDesiredRotationAcceptableLocked(long j) {
            return !this.mTouching && j >= this.mTouchEndedTimestampNanos + 500000000;
        }

        private void scheduleRotationEvaluationIfNecessaryLocked(long j) {
            if (this.mRotationEvaluationScheduled || this.mDesiredRotation == this.mProposedRotation) {
                if (WindowOrientationListener.LOG) {
                    Slog.d(WindowOrientationListener.TAG, "scheduleRotationEvaluationLocked: ignoring, an evaluation is already scheduled or is unnecessary.");
                }
            } else {
                if (this.mTouching) {
                    if (WindowOrientationListener.LOG) {
                        Slog.d(WindowOrientationListener.TAG, "scheduleRotationEvaluationLocked: ignoring, user is still touching the screen.");
                        return;
                    }
                    return;
                }
                if (j >= this.mTouchEndedTimestampNanos + 500000000) {
                    if (WindowOrientationListener.LOG) {
                        Slog.d(WindowOrientationListener.TAG, "scheduleRotationEvaluationLocked: ignoring, already past the next possible time of rotation.");
                    }
                } else {
                    WindowOrientationListener.this.mHandler.postDelayed(this.mRotationEvaluator, (long) Math.ceil(((float) (r2 - j)) * 1.0E-6f));
                    this.mRotationEvaluationScheduled = true;
                }
            }
        }

        private void unscheduleRotationEvaluationLocked() {
            if (this.mRotationEvaluationScheduled) {
                WindowOrientationListener.this.mHandler.removeCallbacks(this.mRotationEvaluator);
                this.mRotationEvaluationScheduled = false;
            }
        }
    }
}
