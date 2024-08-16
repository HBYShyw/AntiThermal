package com.android.server.display.brightness.strategy;

import android.content.Context;
import android.hardware.display.BrightnessConfiguration;
import android.provider.Settings;
import android.view.Display;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.display.AutomaticBrightnessController;
import com.android.server.display.brightness.BrightnessEvent;
import com.android.server.display.brightness.BrightnessUtils;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AutomaticBrightnessStrategy {
    private boolean mAppliedTemporaryAutoBrightnessAdjustment;
    private boolean mAutoBrightnessAdjustmentChanged;
    private AutomaticBrightnessController mAutomaticBrightnessController;
    private BrightnessConfiguration mBrightnessConfiguration;
    private final Context mContext;
    private final int mDisplayId;
    private int mAutoBrightnessAdjustmentReasonsFlags = 0;
    private boolean mShouldResetShortTermModel = false;
    private boolean mAppliedAutoBrightness = false;
    private boolean mUseAutoBrightness = false;
    private boolean mIsAutoBrightnessEnabled = false;
    private boolean mIsShortTermModelActive = false;
    private float mAutoBrightnessAdjustment = getAutoBrightnessAdjustmentSetting();
    private float mPendingAutoBrightnessAdjustment = Float.NaN;
    private float mTemporaryAutoBrightnessAdjustment = Float.NaN;

    public AutomaticBrightnessStrategy(Context context, int i) {
        this.mContext = context;
        this.mDisplayId = i;
    }

    public void setAutoBrightnessState(int i, boolean z, float f, int i2, int i3, float f2, boolean z2) {
        int i4;
        boolean z3 = false;
        int i5 = 1;
        boolean z4 = z && Display.isDozeState(i);
        this.mIsAutoBrightnessEnabled = shouldUseAutoBrightness() && (i == 2 || z4) && !((!Float.isNaN(f) && i2 != 7 && i2 != 8) || this.mAutomaticBrightnessController == null || i2 == 10);
        if (shouldUseAutoBrightness() && i != 2 && !z4) {
            z3 = true;
        }
        if (!this.mIsAutoBrightnessEnabled) {
            if (!z3) {
                i4 = 2;
                accommodateUserBrightnessChanges(z2, f2, i3, this.mBrightnessConfiguration, i4);
            }
            i5 = 3;
        }
        i4 = i5;
        accommodateUserBrightnessChanges(z2, f2, i3, this.mBrightnessConfiguration, i4);
    }

    public boolean isAutoBrightnessEnabled() {
        return this.mIsAutoBrightnessEnabled;
    }

    public void setBrightnessConfiguration(BrightnessConfiguration brightnessConfiguration, boolean z) {
        this.mBrightnessConfiguration = brightnessConfiguration;
        setShouldResetShortTermModel(z);
    }

    public boolean processPendingAutoBrightnessAdjustments() {
        this.mAutoBrightnessAdjustmentChanged = false;
        if (Float.isNaN(this.mPendingAutoBrightnessAdjustment)) {
            return false;
        }
        float f = this.mAutoBrightnessAdjustment;
        float f2 = this.mPendingAutoBrightnessAdjustment;
        if (f == f2) {
            this.mPendingAutoBrightnessAdjustment = Float.NaN;
            return false;
        }
        this.mAutoBrightnessAdjustment = f2;
        this.mPendingAutoBrightnessAdjustment = Float.NaN;
        this.mTemporaryAutoBrightnessAdjustment = Float.NaN;
        this.mAutoBrightnessAdjustmentChanged = true;
        return true;
    }

    public void setAutomaticBrightnessController(AutomaticBrightnessController automaticBrightnessController) {
        AutomaticBrightnessController automaticBrightnessController2 = this.mAutomaticBrightnessController;
        if (automaticBrightnessController == automaticBrightnessController2) {
            return;
        }
        if (automaticBrightnessController2 != null) {
            automaticBrightnessController2.stop();
        }
        this.mAutomaticBrightnessController = automaticBrightnessController;
    }

    public boolean shouldUseAutoBrightness() {
        return this.mUseAutoBrightness;
    }

    public void setUseAutoBrightness(boolean z) {
        this.mUseAutoBrightness = z;
    }

    public boolean isShortTermModelActive() {
        return this.mIsShortTermModelActive;
    }

    public void updatePendingAutoBrightnessAdjustments(boolean z) {
        float floatForUser = Settings.System.getFloatForUser(this.mContext.getContentResolver(), "screen_auto_brightness_adj", 0.0f, -2);
        this.mPendingAutoBrightnessAdjustment = Float.isNaN(floatForUser) ? Float.NaN : BrightnessUtils.clampBrightnessAdjustment(floatForUser);
        if (z) {
            processPendingAutoBrightnessAdjustments();
        }
    }

    public void setTemporaryAutoBrightnessAdjustment(float f) {
        this.mTemporaryAutoBrightnessAdjustment = f;
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("AutomaticBrightnessStrategy:");
        printWriter.println("  mDisplayId=" + this.mDisplayId);
        printWriter.println("  mAutoBrightnessAdjustment=" + this.mAutoBrightnessAdjustment);
        printWriter.println("  mPendingAutoBrightnessAdjustment=" + this.mPendingAutoBrightnessAdjustment);
        printWriter.println("  mTemporaryAutoBrightnessAdjustment=" + this.mTemporaryAutoBrightnessAdjustment);
        printWriter.println("  mShouldResetShortTermModel=" + this.mShouldResetShortTermModel);
        printWriter.println("  mAppliedAutoBrightness=" + this.mAppliedAutoBrightness);
        printWriter.println("  mAutoBrightnessAdjustmentChanged=" + this.mAutoBrightnessAdjustmentChanged);
        printWriter.println("  mAppliedTemporaryAutoBrightnessAdjustment=" + this.mAppliedTemporaryAutoBrightnessAdjustment);
        printWriter.println("  mUseAutoBrightness=" + this.mUseAutoBrightness);
        printWriter.println("  mWasShortTermModelActive=" + this.mIsShortTermModelActive);
        printWriter.println("  mAutoBrightnessAdjustmentReasonsFlags=" + this.mAutoBrightnessAdjustmentReasonsFlags);
    }

    public boolean getAutoBrightnessAdjustmentChanged() {
        return this.mAutoBrightnessAdjustmentChanged;
    }

    public boolean isTemporaryAutoBrightnessAdjustmentApplied() {
        return this.mAppliedTemporaryAutoBrightnessAdjustment;
    }

    public float getAutomaticScreenBrightness(BrightnessEvent brightnessEvent) {
        AutomaticBrightnessController automaticBrightnessController = this.mAutomaticBrightnessController;
        float automaticScreenBrightness = automaticBrightnessController != null ? automaticBrightnessController.getAutomaticScreenBrightness(brightnessEvent) : Float.NaN;
        adjustAutomaticBrightnessStateIfValid(automaticScreenBrightness);
        return automaticScreenBrightness;
    }

    public int getAutoBrightnessAdjustmentReasonsFlags() {
        return this.mAutoBrightnessAdjustmentReasonsFlags;
    }

    public boolean hasAppliedAutoBrightness() {
        return this.mAppliedAutoBrightness;
    }

    @VisibleForTesting
    void adjustAutomaticBrightnessStateIfValid(float f) {
        boolean z = true;
        this.mAutoBrightnessAdjustmentReasonsFlags = isTemporaryAutoBrightnessAdjustmentApplied() ? 1 : 2;
        if (!BrightnessUtils.isValidBrightnessValue(f) && f != -1.0f) {
            z = false;
        }
        this.mAppliedAutoBrightness = z;
        AutomaticBrightnessController automaticBrightnessController = this.mAutomaticBrightnessController;
        float automaticScreenBrightnessAdjustment = automaticBrightnessController != null ? automaticBrightnessController.getAutomaticScreenBrightnessAdjustment() : 0.0f;
        if (!Float.isNaN(automaticScreenBrightnessAdjustment) && this.mAutoBrightnessAdjustment != automaticScreenBrightnessAdjustment) {
            putAutoBrightnessAdjustmentSetting(automaticScreenBrightnessAdjustment);
        } else {
            this.mAutoBrightnessAdjustmentReasonsFlags = 0;
        }
    }

    @VisibleForTesting
    void setShouldResetShortTermModel(boolean z) {
        this.mShouldResetShortTermModel = z;
    }

    @VisibleForTesting
    boolean shouldResetShortTermModel() {
        return this.mShouldResetShortTermModel;
    }

    @VisibleForTesting
    float getAutoBrightnessAdjustment() {
        return this.mAutoBrightnessAdjustment;
    }

    @VisibleForTesting
    float getPendingAutoBrightnessAdjustment() {
        return this.mPendingAutoBrightnessAdjustment;
    }

    @VisibleForTesting
    float getTemporaryAutoBrightnessAdjustment() {
        return this.mTemporaryAutoBrightnessAdjustment;
    }

    @VisibleForTesting
    void putAutoBrightnessAdjustmentSetting(float f) {
        if (this.mDisplayId == 0) {
            this.mAutoBrightnessAdjustment = f;
            Settings.System.putFloatForUser(this.mContext.getContentResolver(), "screen_auto_brightness_adj", f, -2);
        }
    }

    @VisibleForTesting
    void setAutoBrightnessApplied(boolean z) {
        this.mAppliedAutoBrightness = z;
    }

    @VisibleForTesting
    void accommodateUserBrightnessChanges(boolean z, float f, int i, BrightnessConfiguration brightnessConfiguration, int i2) {
        processPendingAutoBrightnessAdjustments();
        float updateTemporaryAutoBrightnessAdjustments = updateTemporaryAutoBrightnessAdjustments();
        this.mIsShortTermModelActive = false;
        AutomaticBrightnessController automaticBrightnessController = this.mAutomaticBrightnessController;
        if (automaticBrightnessController != null) {
            automaticBrightnessController.configure(i2, brightnessConfiguration, f, z, updateTemporaryAutoBrightnessAdjustments, this.mAutoBrightnessAdjustmentChanged, i, this.mShouldResetShortTermModel);
            this.mShouldResetShortTermModel = false;
            this.mIsShortTermModelActive = this.mAutomaticBrightnessController.hasUserDataPoints();
        }
    }

    private float updateTemporaryAutoBrightnessAdjustments() {
        boolean z = !Float.isNaN(this.mTemporaryAutoBrightnessAdjustment);
        this.mAppliedTemporaryAutoBrightnessAdjustment = z;
        return z ? this.mTemporaryAutoBrightnessAdjustment : this.mAutoBrightnessAdjustment;
    }

    private float getAutoBrightnessAdjustmentSetting() {
        float floatForUser = Settings.System.getFloatForUser(this.mContext.getContentResolver(), "screen_auto_brightness_adj", 0.0f, -2);
        if (Float.isNaN(floatForUser)) {
            return 0.0f;
        }
        return BrightnessUtils.clampBrightnessAdjustment(floatForUser);
    }
}
