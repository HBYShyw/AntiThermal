package com.android.server.display;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.display.BrightnessConfiguration;
import android.hardware.display.DisplayManagerInternal;
import android.os.Bundle;
import android.os.Looper;
import com.android.server.display.AutomaticBrightnessController;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IColorAutomaticBrightnessController {
    void addCallbacks(AutomaticBrightnessController.Callbacks callbacks);

    void configure(boolean z, boolean z2, float f, boolean z3, boolean z4, int i, int i2);

    float convertToFloatScale(float f);

    void dump(PrintWriter printWriter);

    int getAIBrightness();

    int getAIBrightness(int i);

    float getAmbientLux();

    int getAutoRate(int i);

    int getAutomaticScreenBrightness();

    int getAutomaticScreenBrightness(int i);

    float getAutomaticScreenBrightnessAdjustment();

    AutomaticBrightnessController.Callbacks getCallbacks();

    int getCameraMode();

    BrightnessConfiguration getDefaultConfig();

    float getRawAutomaticScreenBrightness();

    boolean getmLightSensorEnabled();

    boolean getmProximityNear();

    void init(AutomaticBrightnessController.Callbacks callbacks, Looper looper, SensorManager sensorManager, Sensor sensor, BrightnessMappingStrategy brightnessMappingStrategy, float f, int i, long j);

    boolean isAlreadyInit();

    boolean isCtsTest();

    boolean isDefaultConfig();

    boolean isSensorChanged();

    void resetShortTermModel();

    void setAnimating(boolean z, int i, boolean z2);

    void setAutomaticScreenBrightness(int i, int i2);

    void setAuxFakeLux(boolean z, int i);

    void setCameraBacklight(boolean z);

    void setCameraMode(int i);

    void setCameraUseAdjustmentSetting(boolean z);

    void setDualLightSensorConfig(boolean z, String str, String str2, float f);

    void setFakeLux(boolean z, int i);

    void setGalleryBacklight(boolean z);

    void setGalleryMode(int i);

    boolean setLoggingEnabled(boolean z);

    void setLux(float f);

    void setMainFakeLux(boolean z, int i);

    void setPowerRequest(DisplayManagerInternal.DisplayPowerRequest displayPowerRequest);

    void setScreenOn(int i, boolean z, DisplayManagerInternal.DisplayPowerRequest displayPowerRequest);

    void setStateChanged(int i, Bundle bundle);

    void setTalkBack(boolean z, int i);

    void stop(boolean z);

    void updateBrightnessTotalRateType(int i, int i2);
}
