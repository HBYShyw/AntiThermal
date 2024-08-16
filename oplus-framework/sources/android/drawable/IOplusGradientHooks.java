package android.drawable;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/* loaded from: classes.dex */
public interface IOplusGradientHooks extends IOplusCommonFeature {
    public static final IOplusGradientHooks DEFAULT = new IOplusGradientHooks() { // from class: android.drawable.IOplusGradientHooks.1
    };

    default IOplusGradientHooks getDefault() {
        return DEFAULT;
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusGradientHooks;
    }

    default void drawRoundRect(boolean smoothRound, Canvas canvas, RectF rect, float rad, boolean haveStroke, Paint fillPaint, Paint strokePaint) {
        canvas.drawRoundRect(rect, rad, rad, fillPaint);
        if (haveStroke) {
            canvas.drawRoundRect(rect, rad, rad, strokePaint);
        }
    }
}
