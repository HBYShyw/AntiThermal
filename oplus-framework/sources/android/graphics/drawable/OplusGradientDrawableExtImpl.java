package android.graphics.drawable;

import android.common.OplusFeatureCache;
import android.drawable.IOplusGradientHooks;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/* loaded from: classes.dex */
public class OplusGradientDrawableExtImpl implements IGradientDrawableExt {
    private GradientDrawable mBase;
    private boolean mGradientStateSmoothRound;
    private boolean mSmoothRound;

    public OplusGradientDrawableExtImpl(Object base) {
        this.mBase = (GradientDrawable) base;
    }

    public void drawRoundRect(boolean smoothRound, Canvas canvas, RectF rect, float rad, boolean haveStroke, Paint fillPaint, Paint strokePaint) {
        ((IOplusGradientHooks) OplusFeatureCache.getOrCreate(IOplusGradientHooks.DEFAULT, new Object[0])).drawRoundRect(smoothRound, canvas, rect, rad, haveStroke, fillPaint, strokePaint);
    }

    public boolean checkElementsName(String name) {
        if (name.trim().equals("smooth-rect")) {
            this.mSmoothRound = true;
            this.mGradientStateSmoothRound = true;
            return true;
        }
        return false;
    }

    public void setSmoothRoundStyle(boolean smooth) {
        this.mSmoothRound = smooth;
    }

    public boolean getSmoothRoundStyle() {
        return this.mSmoothRound;
    }

    public boolean getGradientStateSmoothRoundStyle() {
        return this.mGradientStateSmoothRound;
    }
}
