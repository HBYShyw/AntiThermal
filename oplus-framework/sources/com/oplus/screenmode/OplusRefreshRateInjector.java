package com.oplus.screenmode;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.IBinder;
import android.os.SystemProperties;
import android.util.ArraySet;
import android.view.Display;
import android.view.ViewGroup;
import java.lang.reflect.Constructor;
import java.util.Set;

/* loaded from: classes.dex */
public class OplusRefreshRateInjector {
    public static final boolean USE_REFRESH_RATE_V2 = SystemProperties.getBoolean("debug.refresh_rate.v2", true);
    private static final boolean ALLOW_REFRESH_RATE_OVERRIDE = SystemProperties.getBoolean("debug.refresh_rate.view_override", true);
    private static Boolean sFeatureSupport = null;

    private static boolean supportedRefreshRateFeature(Context context) {
        DisplayManager displayManager = (DisplayManager) context.getSystemService("display");
        Display display = displayManager == null ? null : displayManager.getDisplay(0);
        Display.Mode[] supportedModes = display != null ? display.getSupportedModes() : null;
        Set<Float> validRefreshRates = new ArraySet<>();
        if (supportedModes != null) {
            for (Display.Mode mode : supportedModes) {
                validRefreshRates.add(Float.valueOf(mode.getRefreshRate()));
            }
        }
        return validRefreshRates.size() > 1;
    }

    public static OplusRefreshRateInjector newInstance(Context context) {
        if (sFeatureSupport == null) {
            sFeatureSupport = Boolean.valueOf(supportedRefreshRateFeature(context));
        }
        if (USE_REFRESH_RATE_V2 && sFeatureSupport.booleanValue() && ALLOW_REFRESH_RATE_OVERRIDE) {
            try {
                Constructor constructor = Class.forName("com.oplus.screenmode.OplusRefreshRateInjectorImpl").getDeclaredConstructor(Context.class);
                OplusRefreshRateInjector instance = (OplusRefreshRateInjector) constructor.newInstance(context);
                return instance;
            } catch (Exception e) {
                OplusRefreshRateInjector instance2 = new OplusRefreshRateInjector();
                return instance2;
            }
        }
        OplusRefreshRateInjector instance3 = new OplusRefreshRateInjector();
        return instance3;
    }

    public void setRefreshRateIfNeed(Context context, ViewGroup viewGroup, IBinder token) {
    }

    public static void enterPSMode(boolean enter) {
    }

    public static void enterPSModeOnRate(boolean enter, int rate) {
    }

    public boolean setHighTemperatureStatus(int status, int rate) {
        return false;
    }
}
