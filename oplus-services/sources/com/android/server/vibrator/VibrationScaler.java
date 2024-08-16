package com.android.server.vibrator;

import android.R;
import android.content.Context;
import android.os.VibrationEffect;
import android.os.vibrator.PrebakedSegment;
import android.util.Slog;
import android.util.SparseArray;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class VibrationScaler {
    private static final float SCALE_FACTOR_HIGH = 1.2f;
    private static final float SCALE_FACTOR_LOW = 0.8f;
    private static final float SCALE_FACTOR_NONE = 1.0f;
    private static final float SCALE_FACTOR_VERY_HIGH = 1.4f;
    private static final float SCALE_FACTOR_VERY_LOW = 0.6f;
    private static final int SCALE_HIGH = 1;
    private static final int SCALE_LOW = -1;
    private static final int SCALE_NONE = 0;
    private static final int SCALE_VERY_HIGH = 2;
    private static final int SCALE_VERY_LOW = -2;
    private static final String TAG = "VibrationScaler";
    private static final int VIBRATE_VERY_HIGH = 2400;
    private final int mDefaultVibrationAmplitude;
    private final SparseArray<ScaleLevel> mScaleLevels;
    private final VibrationSettings mSettingsController;

    /* JADX INFO: Access modifiers changed from: package-private */
    public VibrationScaler(Context context, VibrationSettings vibrationSettings) {
        this.mSettingsController = vibrationSettings;
        this.mDefaultVibrationAmplitude = context.getResources().getInteger(R.integer.config_extraFreeKbytesAbsolute);
        SparseArray<ScaleLevel> sparseArray = new SparseArray<>();
        this.mScaleLevels = sparseArray;
        sparseArray.put(-2, new ScaleLevel(SCALE_FACTOR_VERY_LOW));
        sparseArray.put(-1, new ScaleLevel(SCALE_FACTOR_LOW));
        sparseArray.put(0, new ScaleLevel(1.0f));
        sparseArray.put(1, new ScaleLevel(SCALE_FACTOR_HIGH));
        sparseArray.put(2, new ScaleLevel(SCALE_FACTOR_VERY_HIGH));
    }

    public int getExternalVibrationScale(int i) {
        this.mSettingsController.getDefaultIntensity(i);
        int currentIntensity = this.mSettingsController.getCurrentIntensity(i);
        if (currentIntensity == 0) {
            return 0;
        }
        if (currentIntensity >= -2 && currentIntensity <= VIBRATE_VERY_HIGH) {
            Slog.e(TAG, "scaleLevel = " + currentIntensity);
            return currentIntensity;
        }
        Slog.w(TAG, "Error in scaling calculations, ended up with invalid scale level " + currentIntensity + " for vibration with usage " + i);
        return 0;
    }

    public <T extends VibrationEffect> T scale(VibrationEffect vibrationEffect, int i) {
        int defaultIntensity = this.mSettingsController.getDefaultIntensity(i);
        int currentIntensity = this.mSettingsController.getCurrentIntensity(i);
        if (currentIntensity == 0) {
            currentIntensity = defaultIntensity;
        }
        T t = (T) vibrationEffect.applyEffectStrength(intensityToEffectStrength(currentIntensity)).resolve(this.mDefaultVibrationAmplitude);
        ScaleLevel scaleLevel = this.mScaleLevels.get(currentIntensity - defaultIntensity);
        if (scaleLevel == null) {
            Slog.e(TAG, "No configured scaling level! (current=" + currentIntensity + ", default= " + defaultIntensity + ")");
            return t;
        }
        return (T) t.scale(scaleLevel.factor);
    }

    public PrebakedSegment scale(PrebakedSegment prebakedSegment, int i) {
        int currentIntensity = this.mSettingsController.getCurrentIntensity(i);
        if (currentIntensity == 0) {
            currentIntensity = this.mSettingsController.getDefaultIntensity(i);
        }
        return prebakedSegment.applyEffectStrength(intensityToEffectStrength(currentIntensity));
    }

    private static int intensityToEffectStrength(int i) {
        if (i == 1) {
            return 0;
        }
        if (i == 2) {
            return 1;
        }
        if (i != 3) {
            Slog.w(TAG, "Got unexpected vibration intensity: " + i);
        }
        return 2;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static final class ScaleLevel {
        public final float factor;

        ScaleLevel(float f) {
            this.factor = f;
        }

        public String toString() {
            return "ScaleLevel{factor=" + this.factor + "}";
        }
    }
}
