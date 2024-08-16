package android.view.autolayout;

import android.util.DisplayMetrics;
import android.view.View;

/* loaded from: classes.dex */
public interface IAutoLayoutMeasurePolicy {
    public static final IAutoLayoutMeasurePolicy DEFAULT = new IAutoLayoutMeasurePolicy() { // from class: android.view.autolayout.IAutoLayoutMeasurePolicy.1
    };

    default int[] beforeMeasure(View view, int widthMeasureSpec, int heightMeasureSpec) {
        return new int[]{widthMeasureSpec, heightMeasureSpec};
    }

    default int[] hookSetMeasureDimension(View view, int measuredWidth, int measuredHeight) {
        return new int[]{measuredWidth, measuredHeight};
    }

    default void afterMeasure(View view) {
    }

    default void setOriginalDisplayMetrics(DisplayMetrics originalDisplayMetrics) {
    }
}
