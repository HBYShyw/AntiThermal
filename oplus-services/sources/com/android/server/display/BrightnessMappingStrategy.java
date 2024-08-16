package com.android.server.display;

import android.R;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.hardware.display.BrightnessConfiguration;
import android.hardware.display.BrightnessCorrection;
import android.text.TextUtils;
import android.util.MathUtils;
import android.util.Pair;
import android.util.Slog;
import android.util.Spline;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.display.BrightnessSynchronizer;
import com.android.internal.util.Preconditions;
import com.android.server.display.utils.Plog;
import com.android.server.display.whitebalance.DisplayWhiteBalanceController;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class BrightnessMappingStrategy {
    private static final float LUX_GRAD_SMOOTHING = 0.25f;
    private static final float MAX_GRAD = 1.0f;
    private static final float MIN_PERMISSABLE_INCREASE = 0.004f;
    public static final float NO_USER_BRIGHTNESS = -1.0f;
    public static final float NO_USER_LUX = -1.0f;
    private static final float SHORT_TERM_MODEL_THRESHOLD_RATIO = 0.6f;
    protected boolean mLoggingEnabled;
    private static final String TAG = "BrightnessMappingStrategy";
    private static final Plog PLOG = Plog.createSystemPlog(TAG);
    public static IBrightnessMappingStrategyExt mBrightnessMappingStrategyExt = (IBrightnessMappingStrategyExt) ExtLoader.type(IBrightnessMappingStrategyExt.class).create();

    public abstract void addUserDataPoint(float f, float f2);

    public abstract void clearUserDataPoints();

    public abstract float convertToAdjustedNits(float f);

    public abstract float convertToFloatScale(float f);

    public abstract float convertToNits(float f);

    public abstract void dump(PrintWriter printWriter, float f);

    public abstract float getAutoBrightnessAdjustment();

    public abstract float getBrightness(float f, String str, int i);

    public abstract BrightnessConfiguration getBrightnessConfiguration();

    public abstract BrightnessConfiguration getDefaultConfig();

    public abstract long getShortTermModelTimeout();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract float getUserBrightness();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract float getUserLux();

    public abstract boolean hasUserDataPoints();

    public abstract boolean isDefaultConfig();

    public abstract boolean isForIdleMode();

    public abstract void recalculateSplines(boolean z, float[] fArr);

    public abstract boolean setAutoBrightnessAdjustment(float f);

    public abstract boolean setBrightnessConfiguration(BrightnessConfiguration brightnessConfiguration);

    public static BrightnessMappingStrategy create(Resources resources, DisplayDeviceConfig displayDeviceConfig, DisplayWhiteBalanceController displayWhiteBalanceController) {
        return create(resources, displayDeviceConfig, false, displayWhiteBalanceController);
    }

    public static BrightnessMappingStrategy createForIdleMode(Resources resources, DisplayDeviceConfig displayDeviceConfig, DisplayWhiteBalanceController displayWhiteBalanceController) {
        return create(resources, displayDeviceConfig, true, displayWhiteBalanceController);
    }

    private static BrightnessMappingStrategy create(Resources resources, DisplayDeviceConfig displayDeviceConfig, boolean z, DisplayWhiteBalanceController displayWhiteBalanceController) {
        float[] autoBrightnessBrighteningLevelsNits;
        float[] autoBrightnessBrighteningLevelsLux;
        if (z) {
            autoBrightnessBrighteningLevelsNits = getFloatArray(resources.obtainTypedArray(R.array.config_autoBrightnessLevels));
            autoBrightnessBrighteningLevelsLux = getLuxLevels(resources.getIntArray(R.array.config_batteryPackageTypeSystem));
        } else {
            autoBrightnessBrighteningLevelsNits = displayDeviceConfig.getAutoBrightnessBrighteningLevelsNits();
            autoBrightnessBrighteningLevelsLux = displayDeviceConfig.getAutoBrightnessBrighteningLevelsLux();
        }
        float[] fArr = autoBrightnessBrighteningLevelsLux;
        int[] intArray = resources.getIntArray(R.array.config_availableColorModes);
        float fraction = resources.getFraction(R.fraction.config_autoBrightnessAdjustmentMaxGamma, 1, 1);
        long integer = resources.getInteger(R.integer.config_bluetooth_max_connected_audio_devices);
        BrightnessMappingStrategy brightnessMappingStrategy = mBrightnessMappingStrategyExt.getBrightnessMappingStrategy();
        if (brightnessMappingStrategy != null) {
            return brightnessMappingStrategy;
        }
        float[] nits = displayDeviceConfig.getNits();
        float[] brightness = displayDeviceConfig.getBrightness();
        if (isValidMapping(nits, brightness) && isValidMapping(fArr, autoBrightnessBrighteningLevelsNits)) {
            BrightnessConfiguration.Builder builder = new BrightnessConfiguration.Builder(fArr, autoBrightnessBrighteningLevelsNits);
            builder.setShortTermModelTimeoutMillis(integer);
            builder.setShortTermModelLowerLuxMultiplier(SHORT_TERM_MODEL_THRESHOLD_RATIO);
            builder.setShortTermModelUpperLuxMultiplier(SHORT_TERM_MODEL_THRESHOLD_RATIO);
            return new PhysicalMappingStrategy(builder.build(), nits, brightness, fraction, z, displayWhiteBalanceController);
        }
        if (!isValidMapping(fArr, intArray) || z) {
            return null;
        }
        return new SimpleMappingStrategy(fArr, intArray, fraction, integer);
    }

    private static float[] getLuxLevels(int[] iArr) {
        float[] fArr = new float[iArr.length + 1];
        int i = 0;
        while (i < iArr.length) {
            int i2 = i + 1;
            fArr[i2] = iArr[i];
            i = i2;
        }
        return fArr;
    }

    public static float[] getFloatArray(TypedArray typedArray) {
        int length = typedArray.length();
        float[] fArr = new float[length];
        for (int i = 0; i < length; i++) {
            fArr[i] = typedArray.getFloat(i, -1.0f);
        }
        typedArray.recycle();
        return fArr;
    }

    private static boolean isValidMapping(float[] fArr, float[] fArr2) {
        if (fArr == null || fArr2 == null || fArr.length == 0 || fArr2.length == 0 || fArr.length != fArr2.length) {
            return false;
        }
        int length = fArr.length;
        float f = fArr[0];
        float f2 = fArr2[0];
        if (f >= 0.0f && f2 >= 0.0f && !Float.isNaN(f) && !Float.isNaN(f2)) {
            for (int i = 1; i < length; i++) {
                float f3 = fArr[i];
                if (f >= f3 || f2 > fArr2[i] || Float.isNaN(f3) || Float.isNaN(fArr2[i])) {
                    return false;
                }
                f = fArr[i];
                f2 = fArr2[i];
            }
            return true;
        }
        return false;
    }

    private static boolean isValidMapping(float[] fArr, int[] iArr) {
        if (fArr == null || iArr == null || fArr.length == 0 || iArr.length == 0 || fArr.length != iArr.length) {
            return false;
        }
        int length = fArr.length;
        float f = fArr[0];
        int i = iArr[0];
        if (f >= 0.0f && i >= 0 && !Float.isNaN(f)) {
            for (int i2 = 1; i2 < length; i2++) {
                float f2 = fArr[i2];
                if (f >= f2 || i > iArr[i2] || Float.isNaN(f2)) {
                    return false;
                }
                f = fArr[i2];
                i = iArr[i2];
            }
            return true;
        }
        return false;
    }

    public boolean setLoggingEnabled(boolean z) {
        if (this.mLoggingEnabled == z) {
            return false;
        }
        this.mLoggingEnabled = z;
        return true;
    }

    public float getBrightness(float f) {
        return getBrightness(f, null, -1);
    }

    public boolean shouldResetShortTermModel(float f, float f2) {
        float f3;
        BrightnessConfiguration brightnessConfiguration = getBrightnessConfiguration();
        float f4 = SHORT_TERM_MODEL_THRESHOLD_RATIO;
        if (brightnessConfiguration != null) {
            float shortTermModelLowerLuxMultiplier = !Float.isNaN(brightnessConfiguration.getShortTermModelLowerLuxMultiplier()) ? brightnessConfiguration.getShortTermModelLowerLuxMultiplier() : 0.6f;
            if (!Float.isNaN(brightnessConfiguration.getShortTermModelUpperLuxMultiplier())) {
                f4 = brightnessConfiguration.getShortTermModelUpperLuxMultiplier();
            }
            f3 = f4;
            f4 = shortTermModelLowerLuxMultiplier;
        } else {
            f3 = 0.6f;
        }
        float f5 = f2 - (f4 * f2);
        float f6 = f2 + (f3 * f2);
        if (f5 < f && f <= f6) {
            if (!this.mLoggingEnabled) {
                return false;
            }
            Slog.d(TAG, "ShortTermModel: re-validate user data, ambient lux is " + f5 + " < " + f + " < " + f6);
            return false;
        }
        Slog.d(TAG, "ShortTermModel: reset data, ambient lux is " + f + "(" + f5 + ", " + f6 + ")");
        return true;
    }

    protected static float normalizeAbsoluteBrightness(int i) {
        return BrightnessSynchronizer.brightnessIntToFloat(i);
    }

    private Pair<float[], float[]> insertControlPoint(float[] fArr, float[] fArr2, float f, float f2) {
        float[] fArr3;
        float[] fArr4;
        int findInsertionPoint = findInsertionPoint(fArr, f);
        if (findInsertionPoint == fArr.length) {
            fArr4 = Arrays.copyOf(fArr, fArr.length + 1);
            fArr3 = Arrays.copyOf(fArr2, fArr2.length + 1);
            fArr4[findInsertionPoint] = f;
            fArr3[findInsertionPoint] = f2;
        } else if (fArr[findInsertionPoint] == f) {
            fArr4 = Arrays.copyOf(fArr, fArr.length);
            fArr3 = Arrays.copyOf(fArr2, fArr2.length);
            fArr3[findInsertionPoint] = f2;
        } else {
            float[] copyOf = Arrays.copyOf(fArr, fArr.length + 1);
            int i = findInsertionPoint + 1;
            System.arraycopy(copyOf, findInsertionPoint, copyOf, i, fArr.length - findInsertionPoint);
            copyOf[findInsertionPoint] = f;
            float[] copyOf2 = Arrays.copyOf(fArr2, fArr2.length + 1);
            System.arraycopy(copyOf2, findInsertionPoint, copyOf2, i, fArr2.length - findInsertionPoint);
            copyOf2[findInsertionPoint] = f2;
            fArr3 = copyOf2;
            fArr4 = copyOf;
        }
        smoothCurve(fArr4, fArr3, findInsertionPoint);
        return Pair.create(fArr4, fArr3);
    }

    private int findInsertionPoint(float[] fArr, float f) {
        for (int i = 0; i < fArr.length; i++) {
            if (f <= fArr[i]) {
                return i;
            }
        }
        return fArr.length;
    }

    private void smoothCurve(float[] fArr, float[] fArr2, int i) {
        if (this.mLoggingEnabled) {
            PLOG.logCurve("unsmoothed curve", fArr, fArr2);
        }
        float f = fArr[i];
        float f2 = fArr2[i];
        int i2 = i + 1;
        while (i2 < fArr.length) {
            float f3 = fArr[i2];
            float f4 = fArr2[i2];
            f2 = MathUtils.constrain(f4, f2, MathUtils.max(permissibleRatio(f3, f) * f2, MIN_PERMISSABLE_INCREASE + f2));
            if (f2 == f4) {
                break;
            }
            fArr2[i2] = f2;
            i2++;
            f = f3;
        }
        float f5 = fArr[i];
        float f6 = fArr2[i];
        int i3 = i - 1;
        while (i3 >= 0) {
            float f7 = fArr[i3];
            float f8 = fArr2[i3];
            f6 = MathUtils.constrain(f8, permissibleRatio(f7, f5) * f6, f6);
            if (f6 == f8) {
                break;
            }
            fArr2[i3] = f6;
            i3--;
            f5 = f7;
        }
        if (this.mLoggingEnabled) {
            PLOG.logCurve("smoothed curve", fArr, fArr2);
        }
    }

    private float permissibleRatio(float f, float f2) {
        return MathUtils.pow((f + LUX_GRAD_SMOOTHING) / (f2 + LUX_GRAD_SMOOTHING), 1.0f);
    }

    protected float inferAutoBrightnessAdjustment(float f, float f2, float f3) {
        float f4;
        float f5 = Float.NaN;
        if (f3 <= 0.1f || f3 >= 0.9f) {
            f4 = f2 - f3;
        } else if (f2 == 0.0f) {
            f4 = -1.0f;
        } else if (f2 == 1.0f) {
            f4 = 1.0f;
        } else {
            f5 = MathUtils.log(f2) / MathUtils.log(f3);
            f4 = (-MathUtils.log(f5)) / MathUtils.log(f);
        }
        float constrain = MathUtils.constrain(f4, -1.0f, 1.0f);
        if (this.mLoggingEnabled) {
            StringBuilder sb = new StringBuilder();
            sb.append("inferAutoBrightnessAdjustment: ");
            sb.append(f);
            sb.append("^");
            float f6 = -constrain;
            sb.append(f6);
            sb.append("=");
            sb.append(MathUtils.pow(f, f6));
            sb.append(" == ");
            sb.append(f5);
            Slog.d(TAG, sb.toString());
            Slog.d(TAG, "inferAutoBrightnessAdjustment: " + f3 + "^" + f5 + "=" + MathUtils.pow(f3, f5) + " == " + f2);
        }
        return constrain;
    }

    protected Pair<float[], float[]> getAdjustedCurve(float[] fArr, float[] fArr2, float f, float f2, float f3, float f4) {
        float[] copyOf = Arrays.copyOf(fArr2, fArr2.length);
        if (this.mLoggingEnabled) {
            PLOG.logCurve("unadjusted curve", fArr, copyOf);
        }
        float f5 = -MathUtils.constrain(f3, -1.0f, 1.0f);
        float pow = MathUtils.pow(f4, f5);
        if (this.mLoggingEnabled) {
            Slog.d(TAG, "getAdjustedCurve: " + f4 + "^" + f5 + "=" + MathUtils.pow(f4, f5) + " == " + pow);
        }
        if (pow != 1.0f) {
            for (int i = 0; i < copyOf.length; i++) {
                copyOf[i] = MathUtils.pow(copyOf[i], pow);
            }
        }
        if (this.mLoggingEnabled) {
            PLOG.logCurve("gamma adjusted curve", fArr, copyOf);
        }
        if (f != -1.0f) {
            Pair<float[], float[]> insertControlPoint = insertControlPoint(fArr, copyOf, f, f2);
            float[] fArr3 = (float[]) insertControlPoint.first;
            copyOf = (float[]) insertControlPoint.second;
            if (this.mLoggingEnabled) {
                Plog plog = PLOG;
                plog.logCurve("gamma and user adjusted curve", fArr3, copyOf);
                Pair<float[], float[]> insertControlPoint2 = insertControlPoint(fArr, fArr2, f, f2);
                plog.logCurve("user adjusted curve", (float[]) insertControlPoint2.first, (float[]) insertControlPoint2.second);
            }
            fArr = fArr3;
        }
        return Pair.create(fArr, copyOf);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class SimpleMappingStrategy extends BrightnessMappingStrategy {
        private float mAutoBrightnessAdjustment;
        private final float[] mBrightness;
        private final float[] mLux;
        private float mMaxGamma;
        private long mShortTermModelTimeout;
        private Spline mSpline;
        private float mUserBrightness;
        private float mUserLux;

        @Override // com.android.server.display.BrightnessMappingStrategy
        public float convertToAdjustedNits(float f) {
            return -1.0f;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public float convertToFloatScale(float f) {
            return Float.NaN;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public float convertToNits(float f) {
            return -1.0f;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public BrightnessConfiguration getBrightnessConfiguration() {
            return null;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public BrightnessConfiguration getDefaultConfig() {
            return null;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public boolean isDefaultConfig() {
            return true;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public boolean isForIdleMode() {
            return false;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public void recalculateSplines(boolean z, float[] fArr) {
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public boolean setBrightnessConfiguration(BrightnessConfiguration brightnessConfiguration) {
            return false;
        }

        private SimpleMappingStrategy(float[] fArr, int[] iArr, float f, long j) {
            Preconditions.checkArgument((fArr.length == 0 || iArr.length == 0) ? false : true, "Lux and brightness arrays must not be empty!");
            Preconditions.checkArgument(fArr.length == iArr.length, "Lux and brightness arrays must be the same length!");
            Preconditions.checkArrayElementsInRange(fArr, 0.0f, Float.MAX_VALUE, "lux");
            Preconditions.checkArrayElementsInRange(iArr, 0, Integer.MAX_VALUE, "brightness");
            int length = iArr.length;
            this.mLux = new float[length];
            this.mBrightness = new float[length];
            for (int i = 0; i < length; i++) {
                this.mLux[i] = fArr[i];
                this.mBrightness[i] = BrightnessMappingStrategy.normalizeAbsoluteBrightness(iArr[i]);
            }
            this.mMaxGamma = f;
            this.mAutoBrightnessAdjustment = 0.0f;
            this.mUserLux = -1.0f;
            this.mUserBrightness = -1.0f;
            if (this.mLoggingEnabled) {
                BrightnessMappingStrategy.PLOG.start("simple mapping strategy");
            }
            computeSpline();
            this.mShortTermModelTimeout = j;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public long getShortTermModelTimeout() {
            return this.mShortTermModelTimeout;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public float getBrightness(float f, String str, int i) {
            return this.mSpline.interpolate(f);
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public float getAutoBrightnessAdjustment() {
            return this.mAutoBrightnessAdjustment;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public boolean setAutoBrightnessAdjustment(float f) {
            float constrain = MathUtils.constrain(f, -1.0f, 1.0f);
            if (constrain == this.mAutoBrightnessAdjustment) {
                return false;
            }
            if (this.mLoggingEnabled) {
                Slog.d(BrightnessMappingStrategy.TAG, "setAutoBrightnessAdjustment: " + this.mAutoBrightnessAdjustment + " => " + constrain);
                BrightnessMappingStrategy.PLOG.start("auto-brightness adjustment");
            }
            this.mAutoBrightnessAdjustment = constrain;
            computeSpline();
            return true;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public void addUserDataPoint(float f, float f2) {
            float unadjustedBrightness = getUnadjustedBrightness(f);
            if (this.mLoggingEnabled) {
                Slog.d(BrightnessMappingStrategy.TAG, "addUserDataPoint: (" + f + "," + f2 + ")");
                BrightnessMappingStrategy.PLOG.start("add user data point").logPoint("user data point", f, f2).logPoint("current brightness", f, unadjustedBrightness);
            }
            float inferAutoBrightnessAdjustment = inferAutoBrightnessAdjustment(this.mMaxGamma, f2, unadjustedBrightness);
            if (this.mLoggingEnabled) {
                Slog.d(BrightnessMappingStrategy.TAG, "addUserDataPoint: " + this.mAutoBrightnessAdjustment + " => " + inferAutoBrightnessAdjustment);
            }
            this.mAutoBrightnessAdjustment = inferAutoBrightnessAdjustment;
            this.mUserLux = f;
            this.mUserBrightness = f2;
            computeSpline();
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public void clearUserDataPoints() {
            if (this.mUserLux != -1.0f) {
                if (this.mLoggingEnabled) {
                    Slog.d(BrightnessMappingStrategy.TAG, "clearUserDataPoints: " + this.mAutoBrightnessAdjustment + " => 0");
                    BrightnessMappingStrategy.PLOG.start("clear user data points").logPoint("user data point", this.mUserLux, this.mUserBrightness);
                }
                this.mAutoBrightnessAdjustment = 0.0f;
                this.mUserLux = -1.0f;
                this.mUserBrightness = -1.0f;
                computeSpline();
            }
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public boolean hasUserDataPoints() {
            return this.mUserLux != -1.0f;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public void dump(PrintWriter printWriter, float f) {
            printWriter.println("SimpleMappingStrategy");
            printWriter.println("  mSpline=" + this.mSpline);
            printWriter.println("  mMaxGamma=" + this.mMaxGamma);
            printWriter.println("  mAutoBrightnessAdjustment=" + this.mAutoBrightnessAdjustment);
            printWriter.println("  mUserLux=" + this.mUserLux);
            printWriter.println("  mUserBrightness=" + this.mUserBrightness);
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        float getUserLux() {
            return this.mUserLux;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        float getUserBrightness() {
            return this.mUserBrightness;
        }

        private void computeSpline() {
            Pair<float[], float[]> adjustedCurve = getAdjustedCurve(this.mLux, this.mBrightness, this.mUserLux, this.mUserBrightness, this.mAutoBrightnessAdjustment, this.mMaxGamma);
            this.mSpline = Spline.createSpline((float[]) adjustedCurve.first, (float[]) adjustedCurve.second);
        }

        private float getUnadjustedBrightness(float f) {
            return Spline.createSpline(this.mLux, this.mBrightness).interpolate(f);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class PhysicalMappingStrategy extends BrightnessMappingStrategy {
        private Spline mAdjustedNitsToBrightnessSpline;
        private float mAutoBrightnessAdjustment;
        private final float[] mBrightness;
        private boolean mBrightnessRangeAdjustmentApplied;
        private Spline mBrightnessSpline;
        private Spline mBrightnessToAdjustedNitsSpline;
        private Spline mBrightnessToNitsSpline;
        private BrightnessConfiguration mConfig;
        private final BrightnessConfiguration mDefaultConfig;
        private final DisplayWhiteBalanceController mDisplayWhiteBalanceController;
        private final boolean mIsForIdleMode;
        private final float mMaxGamma;
        private final float[] mNits;
        private Spline mNitsToBrightnessSpline;
        private float mUserBrightness;
        private float mUserLux;

        public PhysicalMappingStrategy(BrightnessConfiguration brightnessConfiguration, float[] fArr, float[] fArr2, float f, boolean z, DisplayWhiteBalanceController displayWhiteBalanceController) {
            Preconditions.checkArgument((fArr.length == 0 || fArr2.length == 0) ? false : true, "Nits and brightness arrays must not be empty!");
            Preconditions.checkArgument(fArr.length == fArr2.length, "Nits and brightness arrays must be the same length!");
            Objects.requireNonNull(brightnessConfiguration);
            Preconditions.checkArrayElementsInRange(fArr, 0.0f, Float.MAX_VALUE, "nits");
            Preconditions.checkArrayElementsInRange(fArr2, 0.0f, 1.0f, "brightness");
            this.mIsForIdleMode = z;
            this.mMaxGamma = f;
            this.mAutoBrightnessAdjustment = 0.0f;
            this.mUserLux = -1.0f;
            this.mUserBrightness = -1.0f;
            this.mDisplayWhiteBalanceController = displayWhiteBalanceController;
            this.mNits = fArr;
            this.mBrightness = fArr2;
            computeNitsBrightnessSplines(fArr);
            this.mAdjustedNitsToBrightnessSpline = this.mNitsToBrightnessSpline;
            this.mBrightnessToAdjustedNitsSpline = this.mBrightnessToNitsSpline;
            this.mDefaultConfig = brightnessConfiguration;
            if (this.mLoggingEnabled) {
                BrightnessMappingStrategy.PLOG.start("physical mapping strategy");
            }
            this.mConfig = brightnessConfiguration;
            computeSpline();
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public long getShortTermModelTimeout() {
            if (this.mConfig.getShortTermModelTimeoutMillis() >= 0) {
                return this.mConfig.getShortTermModelTimeoutMillis();
            }
            return this.mDefaultConfig.getShortTermModelTimeoutMillis();
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public boolean setBrightnessConfiguration(BrightnessConfiguration brightnessConfiguration) {
            if (brightnessConfiguration == null) {
                brightnessConfiguration = this.mDefaultConfig;
            }
            if (brightnessConfiguration.equals(this.mConfig)) {
                return false;
            }
            if (this.mLoggingEnabled) {
                BrightnessMappingStrategy.PLOG.start("brightness configuration");
            }
            this.mConfig = brightnessConfiguration;
            computeSpline();
            return true;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public BrightnessConfiguration getBrightnessConfiguration() {
            return this.mConfig;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public float getBrightness(float f, String str, int i) {
            float interpolate = this.mBrightnessSpline.interpolate(f);
            DisplayWhiteBalanceController displayWhiteBalanceController = this.mDisplayWhiteBalanceController;
            if (displayWhiteBalanceController != null) {
                interpolate = displayWhiteBalanceController.calculateAdjustedBrightnessNits(interpolate);
            }
            float interpolate2 = this.mAdjustedNitsToBrightnessSpline.interpolate(interpolate);
            if (this.mUserLux == -1.0f) {
                return correctBrightness(interpolate2, str, i);
            }
            if (!this.mLoggingEnabled) {
                return interpolate2;
            }
            Slog.d(BrightnessMappingStrategy.TAG, "user point set, correction not applied");
            return interpolate2;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public float getAutoBrightnessAdjustment() {
            return this.mAutoBrightnessAdjustment;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public boolean setAutoBrightnessAdjustment(float f) {
            float constrain = MathUtils.constrain(f, -1.0f, 1.0f);
            if (constrain == this.mAutoBrightnessAdjustment) {
                return false;
            }
            if (this.mLoggingEnabled) {
                Slog.d(BrightnessMappingStrategy.TAG, "setAutoBrightnessAdjustment: " + this.mAutoBrightnessAdjustment + " => " + constrain);
                BrightnessMappingStrategy.PLOG.start("auto-brightness adjustment");
            }
            this.mAutoBrightnessAdjustment = constrain;
            computeSpline();
            return true;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public float convertToNits(float f) {
            return this.mBrightnessToNitsSpline.interpolate(f);
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public float convertToAdjustedNits(float f) {
            return this.mBrightnessToAdjustedNitsSpline.interpolate(f);
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public float convertToFloatScale(float f) {
            return this.mNitsToBrightnessSpline.interpolate(f);
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public void addUserDataPoint(float f, float f2) {
            float unadjustedBrightness = getUnadjustedBrightness(f);
            if (this.mLoggingEnabled) {
                Slog.d(BrightnessMappingStrategy.TAG, "addUserDataPoint: (" + f + "," + f2 + ")");
                BrightnessMappingStrategy.PLOG.start("add user data point").logPoint("user data point", f, f2).logPoint("current brightness", f, unadjustedBrightness);
            }
            float inferAutoBrightnessAdjustment = inferAutoBrightnessAdjustment(this.mMaxGamma, f2, unadjustedBrightness);
            if (this.mLoggingEnabled) {
                Slog.d(BrightnessMappingStrategy.TAG, "addUserDataPoint: " + this.mAutoBrightnessAdjustment + " => " + inferAutoBrightnessAdjustment);
            }
            this.mAutoBrightnessAdjustment = inferAutoBrightnessAdjustment;
            this.mUserLux = f;
            this.mUserBrightness = f2;
            computeSpline();
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public void clearUserDataPoints() {
            if (this.mUserLux != -1.0f) {
                if (this.mLoggingEnabled) {
                    Slog.d(BrightnessMappingStrategy.TAG, "clearUserDataPoints: " + this.mAutoBrightnessAdjustment + " => 0");
                    BrightnessMappingStrategy.PLOG.start("clear user data points").logPoint("user data point", this.mUserLux, this.mUserBrightness);
                }
                this.mAutoBrightnessAdjustment = 0.0f;
                this.mUserLux = -1.0f;
                this.mUserBrightness = -1.0f;
                computeSpline();
            }
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public boolean hasUserDataPoints() {
            return this.mUserLux != -1.0f;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public boolean isDefaultConfig() {
            return this.mDefaultConfig.equals(this.mConfig);
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public BrightnessConfiguration getDefaultConfig() {
            return this.mDefaultConfig;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public void recalculateSplines(boolean z, float[] fArr) {
            this.mBrightnessRangeAdjustmentApplied = z;
            if (z) {
                this.mAdjustedNitsToBrightnessSpline = Spline.createSpline(fArr, this.mBrightness);
                this.mBrightnessToAdjustedNitsSpline = Spline.createSpline(this.mBrightness, fArr);
            } else {
                this.mAdjustedNitsToBrightnessSpline = this.mNitsToBrightnessSpline;
                this.mBrightnessToAdjustedNitsSpline = this.mBrightnessToNitsSpline;
            }
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public void dump(PrintWriter printWriter, float f) {
            printWriter.println("PhysicalMappingStrategy");
            printWriter.println("  mConfig=" + this.mConfig);
            printWriter.println("  mBrightnessSpline=" + this.mBrightnessSpline);
            printWriter.println("  mNitsToBrightnessSpline=" + this.mNitsToBrightnessSpline);
            printWriter.println("  mBrightnessToNitsSpline=" + this.mBrightnessToNitsSpline);
            printWriter.println("  mAdjustedNitsToBrightnessSpline=" + this.mAdjustedNitsToBrightnessSpline);
            printWriter.println("  mAdjustedBrightnessToNitsSpline=" + this.mBrightnessToAdjustedNitsSpline);
            printWriter.println("  mMaxGamma=" + this.mMaxGamma);
            printWriter.println("  mAutoBrightnessAdjustment=" + this.mAutoBrightnessAdjustment);
            printWriter.println("  mUserLux=" + this.mUserLux);
            printWriter.println("  mUserBrightness=" + this.mUserBrightness);
            printWriter.println("  mDefaultConfig=" + this.mDefaultConfig);
            printWriter.println("  mBrightnessRangeAdjustmentApplied=" + this.mBrightnessRangeAdjustmentApplied);
            dumpConfigDiff(printWriter, f);
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public boolean isForIdleMode() {
            return this.mIsForIdleMode;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        float getUserLux() {
            return this.mUserLux;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        float getUserBrightness() {
            return this.mUserBrightness;
        }

        private void dumpConfigDiff(PrintWriter printWriter, float f) {
            String str;
            float[] fArr;
            String str2;
            int i;
            boolean z;
            PrintWriter printWriter2;
            String str3;
            String str4;
            PhysicalMappingStrategy physicalMappingStrategy = this;
            printWriter.println("  Difference between current config and default: ");
            Pair curve = physicalMappingStrategy.mConfig.getCurve();
            Spline createSpline = Spline.createSpline((float[]) curve.first, (float[]) curve.second);
            Pair curve2 = physicalMappingStrategy.mDefaultConfig.getCurve();
            Spline createSpline2 = Spline.createSpline((float[]) curve2.first, (float[]) curve2.second);
            Object obj = curve.first;
            float[] fArr2 = (float[]) obj;
            if (physicalMappingStrategy.mUserLux >= 0.0f) {
                fArr2 = Arrays.copyOf((float[]) obj, ((float[]) obj).length + 1);
                fArr2[fArr2.length - 1] = physicalMappingStrategy.mUserLux;
                Arrays.sort(fArr2);
            }
            String str5 = "";
            StringBuilder sb = null;
            String str6 = "";
            boolean z2 = true;
            StringBuilder sb2 = null;
            StringBuilder sb3 = null;
            StringBuilder sb4 = null;
            StringBuilder sb5 = null;
            StringBuilder sb6 = null;
            StringBuilder sb7 = null;
            int i2 = 0;
            while (i2 < fArr2.length) {
                float f2 = fArr2[i2];
                if (z2) {
                    StringBuilder sb8 = new StringBuilder("            lux: ");
                    sb2 = new StringBuilder("        default: ");
                    sb3 = new StringBuilder("      long-term: ");
                    sb4 = new StringBuilder("        current: ");
                    sb5 = new StringBuilder("    current(bl): ");
                    sb6 = new StringBuilder("     current(%): ");
                    sb7 = new StringBuilder("  current(hbm%): ");
                    str = str5;
                    sb = sb8;
                    z2 = false;
                } else {
                    str = str5;
                }
                float interpolate = createSpline2.interpolate(f2);
                Spline spline = createSpline2;
                float interpolate2 = createSpline.interpolate(f2);
                Spline spline2 = createSpline;
                float interpolate3 = physicalMappingStrategy.mBrightnessSpline.interpolate(f2);
                float interpolate4 = physicalMappingStrategy.mAdjustedNitsToBrightnessSpline.interpolate(interpolate3);
                int i3 = i2;
                if (f2 == physicalMappingStrategy.mUserLux) {
                    str2 = "^";
                    fArr = fArr2;
                } else {
                    fArr = fArr2;
                    str2 = str;
                }
                String str7 = str2 + physicalMappingStrategy.toStrFloatForDump(f2);
                String strFloatForDump = physicalMappingStrategy.toStrFloatForDump(interpolate);
                String strFloatForDump2 = physicalMappingStrategy.toStrFloatForDump(interpolate2);
                String strFloatForDump3 = physicalMappingStrategy.toStrFloatForDump(interpolate3);
                String strFloatForDump4 = physicalMappingStrategy.toStrFloatForDump(interpolate4);
                String valueOf = String.valueOf(Math.round(BrightnessUtils.convertLinearToGamma(interpolate4 / f) * 100.0f));
                String valueOf2 = String.valueOf(Math.round(BrightnessUtils.convertLinearToGamma(interpolate4) * 100.0f));
                String str8 = str6 + "%" + Math.max(str7.length(), Math.max(strFloatForDump.length(), Math.max(strFloatForDump4.length(), Math.max(valueOf.length(), Math.max(valueOf2.length(), Math.max(strFloatForDump2.length(), strFloatForDump3.length())))))) + "s";
                sb.append(TextUtils.formatSimple(str8, new Object[]{str7}));
                sb2.append(TextUtils.formatSimple(str8, new Object[]{strFloatForDump}));
                sb3.append(TextUtils.formatSimple(str8, new Object[]{strFloatForDump2}));
                sb4.append(TextUtils.formatSimple(str8, new Object[]{strFloatForDump3}));
                sb5 = sb5;
                sb5.append(TextUtils.formatSimple(str8, new Object[]{strFloatForDump4}));
                sb6 = sb6;
                sb6.append(TextUtils.formatSimple(str8, new Object[]{valueOf}));
                sb7 = sb7;
                sb7.append(TextUtils.formatSimple(str8, new Object[]{valueOf2}));
                if (sb.length() <= 80) {
                    fArr2 = fArr;
                    z = true;
                    i = i3;
                    if (i != fArr2.length - 1) {
                        str4 = ", ";
                        str3 = str;
                        printWriter2 = printWriter;
                        i2 = i + 1;
                        str5 = str3;
                        str6 = str4;
                        createSpline2 = spline;
                        createSpline = spline2;
                        physicalMappingStrategy = this;
                    }
                } else {
                    i = i3;
                    fArr2 = fArr;
                    z = true;
                }
                printWriter2 = printWriter;
                printWriter2.println(sb);
                printWriter2.println(sb2);
                printWriter2.println(sb3);
                printWriter2.println(sb4);
                printWriter2.println(sb5);
                printWriter2.println(sb6);
                if (f < 1.0f) {
                    printWriter2.println(sb7);
                }
                str3 = str;
                printWriter2.println(str3);
                z2 = z;
                str4 = str3;
                i2 = i + 1;
                str5 = str3;
                str6 = str4;
                createSpline2 = spline;
                createSpline = spline2;
                physicalMappingStrategy = this;
            }
        }

        private String toStrFloatForDump(float f) {
            if (f == 0.0f) {
                return "0";
            }
            if (f < 0.1f) {
                return String.format(Locale.US, "%.3f", Float.valueOf(f));
            }
            if (f < 1.0f) {
                return String.format(Locale.US, "%.2f", Float.valueOf(f));
            }
            if (f < 10.0f) {
                return String.format(Locale.US, "%.1f", Float.valueOf(f));
            }
            return TextUtils.formatSimple("%d", new Object[]{Integer.valueOf(Math.round(f))});
        }

        private void computeNitsBrightnessSplines(float[] fArr) {
            this.mNitsToBrightnessSpline = Spline.createSpline(fArr, this.mBrightness);
            this.mBrightnessToNitsSpline = Spline.createSpline(this.mBrightness, fArr);
        }

        private void computeSpline() {
            Pair curve = this.mConfig.getCurve();
            float[] fArr = (float[]) curve.first;
            float[] fArr2 = (float[]) curve.second;
            int length = fArr2.length;
            float[] fArr3 = new float[length];
            for (int i = 0; i < length; i++) {
                fArr3[i] = this.mAdjustedNitsToBrightnessSpline.interpolate(fArr2[i]);
            }
            Pair<float[], float[]> adjustedCurve = getAdjustedCurve(fArr, fArr3, this.mUserLux, this.mUserBrightness, this.mAutoBrightnessAdjustment, this.mMaxGamma);
            float[] fArr4 = (float[]) adjustedCurve.first;
            float[] fArr5 = (float[]) adjustedCurve.second;
            int length2 = fArr5.length;
            float[] fArr6 = new float[length2];
            for (int i2 = 0; i2 < length2; i2++) {
                fArr6[i2] = this.mBrightnessToAdjustedNitsSpline.interpolate(fArr5[i2]);
            }
            this.mBrightnessSpline = Spline.createSpline(fArr4, fArr6);
        }

        private float getUnadjustedBrightness(float f) {
            Pair curve = this.mConfig.getCurve();
            return this.mAdjustedNitsToBrightnessSpline.interpolate(Spline.createSpline((float[]) curve.first, (float[]) curve.second).interpolate(f));
        }

        private float correctBrightness(float f, String str, int i) {
            BrightnessCorrection correctionByCategory;
            BrightnessCorrection correctionByPackageName;
            if (str == null || (correctionByPackageName = this.mConfig.getCorrectionByPackageName(str)) == null) {
                return (i == -1 || (correctionByCategory = this.mConfig.getCorrectionByCategory(i)) == null) ? f : correctionByCategory.apply(f);
            }
            return correctionByPackageName.apply(f);
        }
    }
}
