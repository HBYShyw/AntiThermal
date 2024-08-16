package com.android.server.display.color;

import android.R;
import android.animation.ValueAnimator;
import android.content.Context;
import android.hardware.display.ColorDisplayManager;
import android.opengl.Matrix;
import android.util.Slog;
import java.io.PrintWriter;
import java.util.Arrays;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ReduceBrightColorsTintController extends TintController {
    private int mStrength;
    private final float[] mMatrix = new float[16];
    private final float[] mCoefficients = new float[3];

    private float clamp(float f) {
        if (f > 1.0f) {
            return 1.0f;
        }
        if (f < 0.0f) {
            return 0.0f;
        }
        return f;
    }

    @Override // com.android.server.display.color.TintController
    public int getLevel() {
        return 250;
    }

    @Override // com.android.server.display.color.TintController
    public /* bridge */ /* synthetic */ void cancelAnimator() {
        super.cancelAnimator();
    }

    @Override // com.android.server.display.color.TintController
    public /* bridge */ /* synthetic */ void endAnimator() {
        super.endAnimator();
    }

    @Override // com.android.server.display.color.TintController
    public /* bridge */ /* synthetic */ ValueAnimator getAnimator() {
        return super.getAnimator();
    }

    @Override // com.android.server.display.color.TintController
    public /* bridge */ /* synthetic */ long getTransitionDurationMilliseconds() {
        return super.getTransitionDurationMilliseconds();
    }

    @Override // com.android.server.display.color.TintController
    public /* bridge */ /* synthetic */ boolean isActivated() {
        return super.isActivated();
    }

    @Override // com.android.server.display.color.TintController
    public /* bridge */ /* synthetic */ boolean isActivatedStateNotSet() {
        return super.isActivatedStateNotSet();
    }

    @Override // com.android.server.display.color.TintController
    public /* bridge */ /* synthetic */ void setAnimator(ValueAnimator valueAnimator) {
        super.setAnimator(valueAnimator);
    }

    @Override // com.android.server.display.color.TintController
    public void setUp(Context context, boolean z) {
        String[] stringArray = context.getResources().getStringArray(z ? R.array.resolver_target_actions_unpin : R.array.sim_colors);
        for (int i = 0; i < 3 && i < stringArray.length; i++) {
            this.mCoefficients[i] = Float.parseFloat(stringArray[i]);
        }
    }

    @Override // com.android.server.display.color.TintController
    public float[] getMatrix() {
        if (!isActivated()) {
            return ColorDisplayService.MATRIX_IDENTITY;
        }
        float[] fArr = this.mMatrix;
        return Arrays.copyOf(fArr, fArr.length);
    }

    @Override // com.android.server.display.color.TintController
    public void setMatrix(int i) {
        if (i < 0) {
            i = 0;
        } else if (i > 100) {
            i = 100;
        }
        Slog.d("ColorDisplayService", "Setting bright color reduction level: " + i);
        this.mStrength = i;
        Matrix.setIdentityM(this.mMatrix, 0);
        float computeComponentValue = computeComponentValue(i);
        float[] fArr = this.mMatrix;
        fArr[0] = computeComponentValue;
        fArr[5] = computeComponentValue;
        fArr[10] = computeComponentValue;
    }

    @Override // com.android.server.display.color.TintController
    public void dump(PrintWriter printWriter) {
        printWriter.println("    mStrength = " + this.mStrength);
    }

    @Override // com.android.server.display.color.TintController
    public boolean isAvailable(Context context) {
        return ColorDisplayManager.isColorTransformAccelerated(context);
    }

    @Override // com.android.server.display.color.TintController
    public void setActivated(Boolean bool) {
        super.setActivated(bool);
        Slog.i("ColorDisplayService", (bool == null || !bool.booleanValue()) ? "Turning off reduce bright colors" : "Turning on reduce bright colors");
    }

    public int getStrength() {
        return this.mStrength;
    }

    public float getOffsetFactor() {
        float[] fArr = this.mCoefficients;
        return fArr[0] + fArr[1] + fArr[2];
    }

    public float getAdjustedBrightness(float f) {
        return computeComponentValue(this.mStrength) * f;
    }

    private float computeComponentValue(int i) {
        float f = i / 100.0f;
        float[] fArr = this.mCoefficients;
        return clamp((f * f * fArr[0]) + (f * fArr[1]) + fArr[2]);
    }
}
