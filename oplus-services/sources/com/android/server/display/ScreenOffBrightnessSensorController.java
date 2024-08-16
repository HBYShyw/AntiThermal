package com.android.server.display;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.IndentingPrintWriter;
import com.android.internal.annotations.VisibleForTesting;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ScreenOffBrightnessSensorController implements SensorEventListener {
    private static final int SENSOR_INVALID_VALUE = -1;
    private static final long SENSOR_VALUE_VALID_TIME_MILLIS = 1500;
    private static final String TAG = "ScreenOffBrightnessSensorController";
    private final BrightnessMappingStrategy mBrightnessMapper;
    private final Clock mClock;
    private final Handler mHandler;
    private final Sensor mLightSensor;
    private boolean mRegistered;
    private final SensorManager mSensorManager;
    private final int[] mSensorValueToLux;
    private int mLastSensorValue = -1;
    private long mSensorDisableTime = -1;

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface Clock {
        long uptimeMillis();
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public ScreenOffBrightnessSensorController(SensorManager sensorManager, Sensor sensor, Handler handler, Clock clock, int[] iArr, BrightnessMappingStrategy brightnessMappingStrategy) {
        this.mSensorManager = sensorManager;
        this.mLightSensor = sensor;
        this.mHandler = handler;
        this.mClock = clock;
        this.mSensorValueToLux = iArr;
        this.mBrightnessMapper = brightnessMappingStrategy;
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (this.mRegistered) {
            this.mLastSensorValue = (int) sensorEvent.values[0];
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLightSensorEnabled(boolean z) {
        if (z && !this.mRegistered) {
            this.mRegistered = this.mSensorManager.registerListener(this, this.mLightSensor, 3, this.mHandler);
            this.mLastSensorValue = -1;
        } else {
            if (z || !this.mRegistered) {
                return;
            }
            this.mSensorManager.unregisterListener(this);
            this.mRegistered = false;
            this.mSensorDisableTime = this.mClock.uptimeMillis();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stop() {
        setLightSensorEnabled(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getAutomaticScreenBrightness() {
        int i;
        int i2 = this.mLastSensorValue;
        if (i2 < 0 || i2 >= this.mSensorValueToLux.length || ((!this.mRegistered && this.mClock.uptimeMillis() - this.mSensorDisableTime > SENSOR_VALUE_VALID_TIME_MILLIS) || (i = this.mSensorValueToLux[this.mLastSensorValue]) < 0)) {
            return Float.NaN;
        }
        return this.mBrightnessMapper.getBrightness(i);
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("Screen Off Brightness Sensor Controller:");
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter);
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("registered=" + this.mRegistered);
        indentingPrintWriter.println("lastSensorValue=" + this.mLastSensorValue);
    }
}
