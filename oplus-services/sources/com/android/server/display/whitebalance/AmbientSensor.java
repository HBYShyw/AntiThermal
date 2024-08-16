package com.android.server.display.whitebalance;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Slog;
import com.android.server.display.utils.History;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
abstract class AmbientSensor {
    private static final int HISTORY_SIZE = 50;
    private boolean mEnabled;
    private int mEventsCount;
    private History mEventsHistory;
    private final Handler mHandler;
    private SensorEventListener mListener = new SensorEventListener() { // from class: com.android.server.display.whitebalance.AmbientSensor.1
        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent sensorEvent) {
            AmbientSensor.this.handleNewEvent(sensorEvent.values[0]);
        }
    };
    protected boolean mLoggingEnabled;
    private int mRate;
    protected Sensor mSensor;
    protected final SensorManager mSensorManager;
    protected String mTag;

    protected abstract void update(float f);

    AmbientSensor(String str, Handler handler, SensorManager sensorManager, int i) {
        validateArguments(handler, sensorManager, i);
        this.mTag = str;
        this.mLoggingEnabled = false;
        this.mHandler = handler;
        this.mSensorManager = sensorManager;
        this.mEnabled = false;
        this.mRate = i;
        this.mEventsCount = 0;
        this.mEventsHistory = new History(50);
    }

    public boolean setEnabled(boolean z) {
        if (z) {
            return enable();
        }
        return disable();
    }

    public boolean setLoggingEnabled(boolean z) {
        if (this.mLoggingEnabled == z) {
            return false;
        }
        this.mLoggingEnabled = z;
        return true;
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("  " + this.mTag);
        printWriter.println("    mLoggingEnabled=" + this.mLoggingEnabled);
        printWriter.println("    mHandler=" + this.mHandler);
        printWriter.println("    mSensorManager=" + this.mSensorManager);
        printWriter.println("    mSensor=" + this.mSensor);
        printWriter.println("    mEnabled=" + this.mEnabled);
        printWriter.println("    mRate=" + this.mRate);
        printWriter.println("    mEventsCount=" + this.mEventsCount);
        printWriter.println("    mEventsHistory=" + this.mEventsHistory);
    }

    private static void validateArguments(Handler handler, SensorManager sensorManager, int i) {
        Objects.requireNonNull(handler, "handler cannot be null");
        Objects.requireNonNull(sensorManager, "sensorManager cannot be null");
        if (i <= 0) {
            throw new IllegalArgumentException("rate must be positive");
        }
    }

    private boolean enable() {
        if (this.mEnabled) {
            return false;
        }
        if (this.mLoggingEnabled) {
            Slog.d(this.mTag, "enabling");
        }
        this.mEnabled = true;
        startListening();
        return true;
    }

    private boolean disable() {
        if (!this.mEnabled) {
            return false;
        }
        if (this.mLoggingEnabled) {
            Slog.d(this.mTag, "disabling");
        }
        this.mEnabled = false;
        this.mEventsCount = 0;
        stopListening();
        return true;
    }

    private void startListening() {
        SensorManager sensorManager = this.mSensorManager;
        if (sensorManager == null) {
            return;
        }
        sensorManager.registerListener(this.mListener, this.mSensor, this.mRate * 1000, this.mHandler);
    }

    private void stopListening() {
        SensorManager sensorManager = this.mSensorManager;
        if (sensorManager == null) {
            return;
        }
        sensorManager.unregisterListener(this.mListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNewEvent(float f) {
        if (this.mEnabled) {
            if (this.mLoggingEnabled) {
                Slog.d(this.mTag, "handle new event: " + f);
            }
            this.mEventsCount++;
            this.mEventsHistory.add(f);
            update(f);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    static class AmbientBrightnessSensor extends AmbientSensor {
        private static final String TAG = "AmbientBrightnessSensor";
        private Callbacks mCallbacks;

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        interface Callbacks {
            void onAmbientBrightnessChanged(float f);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public AmbientBrightnessSensor(Handler handler, SensorManager sensorManager, int i) {
            super(TAG, handler, sensorManager, i);
            Sensor defaultSensor = this.mSensorManager.getDefaultSensor(5);
            this.mSensor = defaultSensor;
            if (defaultSensor == null) {
                throw new IllegalStateException("cannot find light sensor");
            }
            this.mCallbacks = null;
        }

        public boolean setCallbacks(Callbacks callbacks) {
            if (this.mCallbacks == callbacks) {
                return false;
            }
            this.mCallbacks = callbacks;
            return true;
        }

        @Override // com.android.server.display.whitebalance.AmbientSensor
        public void dump(PrintWriter printWriter) {
            super.dump(printWriter);
            printWriter.println("    mCallbacks=" + this.mCallbacks);
        }

        @Override // com.android.server.display.whitebalance.AmbientSensor
        protected void update(float f) {
            Callbacks callbacks = this.mCallbacks;
            if (callbacks != null) {
                callbacks.onAmbientBrightnessChanged(f);
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    static class AmbientColorTemperatureSensor extends AmbientSensor {
        private static final String TAG = "AmbientColorTemperatureSensor";
        private Callbacks mCallbacks;

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        interface Callbacks {
            void onAmbientColorTemperatureChanged(float f);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public AmbientColorTemperatureSensor(Handler handler, SensorManager sensorManager, String str, int i) {
            super(TAG, handler, sensorManager, i);
            this.mSensor = null;
            Iterator<Sensor> it = this.mSensorManager.getSensorList(-1).iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Sensor next = it.next();
                if (next.getStringType().equals(str)) {
                    this.mSensor = next;
                    break;
                }
            }
            if (this.mSensor == null) {
                throw new IllegalStateException("cannot find sensor " + str);
            }
            this.mCallbacks = null;
        }

        public boolean setCallbacks(Callbacks callbacks) {
            if (this.mCallbacks == callbacks) {
                return false;
            }
            this.mCallbacks = callbacks;
            return true;
        }

        @Override // com.android.server.display.whitebalance.AmbientSensor
        public void dump(PrintWriter printWriter) {
            super.dump(printWriter);
            printWriter.println("    mCallbacks=" + this.mCallbacks);
        }

        @Override // com.android.server.display.whitebalance.AmbientSensor
        protected void update(float f) {
            Callbacks callbacks = this.mCallbacks;
            if (callbacks != null) {
                callbacks.onAmbientColorTemperatureChanged(f);
            }
        }
    }
}
