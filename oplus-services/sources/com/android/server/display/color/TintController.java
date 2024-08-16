package com.android.server.display.color;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Slog;
import java.io.PrintWriter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class TintController {
    private static final long TRANSITION_DURATION = 3000;
    private ValueAnimator mAnimator;
    private Boolean mIsActivated;

    public void dump(PrintWriter printWriter) {
    }

    public abstract int getLevel();

    public abstract float[] getMatrix();

    public long getTransitionDurationMilliseconds() {
        return 3000L;
    }

    public abstract boolean isAvailable(Context context);

    public abstract void setMatrix(int i);

    public abstract void setUp(Context context, boolean z);

    public ValueAnimator getAnimator() {
        return this.mAnimator;
    }

    public void setAnimator(ValueAnimator valueAnimator) {
        this.mAnimator = valueAnimator;
    }

    public void cancelAnimator() {
        ValueAnimator valueAnimator = this.mAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    public void endAnimator() {
        ValueAnimator valueAnimator = this.mAnimator;
        if (valueAnimator != null) {
            valueAnimator.end();
            this.mAnimator = null;
        }
    }

    public void setActivated(Boolean bool) {
        this.mIsActivated = bool;
    }

    public boolean isActivated() {
        Boolean bool = this.mIsActivated;
        return bool != null && bool.booleanValue();
    }

    public boolean isActivatedStateNotSet() {
        return this.mIsActivated == null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String matrixToString(float[] fArr, int i) {
        if (fArr == null || i <= 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid arguments when formatting matrix to string, matrix is null: ");
            sb.append(fArr == null);
            sb.append(" columns: ");
            sb.append(i);
            Slog.e("ColorDisplayService", sb.toString());
            return "";
        }
        StringBuilder sb2 = new StringBuilder("");
        for (int i2 = 0; i2 < fArr.length; i2++) {
            if (i2 % i == 0) {
                sb2.append("\n      ");
            }
            sb2.append(String.format("%9.6f", Float.valueOf(fArr[i2])));
        }
        return sb2.toString();
    }
}
