package com.android.server.notification;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.media.AudioAttributes;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Slog;
import com.android.server.pm.PackageManagerService;
import java.time.Duration;
import java.util.Arrays;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class VibratorHelper {
    private static final long[] DEFAULT_VIBRATE_PATTERN = {0, 200, 200, 0};
    private static final String TAG = "NotificationVibratorHelper";
    private static final int VIBRATE_PATTERN_MAXLEN = 17;
    private final long[] mDefaultPattern;
    private final float[] mDefaultPwlePattern;
    private final long[] mFallbackPattern;
    private final float[] mFallbackPwlePattern;
    private final Vibrator mVibrator;

    public VibratorHelper(Context context) {
        this.mVibrator = (Vibrator) context.getSystemService(Vibrator.class);
        Resources resources = context.getResources();
        long[] jArr = DEFAULT_VIBRATE_PATTERN;
        this.mDefaultPattern = getLongArray(resources, R.array.config_displayWhiteBalanceDisplayPrimaries, 17, jArr);
        this.mFallbackPattern = getLongArray(context.getResources(), R.array.networkAttributes, 17, jArr);
        this.mDefaultPwlePattern = getFloatArray(context.getResources(), R.array.config_displayWhiteBalanceHighLightAmbientBiases);
        this.mFallbackPwlePattern = getFloatArray(context.getResources(), R.array.network_switch_type_name);
    }

    public static VibrationEffect createWaveformVibration(long[] jArr, boolean z) {
        if (jArr == null) {
            return null;
        }
        try {
            return VibrationEffect.createWaveform(jArr, z ? 0 : -1);
        } catch (IllegalArgumentException unused) {
            Slog.e(TAG, "Error creating vibration waveform with pattern: " + Arrays.toString(jArr));
            return null;
        }
    }

    public static VibrationEffect createPwleWaveformVibration(float[] fArr, boolean z) {
        if (fArr == null) {
            return null;
        }
        try {
            int length = fArr.length;
            if (length != 0 && length % 3 == 0) {
                VibrationEffect.WaveformBuilder startWaveform = VibrationEffect.startWaveform();
                for (int i = 0; i < length; i += 3) {
                    startWaveform.addTransition(Duration.ofMillis((int) fArr[i + 2]), VibrationEffect.VibrationParameter.targetAmplitude(fArr[i]), VibrationEffect.VibrationParameter.targetFrequency(fArr[i + 1]));
                }
                VibrationEffect build = startWaveform.build();
                return z ? VibrationEffect.startComposition().repeatEffectIndefinitely(build).compose() : build;
            }
            return null;
        } catch (IllegalArgumentException unused) {
            Slog.e(TAG, "Error creating vibration PWLE waveform with pattern: " + Arrays.toString(fArr));
            return null;
        }
    }

    public void vibrate(VibrationEffect vibrationEffect, AudioAttributes audioAttributes, String str) {
        this.mVibrator.vibrate(1000, PackageManagerService.PLATFORM_PACKAGE_NAME, vibrationEffect, str, new VibrationAttributes.Builder(audioAttributes).build());
    }

    public void cancelVibration() {
        this.mVibrator.cancel(-15);
    }

    public VibrationEffect createFallbackVibration(boolean z) {
        VibrationEffect createPwleWaveformVibration;
        return (!this.mVibrator.hasFrequencyControl() || (createPwleWaveformVibration = createPwleWaveformVibration(this.mFallbackPwlePattern, z)) == null) ? createWaveformVibration(this.mFallbackPattern, z) : createPwleWaveformVibration;
    }

    public VibrationEffect createDefaultVibration(boolean z) {
        VibrationEffect createPwleWaveformVibration;
        return (!this.mVibrator.hasFrequencyControl() || (createPwleWaveformVibration = createPwleWaveformVibration(this.mDefaultPwlePattern, z)) == null) ? createWaveformVibration(this.mDefaultPattern, z) : createPwleWaveformVibration;
    }

    private static float[] getFloatArray(Resources resources, int i) {
        TypedArray obtainTypedArray = resources.obtainTypedArray(i);
        try {
            int length = obtainTypedArray.length();
            float[] fArr = new float[length];
            for (int i2 = 0; i2 < length; i2++) {
                float f = obtainTypedArray.getFloat(i2, Float.NaN);
                fArr[i2] = f;
                if (Float.isNaN(f)) {
                    obtainTypedArray.recycle();
                    return null;
                }
            }
            return fArr;
        } finally {
            obtainTypedArray.recycle();
        }
    }

    private static long[] getLongArray(Resources resources, int i, int i2, long[] jArr) {
        int[] intArray = resources.getIntArray(i);
        if (intArray == null) {
            return jArr;
        }
        if (intArray.length <= i2) {
            i2 = intArray.length;
        }
        long[] jArr2 = new long[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            jArr2[i3] = intArray[i3];
        }
        return jArr2;
    }
}
