package android.view.autolayout;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

/* loaded from: classes.dex */
public interface IAutoLayoutDrawPolicy {
    public static final IAutoLayoutDrawPolicy DEFAULT = new IAutoLayoutDrawPolicy() { // from class: android.view.autolayout.IAutoLayoutDrawPolicy.1
    };

    default void beforeUpdateDisplayListIfDirty(View view) {
    }

    default void beforeDraw(View view, Canvas canvas) {
    }

    default void afterDraw(View view, Canvas canvas) {
    }

    default void drawBitmap(Bitmap bitmap, float left, float top, Paint paint) {
    }

    default void drawBitmap(Bitmap bitmap, Matrix matrix, Paint paint) {
    }

    default Rect drawBitmap(Bitmap bitmap, Rect src, Rect dst, Paint paint) {
        return dst;
    }

    default void drawBitmap(Bitmap bitmap, Rect src, RectF dst, Paint paint) {
    }

    default void start() {
    }

    default void end() {
    }
}
