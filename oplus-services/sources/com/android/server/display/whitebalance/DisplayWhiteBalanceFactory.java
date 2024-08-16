package com.android.server.display.whitebalance;

import android.R;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.TypedValue;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.display.utils.AmbientFilter;
import com.android.server.display.utils.AmbientFilterFactory;
import com.android.server.display.whitebalance.AmbientSensor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DisplayWhiteBalanceFactory {
    private static final String BRIGHTNESS_FILTER_TAG = "AmbientBrightnessFilter";
    private static final String COLOR_TEMPERATURE_FILTER_TAG = "AmbientColorTemperatureFilter";

    public static DisplayWhiteBalanceController create(Handler handler, SensorManager sensorManager, Resources resources) {
        AmbientSensor.AmbientBrightnessSensor createBrightnessSensor = createBrightnessSensor(handler, sensorManager, resources);
        AmbientFilter createBrightnessFilter = AmbientFilterFactory.createBrightnessFilter(BRIGHTNESS_FILTER_TAG, resources);
        AmbientSensor.AmbientColorTemperatureSensor createColorTemperatureSensor = createColorTemperatureSensor(handler, sensorManager, resources);
        DisplayWhiteBalanceController displayWhiteBalanceController = new DisplayWhiteBalanceController(createBrightnessSensor, createBrightnessFilter, createColorTemperatureSensor, AmbientFilterFactory.createColorTemperatureFilter(COLOR_TEMPERATURE_FILTER_TAG, resources), createThrottler(resources), getFloatArray(resources, R.array.config_networkNotifySwitches), getFloatArray(resources, R.array.config_mobile_tcp_buffers), getFloat(resources, R.dimen.config_screenBrightnessSettingDefaultFloat), getFloatArray(resources, R.array.config_minimumBrightnessCurveNits), getFloatArray(resources, R.array.config_minimumBrightnessCurveLux), getFloat(resources, R.dimen.config_screenBrightnessDozeFloat), getFloatArray(resources, R.array.config_integrityRuleProviderPackages), getFloatArray(resources, R.array.config_localPrivateDisplayPorts), getFloatArray(resources, R.array.config_networkSupportedKeepaliveCount), getFloatArray(resources, R.array.config_nightDisplayColorTemperatureCoefficients), resources.getBoolean(17891625));
        createBrightnessSensor.setCallbacks(displayWhiteBalanceController);
        createColorTemperatureSensor.setCallbacks(displayWhiteBalanceController);
        return displayWhiteBalanceController;
    }

    private DisplayWhiteBalanceFactory() {
    }

    @VisibleForTesting
    public static AmbientSensor.AmbientBrightnessSensor createBrightnessSensor(Handler handler, SensorManager sensorManager, Resources resources) {
        return new AmbientSensor.AmbientBrightnessSensor(handler, sensorManager, resources.getInteger(R.integer.config_keepPreloadsMinDays));
    }

    @VisibleForTesting
    public static AmbientSensor.AmbientColorTemperatureSensor createColorTemperatureSensor(Handler handler, SensorManager sensorManager, Resources resources) {
        return new AmbientSensor.AmbientColorTemperatureSensor(handler, sensorManager, resources.getString(R.string.config_useragentprofile_url), resources.getInteger(R.integer.config_lockSoundVolumeDb));
    }

    private static DisplayWhiteBalanceThrottler createThrottler(Resources resources) {
        return new DisplayWhiteBalanceThrottler(resources.getInteger(R.integer.config_longPressOnBackBehavior), resources.getInteger(R.integer.config_longPressOnPowerBehavior), getFloatArray(resources, R.array.config_jitzygoteBootImagePinnerServiceFiles), getFloatArray(resources, R.array.config_mobile_hotspot_provision_app), getFloatArray(resources, R.array.config_keyboardTapVibePattern));
    }

    private static float getFloat(Resources resources, int i) {
        TypedValue typedValue = new TypedValue();
        resources.getValue(i, typedValue, true);
        if (typedValue.type != 4) {
            return Float.NaN;
        }
        return typedValue.getFloat();
    }

    private static float[] getFloatArray(Resources resources, int i) {
        TypedArray obtainTypedArray = resources.obtainTypedArray(i);
        try {
            if (obtainTypedArray.length() == 0) {
                return null;
            }
            int length = obtainTypedArray.length();
            float[] fArr = new float[length];
            for (int i2 = 0; i2 < length; i2++) {
                float f = obtainTypedArray.getFloat(i2, Float.NaN);
                fArr[i2] = f;
                if (Float.isNaN(f)) {
                    return null;
                }
            }
            return fArr;
        } finally {
            obtainTypedArray.recycle();
        }
    }
}
