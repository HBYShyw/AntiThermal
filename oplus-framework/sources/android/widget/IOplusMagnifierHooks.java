package android.widget;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.RecordingCanvas;

/* loaded from: classes.dex */
public interface IOplusMagnifierHooks extends IOplusCommonFeature {
    public static final IOplusMagnifierHooks DEFAULT = new IOplusMagnifierHooks() { // from class: android.widget.IOplusMagnifierHooks.1
    };

    default IOplusMagnifierHooks getDefault() {
        return DEFAULT;
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusMagnifierHooks;
    }

    default int getMagnifierWidth(TypedArray a, Context context) {
        return a.getDimensionPixelSize(5, 0);
    }

    default int getMagnifierHeight(TypedArray a, Context context) {
        return a.getDimensionPixelSize(2, 0);
    }

    default float getMagnifierCornerRadius(TypedArray a, Context context) {
        return a.getDimension(0, 0.0f);
    }

    default void decodeShadowBitmap(Context context) {
    }

    default void recycleShadowBitmap() {
    }

    default void drawShadowBitmap(int contentWidth, int contentHeight, RecordingCanvas canvas, Paint paint) {
    }
}
