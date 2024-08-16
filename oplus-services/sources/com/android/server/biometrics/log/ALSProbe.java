package com.android.server.biometrics.log;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.biometrics.log.ALSProbe;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class ALSProbe implements Probe {
    private static final String TAG = "ALSProbe";
    private boolean mDestroyRequested;
    private boolean mDestroyed;
    private boolean mDisableRequested;
    private boolean mEnabled;
    private volatile float mLastAmbientLux;
    private final Sensor mLightSensor;
    private final SensorEventListener mLightSensorListener;
    private long mMaxSubscriptionTime;
    private NextConsumer mNextConsumer;
    private final SensorManager mSensorManager;
    private final Handler mTimer;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ALSProbe(SensorManager sensorManager) {
        this(sensorManager, new Handler(Looper.getMainLooper()), TimeUnit.MINUTES.toMillis(1L));
    }

    @VisibleForTesting
    ALSProbe(SensorManager sensorManager, Handler handler, long j) {
        this.mMaxSubscriptionTime = -1L;
        this.mEnabled = false;
        this.mDestroyed = false;
        this.mDestroyRequested = false;
        this.mDisableRequested = false;
        this.mNextConsumer = null;
        this.mLastAmbientLux = -1.0f;
        this.mLightSensorListener = new SensorEventListener() { // from class: com.android.server.biometrics.log.ALSProbe.1
            @Override // android.hardware.SensorEventListener
            public void onAccuracyChanged(Sensor sensor, int i) {
            }

            @Override // android.hardware.SensorEventListener
            public void onSensorChanged(SensorEvent sensorEvent) {
                ALSProbe.this.onNext(sensorEvent.values[0]);
            }
        };
        this.mSensorManager = sensorManager;
        Sensor defaultSensor = sensorManager != null ? sensorManager.getDefaultSensor(5) : null;
        this.mLightSensor = defaultSensor;
        this.mTimer = handler;
        this.mMaxSubscriptionTime = j;
        if (sensorManager == null || defaultSensor == null) {
            Slog.w(TAG, "No sensor - probe disabled");
            this.mDestroyed = true;
        }
    }

    @Override // com.android.server.biometrics.log.Probe
    public synchronized void enable() {
        if (!this.mDestroyed && !this.mDestroyRequested) {
            this.mDisableRequested = false;
            enableLightSensorLoggingLocked();
        }
    }

    @Override // com.android.server.biometrics.log.Probe
    public synchronized void disable() {
        this.mDisableRequested = true;
        if (!this.mDestroyed && this.mNextConsumer == null) {
            disableLightSensorLoggingLocked(false);
        }
    }

    @Override // com.android.server.biometrics.log.Probe
    public synchronized void destroy() {
        this.mDestroyRequested = true;
        if (!this.mDestroyed && this.mNextConsumer == null) {
            disableLightSensorLoggingLocked(true);
            this.mDestroyed = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void onNext(float f) {
        this.mLastAmbientLux = f;
        NextConsumer nextConsumer = this.mNextConsumer;
        this.mNextConsumer = null;
        if (nextConsumer != null) {
            Slog.v(TAG, "Finishing next consumer");
            if (this.mDestroyRequested) {
                destroy();
            } else if (this.mDisableRequested) {
                disable();
            }
            nextConsumer.consume(f);
        }
    }

    public float getMostRecentLux() {
        return this.mLastAmbientLux;
    }

    public synchronized void awaitNextLux(Consumer<Float> consumer, Handler handler) {
        NextConsumer nextConsumer = new NextConsumer(consumer, handler);
        float f = this.mLastAmbientLux;
        if (f > -1.0f) {
            nextConsumer.consume(f);
        } else {
            NextConsumer nextConsumer2 = this.mNextConsumer;
            if (nextConsumer2 != null) {
                nextConsumer2.add(nextConsumer);
            } else {
                this.mDestroyed = false;
                this.mNextConsumer = nextConsumer;
                enableLightSensorLoggingLocked();
            }
        }
    }

    private void enableLightSensorLoggingLocked() {
        if (!this.mEnabled) {
            this.mEnabled = true;
            this.mLastAmbientLux = -1.0f;
            this.mSensorManager.registerListener(this.mLightSensorListener, this.mLightSensor, 3);
            Slog.v(TAG, "Enable ALS: " + this.mLightSensorListener.hashCode());
        }
        resetTimerLocked(true);
    }

    private void disableLightSensorLoggingLocked(boolean z) {
        resetTimerLocked(false);
        if (this.mEnabled) {
            this.mEnabled = false;
            if (!z) {
                this.mLastAmbientLux = -1.0f;
            }
            this.mSensorManager.unregisterListener(this.mLightSensorListener);
            Slog.v(TAG, "Disable ALS: " + this.mLightSensorListener.hashCode());
        }
    }

    private void resetTimerLocked(boolean z) {
        this.mTimer.removeCallbacksAndMessages(this);
        if (!z || this.mMaxSubscriptionTime <= 0) {
            return;
        }
        this.mTimer.postDelayed(new Runnable() { // from class: com.android.server.biometrics.log.ALSProbe$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ALSProbe.this.onTimeout();
            }
        }, this, this.mMaxSubscriptionTime);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void onTimeout() {
        Slog.e(TAG, "Max time exceeded for ALS logger - disabling: " + this.mLightSensorListener.hashCode());
        onNext(this.mLastAmbientLux);
        disable();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class NextConsumer {
        private final Consumer<Float> mConsumer;
        private final Handler mHandler;
        private final List<NextConsumer> mOthers;

        private NextConsumer(Consumer<Float> consumer, Handler handler) {
            this.mOthers = new ArrayList();
            this.mConsumer = consumer;
            this.mHandler = handler;
        }

        public void consume(final float f) {
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.post(new Runnable() { // from class: com.android.server.biometrics.log.ALSProbe$NextConsumer$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        ALSProbe.NextConsumer.this.lambda$consume$0(f);
                    }
                });
            } else {
                this.mConsumer.accept(Float.valueOf(f));
            }
            Iterator<NextConsumer> it = this.mOthers.iterator();
            while (it.hasNext()) {
                it.next().consume(f);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$consume$0(float f) {
            this.mConsumer.accept(Float.valueOf(f));
        }

        public void add(NextConsumer nextConsumer) {
            this.mOthers.add(nextConsumer);
        }
    }
}
