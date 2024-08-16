package android.view;

import android.graphics.Rect;
import android.view.WindowManager;

/* loaded from: classes.dex */
public class OplusScreenDragUtil {
    public static void setScreenDragState(int dragState) {
    }

    public static int getScreenDragState() {
        return 0;
    }

    public static int getOffsetX() {
        return 0;
    }

    public static int getOffsetY() {
        return 0;
    }

    public static float getScale() {
        return 0.0f;
    }

    public static int getWidth() {
        return 0;
    }

    public static int getHeight() {
        return 0;
    }

    public static boolean isNormalState() {
        return false;
    }

    public static boolean isDragState() {
        return false;
    }

    public static boolean isHoldState() {
        return false;
    }

    public static boolean isOffsetState() {
        return false;
    }

    public static void resetState() {
    }

    public static float getOffsetPosX(float x) {
        return 0.0f;
    }

    public static float getOffsetPosY(float y) {
        return 0.0f;
    }

    public static float getOffsetPosXScale(float x, float scale) {
        return 0.0f;
    }

    public static float getOffsetPosYScale(float y, float scale) {
        return 0.0f;
    }

    public static void scaleScreenshotIfNeeded(Rect sourceCrop) {
    }

    private static float getXOffset(MotionEvent event) {
        return 0.0f;
    }

    private static float getYOffset(MotionEvent event) {
        return 0.0f;
    }

    public static int shouldMagnify(WindowManager.LayoutParams attr) {
        return -1;
    }
}
